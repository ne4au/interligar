package com.ne4ay.interligar.presenter;

import com.ne4ay.interligar.Address;
import com.ne4ay.interligar.InterligarApplication;
import com.ne4ay.interligar.udp.ChannelState;
import com.ne4ay.interligar.udp.UDPClient;
import com.ne4ay.interligar.udp.UDPServer;
import com.ne4ay.interligar.view.ClientConfView;
import javafx.application.Platform;

import javax.annotation.Nonnull;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static com.ne4ay.interligar.utils.InterligarUtils.wrapInPlatformCall;

public class ClientConfPresenter {

    private final ClientConfView view;
    private final ChannelState channelState;


    public ClientConfPresenter(@Nonnull ClientConfView view, @Nonnull ChannelState channelState) {
        this.view = view;
        this.channelState = channelState;
        init();
    }

    private void init() {
        this.view.setStartClientButtonListener(this::onStartClientButtonClick);
    }

    private ClientConfView setClientInfoText(@Nonnull String text) {
        return this.view.setClientInfoText(text);
    }

    private void onStartClientButtonClick() {
        String addressString = this.view.getAddressString();
        String portString = this.view.getPortString();
        Address address = Address.fromAddress(addressString, portString);
        setClientInfoText("Starting the client...");
        channelState.openChannel(
            () -> createUdpClient(address),
            this::runClient);
    }

    @Nonnull
    private Optional<UDPClient> createUdpClient(@Nonnull Address address) {
        try {
            return Optional.of(new UDPClient(
                address,
                wrapInPlatformCall(() ->
                    setClientInfoText("Client has started!")
                        .setStartClientButtonListener(this::shutDownServer)
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

    private CompletableFuture<Void> runClient(@Nonnull Runnable client) {
        return CompletableFuture.runAsync(client, InterligarApplication.EXECUTOR)
            .exceptionally(ex -> {
                Platform.runLater(() ->
                    setClientInfoText("Client error: " + ex.getMessage())
                );
                return null;
            });
    }

    private void shutDownServer() {
        channelState.shutdownChannel();
    }
}
