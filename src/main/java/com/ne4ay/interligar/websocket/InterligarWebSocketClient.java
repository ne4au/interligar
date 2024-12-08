package com.ne4ay.interligar.websocket;

import com.ne4ay.interligar.Address;
import com.ne4ay.interligar.Channel;
import com.ne4ay.interligar.messages.Message;
import com.ne4ay.interligar.messages.MessageData;
import com.ne4ay.interligar.messages.MessageDataListener;
import com.ne4ay.interligar.messages.MessageType;
import org.java_websocket.handshake.ServerHandshake;

import java.util.function.Consumer;

import static com.ne4ay.interligar.messages.MessagesUtils.serialize;

public class InterligarWebSocketClient implements Channel {

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

    @Override
    public void sendMessage(Message<?> message) {
        serialize(message, this.exceptionHandler)
            .ifPresent(this.client::send);
    }

    public <T extends MessageData> InterligarWebSocketClient addMessageListener(MessageType<T> messageType, MessageDataListener<T> messageDataListener) {
        this.client.addMessageListener(messageType, messageDataListener);
        return this;
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
