package com.csefinalproject.github.multiplayer.networking;

import com.csefinalproject.github.multiplayer.networking.exceptions.ConnectionFailedException;
import com.csefinalproject.github.multiplayer.networking.exceptions.PacketDecodeError;
import com.csefinalproject.github.multiplayer.networking.packet.ConnectionPacket;
import com.csefinalproject.github.multiplayer.networking.packet.ConnectionSuccessfulPacket;
import com.csefinalproject.github.multiplayer.networking.packet.Packet;
import com.csefinalproject.github.multiplayer.util.Ticker;

import java.net.*;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

public class Client implements IPeer{
    InetAddress address;
    String ip;
    short port;
    boolean isConnected;
    DatagramSocket socket;
    Ticker clientThread;


    public void connect(String ip, short port, String username) throws ConnectionFailedException, UnknownHostException {
        if (isConnected) {
            throw new IllegalStateException("Please disconnect the client before attempting to connect again");
        }

        address = InetAddress.getByName(ip);
        this.ip = ip;
        this.port = port;

        try {
            socket = new DatagramSocket();
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }

        tryToConnect(DEFAULT_CONNECTION_TIMEOUT,username);

        clientThread = new Ticker(IPeer.DEFAULT_TPS);
        clientThread.subscribe(this::clientTick);
        clientThread.start();

    }

    private void tryToConnect(int timeout,String username) throws ConnectionFailedException {
        sendPacket(new ConnectionPacket(ip,(short) socket.getPort(),username));
        AtomicBoolean successful = new AtomicBoolean(false);
        Thread waitingThread = new Thread(()->{
            DatagramPacket receivedPacket = receivePacket(socket);
            try {
                Packet packet = decodePacket(receivedPacket);
                successful.set(packet instanceof ConnectionSuccessfulPacket);
            } catch (PacketDecodeError e) {
                successful.set(false);
            }
        });
        waitingThread.start();
        long startTime = System.currentTimeMillis();
        while (!successful.get()) {
            long elapsedTime = System.currentTimeMillis() - startTime;
            if (elapsedTime >= timeout * 1000L) {
                waitingThread.interrupt();
                throw new ConnectionFailedException();
            }
        }
    }

    public void disconnect() {

    }

    public void sendPacket(Packet packet) {

    }

    public double lastKeepAlivePacketTime() {
        return 0;
    }

    public Packet getNextPacket() {
        return null;
    }
    public boolean hasNextPacket() {
        return false;
    }


    private void clientTick() {

    }




}



