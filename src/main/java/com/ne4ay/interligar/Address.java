package com.ne4ay.interligar;

import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;

public record Address(InetAddress inetAddress, int port) {

    public URI toURI() {
        return URI.create("ws://" + getRepresentation());
    }

    public String getRepresentation() {
        return addressToString(inetAddress().getAddress()) + ":" + port();
    }


    public static Address fromAddress(String host, String port) {
        try {
            return new Address(
                InetAddress.getByName(host),
                Integer.parseInt(port));
        } catch (UnknownHostException e) {
            throw new IllegalStateException("Unable to parse address: " + host + ":" + port + ". Error: " + e);
        }
    }


    private static String addressToString(byte[] bytes) {
        return String.format("%d.%d.%d.%d",
            bytes[0] & 0xFF,
            bytes[1] & 0xFF,
            bytes[2] & 0xFF,
            bytes[3] & 0xFF);
    }
}
