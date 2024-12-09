package com.ne4ay.interligar.udp;

import com.ne4ay.interligar.Address;

import java.net.InetAddress;

public interface UdpConnectListener {
    UdpConnectListener EMPTY_LISTENER = __ -> {};

    void onClientConnected(Address address);

    default void onClientConnected(InetAddress address, int port) {
        onClientConnected(new Address(address, port));
    }
}
