package com.csefinalproject.github.multiplayer.networking.server;

import com.csefinalproject.github.multiplayer.networking.IPeer;
import com.csefinalproject.github.multiplayer.networking.packet.Packet;
import com.csefinalproject.github.multiplayer.networking.server.ClientData;
import com.csefinalproject.github.multiplayer.util.Ticker;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Server implements IPeer {

    DatagramSocket socket;
    short ourPort;
    String ourIP;
    boolean running;
    Queue<Packet> packetsToBeProcessed = new ConcurrentLinkedQueue<>();
    Ticker serverThread;
    List<ClientData> connected = new ArrayList<>();
    public void start(short port) {
        if (running) {
            throw new IllegalStateException("Please disconnect the server before attempting to start it again");
        }
        setupIpAndPort(port);

        serverThread = new Ticker(IPeer.DEFAULT_TPS);
        Thread packetWatcher = new Thread(this::packetWatch);
        serverThread.subscribe(this::serverTick);
        serverThread.start();
        packetWatcher.start();
    }

    private void packetWatch() {
        while (running) {

        }
    }

    private void serverTick() {

    }

    private void setupIpAndPort(short port) {
        ourPort = port;
        try {
            socket = new DatagramSocket();
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
        try {
            ourIP = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            // We don't need to have the client handle something that is a very weird edge case
            throw new RuntimeException("We can't resolve the localhost. That is pretty weird",e);
        }
    }

    public void stop() {
        running = false;
        socket.close();
    }
    public void send(Packet packet, short client) {

    }
    public void broadcast(Packet packet) {

    }
    public ClientData[] getClientData() {
        return null;
    }
    public Packet getNextPacket() {
        return packetsToBeProcessed.poll();
    }
    public boolean hasNextPacket() {
        return !packetsToBeProcessed.isEmpty();
    }

    @Override
    public short getPort() {
        return ourPort;
    }

    @Override
    public String getIp() {
        return ourIP;
    }
}
