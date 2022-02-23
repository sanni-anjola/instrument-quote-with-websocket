package io.anjola.instrumentswithwebsocket.config;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.net.URI;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class SimpleClient extends TextWebSocketHandler {


    private WebSocketSession clientSession;

    public SimpleClient () throws ExecutionException, InterruptedException {
        var webSocketClient = new StandardWebSocketClient();
        this.clientSession = webSocketClient.doHandshake(this, new WebSocketHttpHeaders(), URI.create("ws://localhost:8032/quotes")).get();
    }

    public WebSocketSession getClientSession() {
        return clientSession;
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        System.out.println(message.getPayload());
    }

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
//        var simpleClient =  new SimpleClient();
//        simpleClient.getClientSession().sendMessage(new TextMessage("Hello!"));
//        Thread.sleep(20000);
        List<Instant> times = new ArrayList<>();
        Instant start = Instant.now().truncatedTo(ChronoUnit.MINUTES);
        Instant end = start.plus(30, ChronoUnit.MINUTES);

        System.out.println(start);
        System.out.println(end);

        while (start.isBefore(end)){
            times.add(start);
            start = start.plus(1, ChronoUnit.MINUTES);
        }
        System.out.println(times.size());




    }
}
