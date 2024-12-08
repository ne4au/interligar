package com.ne4ay.interligar.presenter;

import com.ne4ay.interligar.Address;
import com.ne4ay.interligar.InterligarApplication;
import com.ne4ay.interligar.capture.ScreenCapturer;
import com.ne4ay.interligar.messages.Message;
import com.ne4ay.interligar.messages.MessageType;
import com.ne4ay.interligar.messages.data.ChangeDestinationModeRequestMessageData;
import com.ne4ay.interligar.messages.data.ChangeDestinationModeResponseMessageData;
import com.ne4ay.interligar.udp.UDPClient;
import com.ne4ay.interligar.view.ClientConfView;
import com.ne4ay.interligar.websocket.InterligarWebSocketClient;
import javafx.application.Platform;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ServerHandshake;

import java.net.SocketException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static com.ne4ay.interligar.utils.InterligarUtils.wrapInPlatformCall;

public class ClientConfPresenter {

    private final ClientConfView view;
    private final ScreenCapturer screenCapturer;
    private volatile InterligarWebSocketClient client;

    public ClientConfPresenter(ClientConfView view, ScreenCapturer screenCapturer) {
        this.view = view;
        this.screenCapturer = screenCapturer;
        init();
    }

    private void init() {
        this.view.setStartClientButtonListener(this::onStartClientButtonClick);
    }

    private ClientConfView setClientInfoText(String text) {
        return this.view.setClientInfoText(text);
    }

    private void onStartClientButtonClick() {
        String addressString = this.view.getAddressString();
        String portString = this.view.getPortString();
        Address address = Address.fromAddress(addressString, portString);
        setClientInfoText("Starting the client...");
        startClient(address);
    }

    private void startClient(Address address) {
        if (this.client != null) {
            this.client.stop();
        }
        this.client = createClient(address);
        this.client.addMessageListener(MessageType.CHANGE_DESTINATION_REQUEST, this::handleChangeDestinationRequest);
        this.client.start();
    }

    private void handleChangeDestinationRequest(WebSocket ws, ChangeDestinationModeRequestMessageData messageData) {

    }

    private InterligarWebSocketClient createClient(Address address) {
        return new InterligarWebSocketClient(address,
            wrapInPlatformCall(() -> {
                setClientInfoText("Client has started!")
                    .setStartClientButtonListener(this::shutDownClient)
                    .setStartClientButtonText("Shutdown client");
                try {
                    this.screenCapturer.start();
                } catch (InterruptedException e) {
                    onClientException(e);
                }
            }),
            this::onConnected,
            this::onClose,
            wrapInPlatformCall(() -> {
                setClientInfoText("Client is not running !")
                    .setStartClientButtonListener(this::onStartClientButtonClick)
                    .setStartClientButtonText("Start client");
                try {
                    this.screenCapturer.stop();
                } catch (InterruptedException e) {
                    onClientException(e);
                }
            }),
            this::onClientException
        );
    }

    private void onConnected(ServerHandshake handshake) {
        Platform.runLater(() -> {
            setClientInfoText("The client has been connected to the server: " + handshake.getHttpStatusMessage());
            this.client.sendMessage(Message.createTestMessage("lol"));
        });
    }

    private void onClose(int code, String reason) {
        Platform.runLater(() -> {
            setClientInfoText("Client has been closed. " + code + "|Close reason: " + reason);
        });
    }

    private void onClientException(Exception e) {
        Platform.runLater(() -> {
            System.out.println(e); //TODO: clean
            setClientInfoText("Error! " + e.getMessage());
        });
    }

    private Optional<UDPClient> createUdpClient(Address address) {
        try {
            return Optional.of(new UDPClient(
                address,
                wrapInPlatformCall(() ->
                    setClientInfoText("Client has started!")
                        .setStartClientButtonListener(this::shutDownClient)
                        .setStartClientButtonText("Shutdown client")),
                wrapInPlatformCall(() ->
                    setClientInfoText("Connected to the server!")
                ),
                wrapInPlatformCall(() ->
                    setClientInfoText("Client is not running!")
                        .setStartClientButtonListener(this::onStartClientButtonClick)
                        .setStartClientButtonText("Start client")
                )));
        } catch (SocketException e) {
            setClientInfoText("Unable to start server! Error:" + e);
            return Optional.empty();
        }
    }

    private void shutDownClient() {
        this.client.stop();
    }
}
