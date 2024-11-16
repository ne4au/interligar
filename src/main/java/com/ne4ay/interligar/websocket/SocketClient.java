package com.ne4ay.interligar.websocket;

import com.ne4ay.interligar.Address;
import com.ne4ay.interligar.Channel;
import com.ne4ay.interligar.messages.Message;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.util.function.Consumer;

import static com.ne4ay.interligar.messages.MessagesUtils.serialize;

public class SocketClient extends WebSocketClient implements Channel {

    private final Runnable onStart;
    private final Consumer<ServerHandshake> onConnected;
    private final CloseHandler onClose;
    private final Runnable onStop;
    private final Consumer<Exception> exceptionHandler;

    private volatile boolean isRunning = false;
    private volatile boolean isConnected = false;

    public SocketClient(Address address,
        Runnable onStart,
        Consumer<ServerHandshake> onConnected,
        CloseHandler onClose,
        Runnable onStop,
        Consumer<Exception> exceptionHandler)
    {
        super(address.toURI());
        this.onStart = onStart;
        this.onConnected = onConnected;
        this.onClose = onClose;
        this.onStop = onStop;
        this.exceptionHandler = exceptionHandler;
    }

    @Override
    public void sendMessage(Message<?> message) {
        serialize(message, this.exceptionHandler)
            .ifPresent(this::send);
    }


    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        this.isConnected = true;
        this.onConnected.accept(serverHandshake);
    }

    @Override
    public void onMessage(String s) {
        System.out.println(s); //TODO: clean
    }

    @Override
    public void onClose(int i, String s, boolean b) {
        this.onClose.onClose(i, s);
    }

    @Override
    public void onError(Exception e) {
        this.exceptionHandler.accept(e);
    }

    @Override
    public void start() {
        this.isRunning = true;
        connect();
        this.onStart.run();
    }

    @Override
    public void stop() throws InterruptedException {
        this.isRunning = false;
        this.closeBlocking();
        this.onStop.run();
    }

    @Override
    public boolean isRunning() {
        return this.isRunning;
    }
}
