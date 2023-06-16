package com.csefinalproject.github.multiplayer.networking.client;

import com.csefinalproject.github.multiplayer.networking.IPeer;
import com.csefinalproject.github.multiplayer.networking.exceptions.ConnectionFailedException;
import com.csefinalproject.github.multiplayer.networking.exceptions.PacketDecodeError;
import com.csefinalproject.github.multiplayer.networking.packet.internal.ConnectionPacket;
import com.csefinalproject.github.multiplayer.networking.packet.internal.ConnectionSuccessfulPacket;
import com.csefinalproject.github.multiplayer.networking.packet.internal.KeepAlivePacket;
import com.csefinalproject.github.multiplayer.networking.packet.Packet;
import com.csefinalproject.github.multiplayer.util.MessageUtils;
import com.csefinalproject.github.multiplayer.util.Ticker;

import java.net.*;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * This class is used to represent a client
 */
public class Client implements IPeer {
    private InetAddress address;
    private String ourIp;
    private int ourPort;
    private int targetPort;

    private boolean isConnected;
    private DatagramSocket socket;
    private Ticker clientThread;
    private final Queue<Packet> packetsReceived = new ConcurrentLinkedQueue<>();

    private long lastKeepAlivePacketTime = 0L;

    private long lastKeepAlivePacketSent = 0L;

    /**
     * This method is used to connect to a server
     * @param ip the ip of the server
     * @param port the port of the server
     * @param username the username to join as
     * @throws ConnectionFailedException if the connection failed
     * @throws UnknownHostException if the host was not found
     */
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

    /**
     * This method is used to set up our ip and port information and get the address of the server
     * @param ip the ip to connect to
     * @param port the port to connect to
     * @throws UnknownHostException if the host was not found
     */
    private void setupIpAndPort(String ip, int port) throws UnknownHostException {
        address = InetAddress.getByName(ip);
        targetPort = port;
        try {
            socket = new DatagramSocket();

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

    /**
     * This method is used internally to try to connect to the server
     * @param username the username to connect as
     * @throws ConnectionFailedException if the connection times out
     */
    private void tryToConnect(String username) throws ConnectionFailedException {
        final Packet connectionPacket = new ConnectionPacket(this,username);
        // Send a packet to the server saying that we want to connect
        MessageUtils.sendMessageWaitingForAck(socket,connectionPacket, address, targetPort, ConnectionSuccessfulPacket.class);
        lastKeepAlivePacketTime = System.currentTimeMillis();
        isConnected = true;

    }

    /**
     * This method is used internally and run every tick. It does everything the client needs to do every tick
     */
    private void clientTick() {
        if (!isConnected && clientThread.isRunning()) {
            clientThread.stop();
            return;
        }
        // Internally, we just need to send a keep alive packet every so often and disconnect if they haven't sent one recently enough
        long currentTime = System.currentTimeMillis();

        if ((currentTime - lastKeepAlivePacketSent ) / 1000L > IPeer.DEFAULT_KEEP_ALIVE_INTERVAL) {
            sendPacket(new KeepAlivePacket(this));
            lastKeepAlivePacketSent = currentTime;
        }
        if ((currentTime - lastKeepAlivePacketTime ) / 1000L > (IPeer.DEFAULT_KEEP_ALIVE_INTERVAL + IPeer.DEFAULT_KEEP_ALIVE_GRACE))
        {
            disconnect();
        }
    }

    /**
     * This method is used internally to watch for packets
     */
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

    /**
     * This method is used to disconnect from the server
     */
    public void disconnect() {
        if (!isConnected) {
            throw new IllegalStateException("Can't disconnect if we are not connected");
        }
        isConnected = false;
        socket.close();
    }

    /**
     * This method is used to send a packet to the server
     * @param packet the packet to send
     */
    public void sendPacket(Packet packet) {
        if (isConnected) {
            MessageUtils.sendPacketTo(socket, packet, address, targetPort);
        }
        else {
            throw new IllegalStateException("Can't send a packet if we are not connected");
        }
    }

    /**
     * This method is used to get the time since the last keep alive packet was received from the server
     * @return the time since the last keep alive packet was received from the server
     */
    public double lastKeepAlivePacketTime() {
        return (lastKeepAlivePacketTime - System.currentTimeMillis()) / 1000.0;
    }

    /**
     * This method is used to get the next packet from the server
     * @return the next packet from the server
     */
    @Override
    public synchronized Packet getNextPacket() {
        return packetsReceived.poll();
    }

    /**
     * This method is used to check if there is a packet available
     * @return true if there is a packet available, false otherwise
     */
    @Override
    public synchronized boolean hasNextPacket() {
        return !packetsReceived.isEmpty();
    }

    /**
     * This method is used to get the ip of the client
     * @return the ip of the client
     */
    @Override
    public String getIp() {
        return ourIp;
    }

    /**
     * This method is used to get the port of the client
     * @return the port of the client
     */
    @Override
    public int getPort() {
        return ourPort;
    }

    /**
     * This method is used to check if the client is active
     * @return true if the client is active, false otherwise
     */
    @Override
    public boolean isActive() {
        return isConnected;
    }
}