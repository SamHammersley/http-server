package com.gonsalves.net.http;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public final class HttpGateway implements Runnable, AutoCloseable {

    private final int port;

    private final Selector clientSelector;

    private volatile boolean acceptingConnections;

    public HttpGateway(int port, Selector clientSelector) {
        this.port = port;
        this.clientSelector = clientSelector;
    }

    @Override
    public void run() {
        acceptingConnections = true;

        try (Selector selector = Selector.open();
             ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {

            serverSocketChannel.bind(new InetSocketAddress(port));
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            while (acceptingConnections) {
                selector.selectNow(key -> acceptConnection(serverSocketChannel));
            }

        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void declineConnections() {
        acceptingConnections = false;
    }

    private void acceptConnection(ServerSocketChannel serverSocketChannel) {
        try {
            SocketChannel channel = serverSocketChannel.accept();

            if (channel != null) {
                channel.configureBlocking(false).register(clientSelector, SelectionKey.OP_READ);
            }

        } catch(IOException e) {
            throw new RuntimeException("Failed to accept connection", e);
        }
    }

    @Override
    public void close() throws Exception {
        acceptingConnections = false;

        clientSelector.close();
    }
}