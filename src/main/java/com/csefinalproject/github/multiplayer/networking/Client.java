package com.csefinalproject.github.multiplayer.networking;

import com.csefinalproject.github.multiplayer.networking.exceptions.ConnectionFailedException;
import com.csefinalproject.github.multiplayer.networking.packet.ConnectionPacket;
import com.csefinalproject.github.multiplayer.networking.packet.ConnectionSuccessfulPacket;
import com.csefinalproject.github.multiplayer.networking.packet.Packet;
import com.csefinalproject.github.multiplayer.util.Ticker;

import java.net.*;

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

        tryToConnect(username);

        clientThread = new Ticker(IPeer.DEFAULT_TPS);
        clientThread.subscribe(this::clientTick);
        clientThread.start();

    }

    private void tryToConnect(String username) throws ConnectionFailedException {
        final Packet connectionPacket = new ConnectionPacket(ip, (short) socket.getPort(),username);
        // Send a packet to the server saying that we want to connect
        this.sendPacket(connectionPacket);
        // This will throw an exception if we don't get a response or the response is not the right packet type
        waitForAck(socket,ConnectionSuccessfulPacket.class);
        isConnected = true;
    }

    private void clientTick() {

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







}



