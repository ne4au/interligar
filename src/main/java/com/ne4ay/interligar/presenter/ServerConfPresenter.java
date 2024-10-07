package com.ne4ay.interligar.presenter;

import com.ne4ay.interligar.InterligarApplication;
import com.ne4ay.interligar.udp.ChannelState;
import com.ne4ay.interligar.udp.UDPServer;
import com.ne4ay.interligar.view.ServerConfView;
import javafx.application.Platform;

import javax.annotation.Nonnull;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static com.ne4ay.interligar.utils.InterligarUtils.wrapInPlatformCall;

public class ServerConfPresenter {

    private final ServerConfView view;
    private final ChannelState channelState;

    public ServerConfPresenter(@Nonnull ServerConfView view, @Nonnull ChannelState channelState) {
        this.view = view;
        this.channelState = channelState;
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

    private ServerConfView setServerInfoText(@Nonnull String text) {
        return this.view.setServerInfoText(text);
    }

    private void onStartServerButtonClick() {
        String portString = this.view.getPortString();
        if (portString.isBlank()) {
            //validation
            return;
        }
        setServerInfoText("Starting the server...");
        channelState.openChannel(
            () -> createUdpServer(portString),
            this::runServer);
    }

    private CompletableFuture<Void> runServer(@Nonnull Runnable server) {
        return CompletableFuture.runAsync(server, InterligarApplication.EXECUTOR)
            .exceptionally(ex -> {
                Platform.runLater(() ->
                    setServerInfoText("Server error: " + ex.getMessage())
                );
                return null;
            });
    }

    private Optional<UDPServer> createUdpServer(@Nonnull String portString) {
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
        channelState.shutdownChannel();
    }
}
