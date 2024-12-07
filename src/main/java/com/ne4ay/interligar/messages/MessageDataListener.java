package com.ne4ay.interligar.messages;

import org.java_websocket.WebSocket;

public interface MessageDataListener<T extends MessageData> {
    void handleMessage(WebSocket webSocket, T messageData);
}
