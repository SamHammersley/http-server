package com.gonsalves.net.http;

public final class HttpRequestLine {

    private final HttpMethod method;

    private final String target;

    private final String version;

    public HttpRequestLine(HttpMethod method, String target, String version) {
        this.method = method;
        this.target = target;
        this.version = version;
    }

    @Override
    public String toString() {
        return method.name() + ": " + target;
    }

}