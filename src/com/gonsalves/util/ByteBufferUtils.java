package com.gonsalves.util;

import java.nio.ByteBuffer;
import java.util.function.Function;

public final class ByteBufferUtils {

    private ByteBufferUtils() {

    }

    public static <T> T getAs(ByteBuffer buffer, int bytes, Function<byte[], T> mappingFunction) {
        byte[] buff = new byte[bytes];
        buffer.get(buff);

        return mappingFunction.apply(buff);
    }

}