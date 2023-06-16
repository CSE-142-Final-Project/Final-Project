package com.csefinalproject.github.multiplayer.networking.server;

import com.csefinalproject.github.multiplayer.behaviour.shared.Player;
import com.csefinalproject.github.multiplayer.networking.IPeer;
import com.csefinalproject.github.multiplayer.networking.exceptions.PacketDecodeError;
import com.csefinalproject.github.multiplayer.networking.packet.PlayerLeftPacket;
import com.csefinalproject.github.multiplayer.networking.packet.internal.ConnectionPacket;
import com.csefinalproject.github.multiplayer.networking.packet.internal.ConnectionSuccessfulPacket;
import com.csefinalproject.github.multiplayer.networking.packet.internal.DummyTimeoutPacket;
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

/**
 * This class is the server, it handles all the networking a server would need to handle
 * It automatically sends keep alive packets and disconnects clients if they don't respond
 */
public class Server implements IPeer {

    private DatagramSocket socket;
    private int ourPort;
    private String ourIP;
    private boolean running;
    private final Queue<Packet> packetsToBeProcessed = new ConcurrentLinkedQueue<>();
    private Ticker serverThread;
    private final ConcurrentHashMap<Short,ClientData> connected = new ConcurrentHashMap<>();

    Thread packetWatcher;

    /**
     * This method starts the server
     * @param port the port to listen on
     */
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

    /**
     * This method is used internally to wait and process incoming packets
     */
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
                    if (sender == null) {
                        // This is quite odd
                        continue;
                    }
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
    /**
     * This method is used internally to process the server tick
     */
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
                packetsToBeProcessed.add(new DummyTimeoutPacket(this,data));
                // They are disconnected
                connected.remove(data.getClientID());
            }
        }
    }

    /**
     * This method figures out all the ip and port information
     * @param port the port to listen on
     */
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

    /**
     * This method stops the server
     */
    public void stop() {
        if (!running) {
            throw new IllegalStateException("You can't stop a server that isn't running");
        }
        running = false;
        serverThread.stop();
        socket.close();
    }

    /**
     * Send a packet on the server to a specific client
     * @param packet packet to send
     * @param client the ID of the client to send to
     */
    public void send(Packet packet, short client) {
        ClientData target = connected.get(client);
        if (target == null) {
            throw new IllegalArgumentException("That client has been disconnected ");
        }
        sendMessageToClient(packet,target);
    }

    /**
     * Sends a packet to all connected clients
     * @param packet the packet to send
     */
    public void broadcast(Packet packet) {
        Collection<ClientData> clientValues = connected.values();
        for (ClientData data : clientValues) {
            sendMessageToClient(packet,data);
        }
    }

    /**
     * Internal method to send a packet to a client from {@link ClientData}
     * @param packet packet to send
     * @param data client to send to
     */
    private void sendMessageToClient(Packet packet, @NotNull ClientData data) {
        try {
            MessageUtils.sendPacketTo(socket,packet,InetAddress.getByName(data.getIP()), data.getPort());
        } catch (UnknownHostException e) {
            // This is impossible and will never happen unless someone does weird reflection
            throw new RuntimeException(e);
        }
    }

    /**
     * This method gets the sender of a packet
     * @param packet packet to get the client data from
     * @return the client data of the sender
     */
    public ClientData getUserFromPacket(Packet packet) {
        return ClientData.getFromIpAndPort(packet.getIp(),packet.getPort());
    }

    /**
     * This method gets all the connected clients
     * @return an array of all the connected clients
     */
    public ClientData[] getClientData() {
        return connected.values().toArray(new ClientData[0]);
    }

    /**
     * This method gets the next packet to be processed
     * @return the next packet to be processed
     */
    public synchronized Packet getNextPacket() {
        return packetsToBeProcessed.poll();
    }

    /**
     * This method checks if there is a packet to be processed
     * @return if there is a packet to be processed
     */
    public synchronized boolean hasNextPacket() {
        return !packetsToBeProcessed.isEmpty();
    }

    /**
     * This method gets the port the server is listening on
     * @return the port the server is listening on
     */
    @Override
    public int getPort() {
        return ourPort;
    }

    /**
     * This method gets the ip the server is running on
     * @return the ip the server is running on
     */
    @Override
    public String getIp() {
        return ourIP;
    }

    /**
     * This method checks if the server is running
     * @return if the server is running
     */
    @Override
    public boolean isActive() {
        return running;
    }

}
