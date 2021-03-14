package com.gonsalves.net.http;

import com.gonsalves.net.MessageHandler;

import java.io.IOException;
import java.nio.channels.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class HttpServer implements Runnable {

    private final int port;

    public HttpServer(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        try {
            Selector clientSelector = Selector.open();

            ExecutorService executor = Executors.newFixedThreadPool(2);
            executor.submit(new HttpGateway(port, clientSelector));
            executor.submit(new MessageHandler(clientSelector));

        } catch(IOException e) {
            throw new RuntimeException("Failed to open Selector for clients", e);
        }
    }

}