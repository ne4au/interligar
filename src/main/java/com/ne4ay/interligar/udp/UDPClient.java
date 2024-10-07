package com.ne4ay.interligar.udp;

import com.ne4ay.interligar.Address;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class UDPClient implements Channel {
    private final DatagramSocket socket;
    private final Address address;
    private final Runnable onStart;
    private final Runnable onConnect;
    private final Runnable onClose;

    private final byte[] buf = new byte[256];

    private volatile boolean isRunning = false;
    private volatile boolean isConnected = false;

    public UDPClient(
        @Nonnull Address address,
        @Nonnull Runnable onStart,
        @Nonnull Runnable onConnect,
        @Nonnull Runnable onClose) throws SocketException
    {
        this.socket = new DatagramSocket();
        this.address = address;
        this.onStart = onStart;
        this.onConnect = onConnect;
        this.onClose = onClose;
    }

    @Override
    public void run() {
        this.isRunning = true;
        this.onStart.run();
        ByteBuffer byteBuffer = ByteBuffer.allocate(64);
        byteBuffer.put("Lol".getBytes(StandardCharsets.US_ASCII));
        byte[] arr = new byte[byteBuffer.remaining()];
        byteBuffer.get(arr);
        while (this.isRunning) {
            DatagramPacket packet = createPacket(arr);
            try {
                socket.send(packet);
            } catch (IOException e) {
                if (this.isRunning) {
                    socket.close();
                    onClose.run();
                    throw new RuntimeException("Socket closed unexpectedly", e);
                }
                socket.close();
                break;
            }
            packet = new DatagramPacket(buf, buf.length);
            try {
                socket.receive(packet);
            } catch (IOException e) {
                if (this.isRunning) {
                    socket.close();
                    onClose.run();
                    throw new RuntimeException(e);
                }
                socket.close();
                break;
            }
            this.onConnect.run();
            String received = new String(packet.getData(), StandardCharsets.US_ASCII);
            System.out.println(received); //TODO: clean
        }
    }

    private DatagramPacket createPacket(byte[] arr) {
        return new DatagramPacket(arr, arr.length, address.inetAddress(), address.port());
    }

    @Override
    public void close() {
        this.isRunning = false;
        socket.close();
        Thread.currentThread().interrupt();
        onClose.run();
    }
}
