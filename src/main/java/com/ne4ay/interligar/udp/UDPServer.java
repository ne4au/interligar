package com.ne4ay.interligar.udp;

import java.io.Closeable;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class UDPServer implements  Runnable, Closeable {

    private final DatagramSocket socket;
    private final Runnable onStart;
    private final UdpConnectListener onClientConnected;
    private final Runnable onClose;

    private volatile boolean isRunning = false;
    private volatile boolean isConnected = false;
    private final byte[] buf = new byte[256];

    public UDPServer(int port,
        Runnable onStart,
        UdpConnectListener onClientConnected,
        Runnable onClose) throws SocketException
    {
        this.socket = new DatagramSocket(port);
        this.onStart = onStart;
        this.onClientConnected = onClientConnected;
        this.onClose = onClose;
    }

    @Override
    public void run() {
        this.isRunning = true;
        this.onStart.run();
        while (this.isRunning) {
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            try {
                socket.receive(packet);
            } catch (SocketException e) {
                if (this.isRunning) {
                    socket.close();
                    onClose.run();
                    throw new RuntimeException("Socket closed unexpectedly", e);
                }
                socket.close();
                break;
            } catch (IOException e) {
                if (this.isRunning) {
                    socket.close();
                    onClose.run();
                    throw new RuntimeException(e);
                }
                socket.close();
                break;
            }
            InetAddress address = packet.getAddress();
            int port = packet.getPort();
            if (!this.isConnected) {
                this.isConnected = true;
                this.onClientConnected.onClientConnected(address, port);
            }
            DatagramPacket directedPacket = new DatagramPacket(buf, buf.length, address, port);
            String received = new String(directedPacket.getData(),  0, directedPacket.getLength());
            System.out.println(received); //TODO: clean
        }
        socket.close();
    }


    @Override
    public void close() {
        this.isRunning = false;
        socket.close();
        Thread.currentThread().interrupt();
        onClose.run();
    }
}
