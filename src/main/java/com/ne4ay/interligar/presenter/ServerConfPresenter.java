package com.ne4ay.interligar.presenter;

import com.ne4ay.interligar.capture.ScreenCapturer;
import com.ne4ay.interligar.messages.Message;
import com.ne4ay.interligar.udp.UDPServer;
import com.ne4ay.interligar.view.ServerConfView;
import com.ne4ay.interligar.websocket.InterligarWebSocketServer;
import javafx.application.Platform;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;

import java.awt.AWTException;
import java.net.Inet4Address;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.ne4ay.interligar.utils.InterligarUtils.wrapInPlatformCall;

public class ServerConfPresenter {

    private final ServerConfView view;
    private final ScreenCapturer screenCapturer;
    private volatile InterligarWebSocketServer server;

    public ServerConfPresenter(ServerConfView view, ScreenCapturer screenCapturer) {
        this.view = view;
        this.screenCapturer = screenCapturer;
        init();
    }

    private void init() {
        try {
            view.setIpText("Address:    " + getLocalAddress() + ":");
        } catch (Exception e) {
            setServerInfoText("Unable to determine local address.");
        }
        this.view.setStartServerButtonListener(this::onStartServerButtonClick);
    }

    private String getLocalAddress() throws SocketException, AWTException {

        return Collections.list(NetworkInterface.getNetworkInterfaces())
            .stream()
            .map(NetworkInterface::getInetAddresses)
            .map(Collections::list)
            .flatMap(List::stream)
            .filter(ip -> ip instanceof Inet4Address && ip.isSiteLocalAddress())
            .map(ip -> (Inet4Address) ip)
            .filter(ip -> ip.getHostAddress().startsWith("192"))
//            .sorted()
            .findFirst()
            .orElseThrow(RuntimeException::new)
            .getHostAddress();
    }

    private ServerConfView setServerInfoText(String text) {
        return this.view.setServerInfoText(text);
    }

    private void onStartServerButtonClick() {
        String portString = this.view.getPortString();
        if (portString.isBlank()) {
            //validation
            return;
        }
        int port;
        try {
            port = Integer.parseInt(portString);
        } catch (Exception e) {
            setServerInfoText("Unable to start server! Error:" + e);
            return;
        }
        setServerInfoText("Starting the server...");
        startServer(port);
    }

    private void startServer(int port) {
        if (this.server != null) {
            this.server.stop();
        }
        this.server = new InterligarWebSocketServer(port,
            wrapInPlatformCall(() ->
                setServerInfoText("Server has started!")
                    .setStartServerButtonListener(this::shutDownServer)
                    .setStartServerButtonText("Shutdown server")),
            this::onClientConnected,
            this::onClose,
            wrapInPlatformCall(() ->
                setServerInfoText("Server is not running!")
                    .setStartServerButtonListener(this::onStartServerButtonClick)
                    .setStartServerButtonText("Start server")),
            this::onServerException);
        this.server.start();
    }

    private void onServerException(Exception e) {
        Platform.runLater(() -> {
            setServerInfoText("Error! " + e.getMessage());
        });
    }

    private void onClose(int code, String reason) {
        Platform.runLater(() -> {
            setServerInfoText("Client has been disconnected. " + code + "|Close reason: " + reason);
        });
    }

    private void onClientConnected(WebSocket client, ClientHandshake handshake) {
        Platform.runLater(() -> {
            setServerInfoText("Client has been connected: " + client.getRemoteSocketAddress());
            this.server.sendMessage(Message.createTestMessage("lol"));
        });
        try {
            this.screenCapturer.start();
        } catch (InterruptedException e) {
            onServerException(e);
        }
    }

    private Optional<UDPServer> createUdpServer(String portString) {
        try {
            return Optional.of(new UDPServer(
                Integer.parseInt(portString),
                wrapInPlatformCall(() ->
                    setServerInfoText("Server has started!")
                        .setStartServerButtonListener(this::shutDownServer)
                        .setStartServerButtonText("Shutdown server")),
                (address) -> Platform.runLater(() -> {
                    setServerInfoText("Client " + address.getRepresentation() + " has been connected.");
                }),
                wrapInPlatformCall(() ->
                    setServerInfoText("Server is not running")
                        .setStartServerButtonListener(this::onStartServerButtonClick)
                        .setStartServerButtonText("Start server"))
            ));
        } catch (SocketException e) {
            setServerInfoText("Unable to start server! Error:" + e);
            return Optional.empty();
        }
    }

    private void shutDownServer() {
        Optional.ofNullable(this.server)
            .ifPresent(InterligarWebSocketServer::stop);
    }
}
