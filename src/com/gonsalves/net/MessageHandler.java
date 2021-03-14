package com.gonsalves.net;

import com.gonsalves.net.http.HttpConstants;
import com.gonsalves.net.http.HttpMethod;
import com.gonsalves.net.http.HttpRequestLine;
import com.gonsalves.util.ByteBufferUtils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Optional;

/**
 * Unfinished pass messages to decoder.
 */
public final class MessageHandler implements Runnable {

    private final Selector clientSelector;

    public MessageHandler(Selector clientSelector) {
        this.clientSelector = clientSelector;
    }

    @Override
    public void run() {
        try {

            while (true) {
                clientSelector.selectNow(key -> {
                    SocketChannel channel = (SocketChannel) key.channel();

                    read(channel);
                    write(channel);
                });
            }

        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    private void read(SocketChannel channel) {
        ByteBuffer buffer = ByteBuffer.allocate(HttpConstants.MAX_START_LINE_SIZE);

        try {
            final int startLineBytes = channel.read(buffer);

            if (startLineBytes == -1) {
                return;
            }
            buffer.flip();

            String requestString = ByteBufferUtils.getAs(buffer, startLineBytes, String::new);

            String startLine = requestString.split("\n")[0];
            HttpRequestLine requestLine = decodeRequestLine(startLine);

            System.out.println(requestString);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read from " + channel.socket().getInetAddress(), e);
        }
    }

    private HttpRequestLine decodeRequestLine(String requestString) {
        String[] parts = requestString.split(" ");

        if (parts.length != 3) {
            // malformed request.
            return null;
        }

        Optional<HttpMethod> method = HttpMethod.fromName(parts[0]);

        if (method.isEmpty()) {
            // stop here, unsupported (not implemented?) method.
            return null;
        }

        return new HttpRequestLine(method.get(), parts[1], parts[2]);
    }

    private void write(SocketChannel channel) {
        ByteBuffer buffer = ByteBuffer.wrap(HttpConstants.RESPONSE.getBytes());

        try {
            while (buffer.hasRemaining()) {
                channel.write(buffer);
            }

            channel.close();
        } catch(IOException e) {
            throw new RuntimeException("Failed to write to " + channel.socket().getInetAddress(), e);
        }
    }

}