package com.ne4ay.interligar.websocket;


import com.ne4ay.interligar.Channel;
import com.ne4ay.interligar.messages.Message;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static com.ne4ay.interligar.messages.MessagesUtils.serialize;

public class SocketServer extends WebSocketServer implements Channel {

    private final Runnable onStart;
    private final BiConsumer<WebSocket, ClientHandshake> onClientConnected;
    private final CloseHandler onClose;
    private final Runnable onStop;
    private final Consumer<Exception> exceptionHandler;

    private volatile boolean isRunning = false;
    private volatile boolean isConnected = false;
    private volatile WebSocket client;

    public SocketServer(int port,
        Runnable onStart,
        BiConsumer<WebSocket, ClientHandshake> onClientConnected,
        CloseHandler onConnectionClose,
        Runnable onStop,
        Consumer<Exception> exceptionHandler)
    {
        super(new InetSocketAddress(port));
        this.onStart = onStart;
        this.onClientConnected = onClientConnected;
        this.onClose = onConnectionClose;
        this.onStop = onStop;
        this.exceptionHandler = exceptionHandler;
    }

    @Override
    public void sendMessage(Message<?> message) {
        serialize(message, this.exceptionHandler)
            .ifPresent(this::broadcast);
    }

    @Override
    public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
        this.isConnected = true;
        this.client = webSocket;
        this.onClientConnected.accept(webSocket, clientHandshake);
    }

    @Override
    public void onClose(WebSocket webSocket, int i, String s, boolean b) {
        this.isRunning = false;
        this.isConnected = false;
    }

    @Override
    public void stop() throws InterruptedException {
        super.stop();
        this.onStop.run();
    }

    @Override
    public void onMessage(WebSocket webSocket, String s) {
        System.out.println(s); //TODO: clean
    }

    @Override
    public void onError(WebSocket webSocket, Exception e) {
        this.exceptionHandler.accept(e);
    }

    @Override
    public void onClosing(WebSocket conn, int code, String reason, boolean remote) {
        super.onClosing(conn, code, reason, remote);
        this.onClose.onClose(code, reason);
    }

    @Override
    public void onStart() {
        this.isRunning = true;
        this.onStart.run();
    }

    @Override
    public boolean isRunning() {
        return this.isRunning;
    }
}
