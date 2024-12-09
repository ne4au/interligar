package com.ne4ay.interligar.websocket;

import com.ne4ay.interligar.Address;
import com.ne4ay.interligar.Channel;
import com.ne4ay.interligar.messages.Message;
import com.ne4ay.interligar.messages.MessageData;
import com.ne4ay.interligar.messages.MessageDataListener;
import com.ne4ay.interligar.messages.MessageType;
import com.ne4ay.interligar.messages.MessagesUtils;
import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

import static com.ne4ay.interligar.messages.MessagesUtils.serialize;

public class SocketClient extends WebSocketClient implements Channel {

    private final Runnable onStart;
    private final Consumer<ServerHandshake> onConnected;
    private final CloseHandler onClose;
    private final Runnable onStop;
    private final Consumer<Exception> exceptionHandler;
    private final ListenerContainer listenerContainer = new ListenerContainer();

    private volatile boolean isRunning = false;
    private volatile boolean isConnected = false;

    private volatile WebSocket webSocket;

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

    public <T extends MessageData> void addMessageListener(MessageType<T> messageType, MessageDataListener<T> messageDataListener) {
        this.listenerContainer.addMessageListener(messageType, messageDataListener);
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        this.isConnected = true;
        this.onConnected.accept(serverHandshake);
        this.webSocket = getConnection();
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
    public void onMessage(ByteBuffer bytes) {
        super.onMessage(bytes);
        String messageText = StandardCharsets.UTF_8.decode(bytes).toString();
        MessagesUtils
            .parse(messageText, this.exceptionHandler)
            .ifPresentOrElse(
                message -> listenerContainer.handleMessage(webSocket, message, this.exceptionHandler),
                () -> System.out.println("Unable to parse " + messageText));
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
