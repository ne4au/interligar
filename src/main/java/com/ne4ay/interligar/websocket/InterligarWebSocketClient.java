package com.ne4ay.interligar.websocket;

import com.ne4ay.interligar.Address;
import com.ne4ay.interligar.Lifecycle;
import com.ne4ay.interligar.messages.Message;
import org.java_websocket.handshake.ServerHandshake;

import java.util.function.Consumer;

import static com.ne4ay.interligar.messages.MessagesUtils.serialize;

public class InterligarWebSocketClient implements Lifecycle {

    private final SocketClient client;
    private final Consumer<Exception> exceptionHandler;

    public InterligarWebSocketClient(Address address,
        Runnable onStart,
        Consumer<ServerHandshake> onConnected,
        CloseHandler onClose,
        Runnable onStop,
        Consumer<Exception> exceptionHandler)
    {
        this.client = new SocketClient(address, onStart, onConnected, onClose, onStop, exceptionHandler);
        this.exceptionHandler = exceptionHandler;
    }

    public void sendMessage(Message<?> message) {
        serialize(message, this.exceptionHandler)
            .ifPresent(this.client::send);
    }

    @Override
    public void start() {
        this.client.start();
    }

    @Override
    public void stop()  {
        if (!isRunning()) {
            return;
        }
        try {
            this.client.stop();
        } catch (InterruptedException e) {
            this.exceptionHandler.accept(e);
        }
    }

    @Override
    public boolean isRunning() {
        return this.client.isRunning();
    }
}
