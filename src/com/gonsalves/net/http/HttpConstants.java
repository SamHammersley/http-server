package com.gonsalves.net.http;

public final class HttpConstants {

    public static final String RESPONSE = "HTTP/1.1 200 OK\n\nHello, world!";

    /**
     * RFC 7230 declares that the minimum supported request line size should be 8000 octets.
     */
    public static final int MAX_START_LINE_SIZE = 1 << 13;

    private HttpConstants() {

    }

}