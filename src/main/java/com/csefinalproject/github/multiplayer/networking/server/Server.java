package com.csefinalproject.github.multiplayer.networking.server;

import com.csefinalproject.github.multiplayer.networking.IPeer;
import com.csefinalproject.github.multiplayer.networking.exceptions.PacketDecodeError;
import com.csefinalproject.github.multiplayer.networking.packet.ConnectionPacket;
import com.csefinalproject.github.multiplayer.networking.packet.Packet;
import com.csefinalproject.github.multiplayer.networking.server.ClientData;
import com.csefinalproject.github.multiplayer.util.MessageUtils;
import com.csefinalproject.github.multiplayer.util.Ticker;
import org.jetbrains.annotations.NotNull;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Server implements IPeer {

    DatagramSocket socket;
    short ourPort;
    String ourIP;
    boolean running;
    Queue<Packet> packetsToBeProcessed = new ConcurrentLinkedQueue<>();
    Ticker serverThread;
    HashMap<Short,ClientData> connected = new HashMap<>();
    public void start(short port) {
        if (running) {
            throw new IllegalStateException("Please disconnect the server before attempting to start it again");
        }
        setupIpAndPort(port);


        running = true;
        serverThread = new Ticker(IPeer.DEFAULT_TPS);
        Thread packetWatcher = new Thread(this::packetWatch);
        serverThread.subscribe(this::serverTick);
        serverThread.start();
        packetWatcher.start();
    }

    private void packetWatch() {
        while (running) {
            try {
                Packet packet = MessageUtils.waitForPacket(socket);
                if (packet instanceof ConnectionPacket connectionPacket) {
                    ClientData newClient = new ClientData(connectionPacket.getIp(),connectionPacket.getPort(),connectionPacket.getUsername());
                    connected.put(newClient.getClientID(),newClient);
                    send(packet,newClient.getClientID());
                }
            } catch (PacketDecodeError e) {
                throw new RuntimeException(e);
            } catch (SocketException e) {
                // We are done
                running = false;
            }
        }
    }

    private void serverTick() {

    }

    private void setupIpAndPort(short port) {
        ourPort = port;
        try {
            socket = new DatagramSocket(port);
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
        sendMessageToClient(packet,connected.get(client));
    }
    public void broadcast(Packet packet) {
        for (ClientData data : connected.values()) {
            sendMessageToClient(packet,data);
        }
    }

    private void sendMessageToClient(Packet packet, @NotNull ClientData data) {
        try {
            MessageUtils.sendPacketTo(socket,MessageUtils.encodePacket(packet),InetAddress.getByName(data.getIP()), data.getPort());
        } catch (UnknownHostException e) {
            // This is impossible and will never happen unless someone does weird reflection
            throw new RuntimeException(e);
        }
    }

    public ClientData[] getClientData() {
        return connected.values().toArray(new ClientData[0]);
    }
    public Packet getNextPacket() {
        return packetsToBeProcessed.poll();
    }
    public boolean hasNextPacket() {
        return !packetsToBeProcessed.isEmpty();
    }

    @Override
    public int getPort() {
        return ourPort;
    }

    @Override
    public String getIp() {
        return ourIP;
    }
}
