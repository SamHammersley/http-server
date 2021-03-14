package com.gonsalves;

import com.gonsalves.net.http.HttpServer;

public final class EntryPoint {

    private static final int PORT = 8080;

    public static void main(String[] args) {
        HttpServer server = new HttpServer(PORT);

        server.run();
    }

}