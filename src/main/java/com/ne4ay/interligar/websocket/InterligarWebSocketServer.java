package com.ne4ay.interligar.websocket;

import com.ne4ay.interligar.Channel;
import com.ne4ay.interligar.messages.Message;
import com.ne4ay.interligar.messages.MessageData;
import com.ne4ay.interligar.messages.MessageDataListener;
import com.ne4ay.interligar.messages.MessageType;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static com.ne4ay.interligar.messages.MessagesUtils.serialize;

public class InterligarWebSocketServer implements Channel {

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

    @Override
    public void sendMessage(Message<?> message) {
        serialize(message, this.exceptionHandler)
            .ifPresent(this.server::broadcast);
    }

    public <T extends MessageData> InterligarWebSocketServer addMessageListener(MessageType<T> messageType, MessageDataListener<T> messageDataListener) {
        this.server.addMessageListener(messageType, messageDataListener);
        return this;
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
