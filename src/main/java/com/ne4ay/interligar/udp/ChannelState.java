package com.ne4ay.interligar.udp;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Supplier;

public class ChannelState {

    private final Object channelLock = new Object();
    private Channel channel;
    private CompletableFuture<Void> udpChannelFuture;
    private boolean isOccupied = false;

    public void openChannel(
        @Nonnull Supplier<Optional<? extends Channel>> channelCreator,
        @Nonnull Function<Channel, CompletableFuture<Void>> futureCreator)
    {
        synchronized (this.channelLock) {
            if (this.isOccupied) {
                shutdownChannel();
            }
            channelCreator.get().ifPresent(createdChannel -> {
                this.channel = createdChannel;
                this.udpChannelFuture = futureCreator.apply(createdChannel);
                this.isOccupied = true;
            });
        }
    }

    public void shutdownChannel() {
        synchronized (this.channelLock) {
            if (this.channel != null && this.isOccupied) {
                this.channel.close();
                this.udpChannelFuture.join();
                this.isOccupied = false;
                this.channel = null;
                this.udpChannelFuture = null;
            }
        }
    }

}
