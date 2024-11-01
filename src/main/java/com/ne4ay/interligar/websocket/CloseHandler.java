package com.ne4ay.interligar.websocket;

public interface CloseHandler {
    void onClose(int code, String reason);
}
