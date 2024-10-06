package com.ne4ay.interligar.presenter;

import com.ne4ay.interligar.InterligarApplication;
import com.ne4ay.interligar.udp.UDPServer;
import com.ne4ay.interligar.view.ServerConfView;
import javafx.application.Platform;

import javax.annotation.Nonnull;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.concurrent.CompletableFuture;

import static com.ne4ay.interligar.utils.InterligarUtils.wrapInPlatformCall;

public class ServerConfPresenter {

    private final ServerConfView view;

    private volatile UDPServer udpServer;
    private volatile CompletableFuture<Void> udpChannelFuture = CompletableFuture.completedFuture(null);

    public ServerConfPresenter(@Nonnull ServerConfView view) {
        this.view = view;
        init();
    }

    private void init() {
        try {
            view.setIpText("Address:    " + InetAddress.getLocalHost().getHostAddress() + ":");
        } catch (Exception e) {
            setServerInfoText("Unable to determine local address.");
        }
        this.view.setStartServerButtonListener(this::onStartServerButtonClick);
    }

    private void setServerInfoText(@Nonnull String text) {
        this.view.setServerInfoText(text);
    }

    private void onStartServerButtonClick() {
        String portString = this.view.getPortString();
        if (portString.isBlank()) {
            //validation
            return;
        }
        try {
            setServerInfoText("Starting the server...");
                this.udpServer = new UDPServer(
                    Integer.parseInt(portString),
                    wrapInPlatformCall(() -> {
                        setServerInfoText("Server has started!");
                        this.view.setStartServerButtonListener(this::shutDownServer);
                        this.view.setStartServerButtonText("Shutdown server");
                    }),
                    (address) -> Platform.runLater(() -> {
                        setServerInfoText("Client " + address.getRepresentation() + " has been connected.");
                    }),
                    wrapInPlatformCall(() -> {
                        this.view.setStartServerButtonListener(this::onStartServerButtonClick);
                        this.view.setStartServerButtonText("Start server");
                        this.view.setServerInfoText("Server is not running");
                    }));
                this.udpChannelFuture = CompletableFuture.runAsync(this.udpServer, InterligarApplication.EXECUTOR)
                    .exceptionally(ex -> {
                        setServerInfoText("Server error: " + ex.getMessage());
                        return null;
                    });
        } catch (SocketException e) {
            setServerInfoText("Unable to start server! Error:" + e);
        }
    }

    private void shutDownServer() {
        if (this.udpServer != null) {
            this.udpServer.close();
            this.udpChannelFuture.join();
        }
    }
}
