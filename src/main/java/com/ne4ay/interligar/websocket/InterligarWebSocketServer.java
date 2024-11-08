package com.ne4ay.interligar.websocket;

import com.ne4ay.interligar.Lifecycle;
import com.ne4ay.interligar.messages.Message;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static com.ne4ay.interligar.messages.MessagesUtils.serialize;

public class InterligarWebSocketServer implements Lifecycle {

    private final SocketServer server;
    private final Consumer<Exception> exceptionHandler;

    public InterligarWebSocketServer(int port,
        Runnable onStart,
        BiConsumer<WebSocket, ClientHandshake> onClientConnected,
        CloseHandler onClose,
        Runnable onStop,
        Consumer<Exception> exceptionHandler)
    {
        this.server = new SocketServer(port, onStart, onClientConnected, onClose, onStop, exceptionHandler);
        this.exceptionHandler = exceptionHandler;
    }

    public void sendMessage(Message<?> message) {
        serialize(message, this.exceptionHandler)
            .ifPresent(this.server::broadcast);
    }


    public void start() {
        this.server.start();
    }

    public void stop() {
        if (!isRunning()) {
            return;
        }
        try {
            this.server.stop();
        } catch (InterruptedException e) {
            this.exceptionHandler.accept(e);
        }
    }

    public boolean isRunning() {
        return this.server.isRunning();
    }

}
