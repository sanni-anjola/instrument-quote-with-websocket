package io.anjola.instrumentswithwebsocket.service;

import io.anjola.instrumentswithwebsocket.dto.CandleStick;
import io.anjola.instrumentswithwebsocket.dto.PriceTime;
import io.anjola.instrumentswithwebsocket.enums.OperationType;
import io.anjola.instrumentswithwebsocket.model.Instrument;
import io.anjola.instrumentswithwebsocket.model.InstrumentQuote;
import io.anjola.instrumentswithwebsocket.model.Quote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class InstrumentService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final Map<String, InstrumentQuote> map = new HashMap<>();

    public Instrument saveOrDeleteInstrument(Instrument instrument){
        if(instrument.getType() == OperationType.DELETE){
            map.remove(instrument.getData().getIsin());
            return instrument;
        }

        InstrumentQuote instrumentQuote = new InstrumentQuote(instrument.getData().getIsin(), instrument.getData().getDescription());
        map.put(instrument.getData().getIsin(), instrumentQuote);
        return instrument;
    }

    public List<InstrumentQuote> getInstruments(){
        return map.values().stream().collect(Collectors.toList());
    }

    public Quote saveQuote(Quote quote) {

        String isin = quote.getData().getIsin();
        if(map.containsKey(isin)) {
            PriceTime priceTime = new PriceTime(quote.getData().getPrice(), quote.getData().getDatetime());
            map.get(isin).getPriceTimeList().add(priceTime);
            return quote;
        }
        return null;
    }

    public List<CandleStick> getCandleSticks(String isin){
        logger.info("{}", map.values().stream().filter(t -> t.getPriceTimeList().size() > 3).collect(Collectors.toList()));
        final int LAST_NO_OF_MINUTES_NEEDED = 30;
        InstrumentQuote instrumentQuote = map.get(isin);
        if(instrumentQuote == null) return new ArrayList<>();
        List<PriceTime> priceTimes = instrumentQuote.getPriceTimeList();
        if(priceTimes.isEmpty()) return new ArrayList<>();
        logger.info("{}", priceTimes);
        Instant start = Instant.now().truncatedTo(ChronoUnit.MINUTES).minus(LAST_NO_OF_MINUTES_NEEDED, ChronoUnit.MINUTES);
        Instant end = start.plus(LAST_NO_OF_MINUTES_NEEDED, ChronoUnit.MINUTES);
        List<Instant> times = new ArrayList<>();

        while (start.isBefore(end)){
            times.add(start);
            start = start.plus(1, ChronoUnit.MINUTES);
        }

//        List<List<PriceTime>> collect = times.stream()
//                .map(t -> priceTimes.stream()
//                        .filter(pt -> pt.getDatetime().isBefore(t))
//                        .collect(Collectors.toList()))
//                .collect(Collectors.toList());
        List<CandleStick> candleSticks = times.stream()
                .map(t -> priceTimes.stream()
                        .filter(pt -> pt.getDatetime().isAfter(t) && pt.getDatetime().isBefore(t.plusSeconds(60)))
                        .collect(Collectors.toList()))
                .map(this::getCandleStickFromList)
                .collect(Collectors.toList());

        for (int i = 0; i < candleSticks.size(); i++) {
            if(i > 0 && isEmpty(candleSticks.get(i)) && !isEmpty(candleSticks.get(i - 1))) candleSticks.set(i, candleSticks.get(i - 1));
        }

        CandleStick initialStick = new CandleStick();
        int initialPoint = 0;
        for (int i = 0; i < candleSticks.size(); i++) {
            if (i > 0 && !isEmpty(candleSticks.get(i))){
                initialStick = candleSticks.get(i);
                initialPoint = i;
                break;
            }
        }

        for (int i = 0; i < initialPoint; i++) {
            candleSticks.set(i, initialStick);
        }

        return candleSticks;
    }

    private boolean isEmpty(CandleStick candleStick){
        return candleStick.getOpenPrice() == null || candleStick.getLowPrice() == null || candleStick.getClosePrice() == null || candleStick.getHighPrice() == null;
    }

    private CandleStick getCandleStickFromList(List<PriceTime> priceTimes){
        if(priceTimes.isEmpty()) return new CandleStick();
        BigDecimal max = priceTimes.stream()
                .parallel()
                .map(PriceTime::getPrice)
                .max(BigDecimal::compareTo).orElse(null);

        BigDecimal min = priceTimes.stream()
                .parallel()
                .map(PriceTime::getPrice)
                .min(BigDecimal::compareTo).orElse(null);

        Instant open = priceTimes.stream()
                .parallel()
                .map(PriceTime::getDatetime)
                .min(Instant::compareTo)
                .orElse(null);

//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        CandleStick candleStick = new CandleStick();
        candleStick.setHighPrice(max);
        candleStick.setLowPrice(min);
        candleStick.setOpenPrice(priceTimes.get(0).getPrice());
        candleStick.setClosePrice(priceTimes.get(priceTimes.size() - 1).getPrice());
        candleStick.setOpenTimeStamp(open.truncatedTo(ChronoUnit.MINUTES));
        candleStick.setCloseTimeStamp(open.truncatedTo(ChronoUnit.MINUTES).plus(1, ChronoUnit.MINUTES));
        return candleStick;

    }
}