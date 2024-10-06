package com.ne4ay.interligar.udp;

import com.ne4ay.interligar.Address;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class UDPClient implements Runnable {
    private final DatagramSocket socket;
    private final Address address;
    private final Runnable onClose;

    private final byte[] buf = new byte[256];

    private volatile boolean isRunning = false;
    private volatile boolean isConnected = false;

    public UDPClient(
        @Nonnull Address address,
        @Nonnull Runnable onConnect,
        @Nonnull Runnable onClose) throws SocketException
    {
        this.socket = new DatagramSocket();
        this.address = address;
        this.onClose = onClose;
    }


    @Override
    public void run() {
        this.isRunning = true;
        ByteBuffer byteBuffer = ByteBuffer.allocate(64);
        byteBuffer.put("Lol".getBytes(StandardCharsets.US_ASCII));
        byte[] arr = new byte[byteBuffer.remaining()];
        byteBuffer.get(arr);
        while (this.isRunning) {
            DatagramPacket packet = createPacket(arr);
            try {
                socket.send(packet);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            packet = new DatagramPacket(buf, buf.length);
            try {
                socket.receive(packet);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            String received = new String(packet.getData(), StandardCharsets.US_ASCII);
            System.out.println(received); //TODO: clean
        }
    }

    private DatagramPacket createPacket(byte[] arr) {
        return new DatagramPacket(arr, arr.length, address.inetAddress(), address.port());
    }
}
