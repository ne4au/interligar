package com.ne4ay.interligar.udp;

import com.ne4ay.interligar.Address;

import javax.annotation.Nonnull;
import java.net.InetAddress;

public interface UdpConnectListener {

    void onClientConnected(@Nonnull Address address);

    default void onClientConnected(@Nonnull InetAddress address, int port) {
        onClientConnected(new Address(address, port));
    }
}
