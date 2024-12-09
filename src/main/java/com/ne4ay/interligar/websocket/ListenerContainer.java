package com.ne4ay.interligar.websocket;

import com.ne4ay.interligar.messages.Message;
import com.ne4ay.interligar.messages.MessageData;
import com.ne4ay.interligar.messages.MessageDataListener;
import com.ne4ay.interligar.messages.MessageType;
import org.java_websocket.WebSocket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

public class ListenerContainer {
    private final Map<MessageType<?>, MessageListeners<?>> messageListeners = new HashMap<>();

    public <T extends MessageData> ListenerContainer addMessageListener(MessageType<T> messageType, MessageDataListener<T> messageListener) {
        @SuppressWarnings("unchecked") MessageListeners<T> listeners = (MessageListeners<T>) this.messageListeners.computeIfAbsent(messageType, __ -> MessageListeners.empty(messageType.getMessageDataClass()));
        listeners.add(messageListener);
        return this;
    }

    public <T extends MessageData> ListenerContainer removeMessageListener(MessageType<T> messageType, MessageDataListener<T> messageListener) {
        Optional.ofNullable(this.messageListeners.get(messageType))
            .ifPresent(listeners -> listeners.remove(messageListener));
        return this;
    }

    public <T extends MessageData> void handleMessage(WebSocket webSocket, Message<T> message, Consumer<Exception> exceptionHandler) {
        MessageType<T> messageType = message.messageType();
        T messageData = message.messageData();
        Optional.of(this.messageListeners)
            .map(listeners -> listeners.get(messageType))
            .ifPresent(listeners -> {
                if (messageType.getMessageDataClass().isAssignableFrom(listeners.messageDataType())) {
                    //noinspection unchecked
                    triggerListeners((MessageListeners<T>) listeners, webSocket, messageData);
                    return;
                }
                exceptionHandler.accept(new IllegalStateException(
                    "Unable to process message: " + message
                        + ". Expected listener type: " + messageType
                        + ". Actual type:" + listeners.messageDataType()));
            });
    }


    private <T extends MessageData> void triggerListeners(MessageListeners<T> listeners, WebSocket webSocket, T messageData) {
        listeners
            .messageDataListeners()
            .forEach(listener -> handleMessage(listener, webSocket, messageData));
    }

    private <T extends MessageData> void handleMessage(MessageDataListener<T> listener, WebSocket webSocket, T messageData) {
        listener.handleMessage(webSocket, messageData);
    }

    private record MessageListeners<T extends MessageData>(Class<T> messageDataType, List<MessageDataListener<T>> messageDataListeners) {
        public static <T extends MessageData> MessageListeners<T> empty(Class<T> messageDataType) {
            return new MessageListeners<>(messageDataType, new ArrayList<>());
        }

        public void add(MessageDataListener<T> listener) {
            this.messageDataListeners.add(listener);
        }

        public void remove(MessageDataListener<?> listener) {
            this.messageDataListeners.remove(listener);
        }

    }
}
