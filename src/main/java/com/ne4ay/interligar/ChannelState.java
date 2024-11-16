package com.ne4ay.interligar;

import com.ne4ay.interligar.messages.Message;

import java.util.Optional;
import java.util.function.Supplier;

public class ChannelState {

    private final Object channelLock = new Object();
    private Channel channel;
    private boolean isOccupied = false;

    public void openChannel(Supplier<Optional<? extends Channel>> channelCreator) throws InterruptedException {
        synchronized (this.channelLock) {
            if (this.isOccupied) {
                shutdownChannel();
            }
        }
    }

    public void sendMessage(Message<?> message) {
        synchronized (this.channelLock) {
            if (this.channel == null) {
                return;
            }
            this.channel.sendMessage(message);
        }
    }

    public void shutdownChannel() throws InterruptedException {
        synchronized (this.channelLock) {
            if (this.channel != null && this.isOccupied) {
                this.channel.stop();
                this.isOccupied = false;
                this.channel = null;
            }
        }
    }

}
