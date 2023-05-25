package com.csefinalproject.github.multiplayer.networking;

import com.csefinalproject.github.multiplayer.networking.exceptions.ConnectionFailedException;
import com.csefinalproject.github.multiplayer.networking.exceptions.PacketDecodeError;
import com.csefinalproject.github.multiplayer.networking.packet.ConnectionPacket;
import com.csefinalproject.github.multiplayer.networking.packet.ConnectionSuccessfulPacket;
import com.csefinalproject.github.multiplayer.networking.packet.KeepAlivePacket;
import com.csefinalproject.github.multiplayer.networking.packet.Packet;
import com.csefinalproject.github.multiplayer.util.MessageUtils;
import com.csefinalproject.github.multiplayer.util.Ticker;

import java.io.IOException;
import java.net.*;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Client implements IPeer {
    InetAddress address;
    String ip;
    short port;
    boolean isConnected;
    DatagramSocket socket;
    Ticker clientThread;
    Queue<Packet> packetsReceived = new ConcurrentLinkedQueue<>();

    long lastKeepAlivePacketTime = 0L;

    long lastKeepAlivePacketSent = 0L;

    public void connect(String ip, short port, String username) throws ConnectionFailedException, UnknownHostException {
        if (isConnected) {
            throw new IllegalStateException("Please disconnect the client before attempting to connect again");
        }
        address = InetAddress.getByName(ip);

        try {
            socket = new DatagramSocket();
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
        this.ip = socket.getInetAddress().toString();
        this.port = (short) socket.getPort();
        tryToConnect(username);
        clientThread = new Ticker(IPeer.DEFAULT_TPS);
        Thread packetWatcher = new Thread(this::packetWatch);
        clientThread.subscribe(this::clientTick);
        clientThread.start();
        packetWatcher.start();

    }

    private void tryToConnect(String username) throws ConnectionFailedException {
        final Packet connectionPacket = new ConnectionPacket(ip, (short) socket.getPort(),username);
        // Send a packet to the server saying that we want to connect
        this.sendPacket(connectionPacket);
        // This will throw an exception if we don't get a response or the response is not the right packet type
        MessageUtils.waitForAck(socket,ConnectionSuccessfulPacket.class);
        isConnected = true;
        lastKeepAlivePacketTime = System.currentTimeMillis();
    }

    private void clientTick() {
        if (!isConnected) {
            clientThread.stop();
        }
        // Internally we just need to send a keep alive packet every so often and disconnect if they haven't sent one recently enogu
        if (lastKeepAlivePacketSent - System.currentTimeMillis() > IPeer.DEFAULT_KEEP_ALIVE_INTERVAL * 1000) {
            sendPacket(new KeepAlivePacket(this.ip,this.port));
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
            MessageUtils.sendPacketTo(socket, MessageUtils.encodePacket(packet), address, port);
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
        return ip;
    }

    public short getPort() {
        return port;
    }
}