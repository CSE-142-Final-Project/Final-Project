package com.csefinalproject.github.multiplayer.networking.server;

import com.csefinalproject.github.multiplayer.networking.IPeer;
import com.csefinalproject.github.multiplayer.networking.exceptions.PacketDecodeError;
import com.csefinalproject.github.multiplayer.networking.packet.internal.ConnectionPacket;
import com.csefinalproject.github.multiplayer.networking.packet.internal.ConnectionSuccessfulPacket;
import com.csefinalproject.github.multiplayer.networking.packet.internal.KeepAlivePacket;
import com.csefinalproject.github.multiplayer.networking.packet.Packet;
import com.csefinalproject.github.multiplayer.util.MessageUtils;
import com.csefinalproject.github.multiplayer.util.Ticker;
import org.jetbrains.annotations.NotNull;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Server implements IPeer {

    private DatagramSocket socket;
    private int ourPort;
    private String ourIP;
    private boolean running;
    private final Queue<Packet> packetsToBeProcessed = new ConcurrentLinkedQueue<>();
    private Ticker serverThread;
    private final ConcurrentHashMap<Short,ClientData> connected = new ConcurrentHashMap<>();

    Thread packetWatcher;

    public void start(int port) {
        if (running) {
            throw new IllegalStateException("Please disconnect the server before attempting to start it again");
        }
        setupIpAndPort(port);


        running = true;
        serverThread = new Ticker(IPeer.DEFAULT_TPS);
        packetWatcher = new Thread(this::packetWatch);
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
                    // Yay! they connected
                    send(new ConnectionSuccessfulPacket(this),newClient.getClientID());
                } else if (packet instanceof KeepAlivePacket keepAlivePacket) {// Note that we got a keep alive packet and send one back
                    ClientData sender = ClientData.getFromIpAndPort(keepAlivePacket.getIp(),keepAlivePacket.getPort());
                    connected.get(sender.getClientID()).setLastReceivedPacketTime(System.currentTimeMillis());
                    send(new KeepAlivePacket(this),sender.getClientID());
                } else {
                    packetsToBeProcessed.add(packet);
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
        if (!running && serverThread.isRunning())
        {
            serverThread.stop();
        }
        // iterate through the connected clients
        long currentTime = System.currentTimeMillis();

        Collection<ClientData> clientValues = connected.values();
        for (ClientData data : clientValues) {
            if ((currentTime - data.getLastReceivedPacketTime()) / 1000 >= IPeer.DEFAULT_KEEP_ALIVE_INTERVAL + IPeer.DEFAULT_KEEP_ALIVE_GRACE) {
                // They are disconnected
                connected.remove(data.getClientID());
            }
        }
    }

    private void setupIpAndPort(int port) {
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
        if (!running) {
            throw new IllegalStateException("You can't stop a server that isn't running");
        }
        running = false;
        serverThread.stop();
        socket.close();
    }
    public void send(Packet packet, short client) {
        sendMessageToClient(packet,connected.get(client));
    }
    public void broadcast(Packet packet) {
        Collection<ClientData> clientValues = connected.values();
        for (ClientData data : clientValues) {
            sendMessageToClient(packet,data);
        }
    }

    private void sendMessageToClient(Packet packet, @NotNull ClientData data) {
        try {
            MessageUtils.sendPacketTo(socket,packet,InetAddress.getByName(data.getIP()), data.getPort());
        } catch (UnknownHostException e) {
            // This is impossible and will never happen unless someone does weird reflection
            throw new RuntimeException(e);
        }
    }

    public ClientData getUserFromPacket(Packet packet) {
        return ClientData.getFromIpAndPort(packet.getIp(),packet.getPort());
    }


    public ClientData[] getClientData() {
        return connected.values().toArray(new ClientData[0]);
    }


    public synchronized Packet getNextPacket() {
        return packetsToBeProcessed.poll();
    }
    public synchronized boolean hasNextPacket() {
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
    @Override
    public boolean isActive() {
        return running;
    }
}
