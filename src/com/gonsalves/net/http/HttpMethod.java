package com.gonsalves.net.http;

import java.util.Optional;

public enum HttpMethod {

    GET,

    HEAD,

    POST,

    PUT,

    DELETE,

    CONNECT,

    OPTIONS,

    TRACE;

    public static Optional<HttpMethod> fromName(String name) {
        try {
            return Optional.of(HttpMethod.valueOf(name));

        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }

    }

}