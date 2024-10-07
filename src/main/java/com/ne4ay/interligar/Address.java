package com.ne4ay.interligar;

import javax.annotation.Nonnull;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

public record Address(@Nonnull InetAddress inetAddress, int port) {

    public String getRepresentation() {
        return addressToString(inetAddress().getAddress()) + ":" + port();
    }

    @Nonnull
    public static Address fromAddress(@Nonnull String host, @Nonnull String port) {
        try {
            return new Address(
                InetAddress.getByName(host),
                Integer.parseInt(port));
        } catch (UnknownHostException e) {
            throw new IllegalStateException("Unable to parse address: " + host + ":" + port + ". Error: " + e);
        }
    }


    @Nonnull
    private static String addressToString(@Nonnull byte[] bytes) {
        return String.format("%d.%d.%d.%d",
            bytes[0] & 0xFF,
            bytes[1] & 0xFF,
            bytes[2] & 0xFF,
            bytes[3] & 0xFF);
    }
}
