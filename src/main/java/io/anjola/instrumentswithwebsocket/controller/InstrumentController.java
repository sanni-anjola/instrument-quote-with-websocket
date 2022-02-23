package io.anjola.instrumentswithwebsocket.controller;

import io.anjola.instrumentswithwebsocket.dto.CandleStick;
import io.anjola.instrumentswithwebsocket.service.InstrumentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class InstrumentController {

    private final InstrumentService instrumentService;

    public InstrumentController(InstrumentService instrumentService) {
        this.instrumentService = instrumentService;
    }

    @GetMapping("/candlesticks")

    public List<CandleStick> getCandleSticks(@RequestParam(name = "isin") String isin){
        return instrumentService.getCandleSticks(isin);
    }
}
