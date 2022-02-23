package io.anjola.instrumentswithwebsocket.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.anjola.instrumentswithwebsocket.model.Instrument;
import io.anjola.instrumentswithwebsocket.model.Quote;
import io.anjola.instrumentswithwebsocket.service.InstrumentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.Executors.newSingleThreadScheduledExecutor;

@Component
public class InstrumentConfig {
    @Autowired
    private InstrumentService service;
    @Autowired
    private ObjectMapper mapper;

    private static final Logger logger = LoggerFactory.getLogger(InstrumentConfig.class);

    @PostConstruct
    public void connect(){
        try {
            WebSocketClient webSocketClient = new StandardWebSocketClient();

//            WebSocketSession webSocketSession =
            webSocketClient.doHandshake(new TextWebSocketHandler(){
               @Override
               public void handleTextMessage(WebSocketSession session, TextMessage message) throws JsonProcessingException {
//                   logger.info("received message --> {}: time --> {}", message.getPayload(), Instant.now());
                   Instrument instrument = mapper.readValue(message.getPayload(), Instrument.class);
                   service.saveOrDeleteInstrument(instrument);
//                   logger.info("From DB --> {}", service.getInstruments());
               }
               @Override
                public void afterConnectionEstablished(WebSocketSession session){
                   logger.info("established connection - {}", session);
               }
            }, new WebSocketHttpHeaders(), URI.create("ws://localhost:8032/instruments")).get();

            webSocketClient.doHandshake(new TextWebSocketHandler(){
               @Override
               public void handleTextMessage(WebSocketSession session, TextMessage message) throws JsonProcessingException {
//                   logger.info("received message --> {}: time --> {}", message.getPayload(), Instant.now());
                   Quote quote = mapper.readValue(message.getPayload(), Quote.class);
                   service.saveQuote(quote);
//                   logger.info("From DB --> {}", service.getInstruments());
               }
               @Override
                public void afterConnectionEstablished(WebSocketSession session){
                   logger.info("established connection - {}", session);
               }
            }, new WebSocketHttpHeaders(), URI.create("ws://localhost:8032/quotes")).get();
//            newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
//                try {
//                    TextMessage message = new TextMessage("Hello !!");
//                    webSocketSession.sendMessage(message);
//                    logger.info("sent message - {}", message.getPayload());
//                } catch (Exception e) {
//                    logger.error("Exception while sending a message", e);
//                }
//            }, 1, 10, TimeUnit.SECONDS);
        } catch (Exception e) {
            logger.error("Exception while accessing websockets", e);
        }

    }
}
