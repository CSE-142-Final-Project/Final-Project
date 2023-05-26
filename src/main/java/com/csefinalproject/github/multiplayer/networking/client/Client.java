package com.csefinalproject.github.multiplayer.networking.client;

import com.csefinalproject.github.multiplayer.networking.IPeer;
import com.csefinalproject.github.multiplayer.networking.exceptions.ConnectionFailedException;
import com.csefinalproject.github.multiplayer.networking.exceptions.PacketDecodeError;
import com.csefinalproject.github.multiplayer.networking.packet.ConnectionPacket;
import com.csefinalproject.github.multiplayer.networking.packet.ConnectionSuccessfulPacket;
import com.csefinalproject.github.multiplayer.networking.packet.KeepAlivePacket;
import com.csefinalproject.github.multiplayer.networking.packet.Packet;
import com.csefinalproject.github.multiplayer.util.MessageUtils;
import com.csefinalproject.github.multiplayer.util.Ticker;

import java.net.*;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Client implements IPeer {
    InetAddress address;
    String ourIp;
    int ourPort;
    int targetPort;

    boolean isConnected;
    DatagramSocket socket;
    Ticker clientThread;
    Queue<Packet> packetsReceived = new ConcurrentLinkedQueue<>();

    long lastKeepAlivePacketTime = 0L;

    long lastKeepAlivePacketSent = 0L;

    public void connect(String ip, int port, String username) throws ConnectionFailedException, UnknownHostException {
        if (isConnected) {
            throw new IllegalStateException("Please disconnect the client before attempting to connect again");
        }
        setupIpAndPort(ip, port);

        tryToConnect(username);


        clientThread = new Ticker(IPeer.DEFAULT_TPS);
        Thread packetWatcher = new Thread(this::packetWatch);
        clientThread.subscribe(this::clientTick);
        clientThread.start();
        packetWatcher.start();
    }

    private void setupIpAndPort(String ip, int port) throws UnknownHostException {
        address = InetAddress.getByName(ip);
        targetPort = port;
        try {
            socket = new DatagramSocket();
            socket.connect(address,targetPort);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
        try {
            this.ourIp = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            throw new RuntimeException("We can't resolve the localhost. That is pretty weird",e);// See the comment in Server
        }
        this.ourPort = socket.getLocalPort();
    }

    private void tryToConnect(String username) throws ConnectionFailedException {
        final Packet connectionPacket = new ConnectionPacket(this,username);
        // Send a packet to the server saying that we want to connect
        MessageUtils.sendPacketTo(socket, MessageUtils.encodePacket(connectionPacket), address, targetPort);
        // This will throw an exception if we don't get a response or the response is not the right packet type
        MessageUtils.waitForAck(socket,ConnectionSuccessfulPacket.class);
        lastKeepAlivePacketTime = System.currentTimeMillis();
        isConnected = true;

    }

    private void clientTick() {
        if (!isConnected) {
            clientThread.stop();
        }
        // Internally we just need to send a keep alive packet every so often and disconnect if they haven't sent one recently enough
        if (lastKeepAlivePacketSent - System.currentTimeMillis() > IPeer.DEFAULT_KEEP_ALIVE_INTERVAL * 1000) {
            sendPacket(new KeepAlivePacket(this));
            lastKeepAlivePacketSent = System.currentTimeMillis();
        }
        if (lastKeepAlivePacketTime - System.currentTimeMillis() > (IPeer.DEFAULT_KEEP_ALIVE_INTERVAL + IPeer.DEFAULT_KEEP_ALIVE_GRACE) * 1000)
        {
            disconnect();
        }
    }
    private void packetWatch() {
        while (isConnected) {
            try {
                Packet packet = MessageUtils.waitForPacket(socket);
                // Handle these internally
                if (packet instanceof KeepAlivePacket) {
                    lastKeepAlivePacketTime = System.currentTimeMillis();
                } else {// Otherwise we can just add it to the queue
                    packetsReceived.add(packet);
                }
            } catch (PacketDecodeError e) {
                throw new RuntimeException(e);
            } catch (SocketException e) {
                // This will usually be thrown if we are shutting down while waiting for a packet
            }
        }
    }


    public void disconnect() {
        if (!isConnected) {
            throw new IllegalStateException("Can't disconnect if we are not connected");
        }
        isConnected = false;
        socket.close();
    }

    public void sendPacket(Packet packet) {
        if (isConnected) {
            MessageUtils.sendPacketTo(socket, MessageUtils.encodePacket(packet), address, targetPort);
        }
        else {
            throw new IllegalStateException("Can't send a packet if we are not connected");
        }
    }

    public double lastKeepAlivePacketTime() {
        return (lastKeepAlivePacketTime - System.currentTimeMillis()) / 1000.0;
    }

    public Packet getNextPacket() {
        return packetsReceived.poll();
    }
    public boolean hasNextPacket() {
        return !packetsReceived.isEmpty();
    }

    public String getIp() {
        return ourIp;
    }

    public int getPort() {
        return ourPort;
    }
}