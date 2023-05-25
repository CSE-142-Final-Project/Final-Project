package com.csefinalproject.github.multiplayer.util;

import com.csefinalproject.github.multiplayer.networking.IPeer;
import com.csefinalproject.github.multiplayer.networking.exceptions.ConnectionFailedException;
import com.csefinalproject.github.multiplayer.networking.exceptions.PacketDecodeError;
import com.csefinalproject.github.multiplayer.networking.packet.Packet;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.csefinalproject.github.multiplayer.networking.IPeer.*;

public class MessageUtils {

    @Contract("_ -> new")
    public static @NotNull DatagramPacket encodePacket(@NotNull Packet packet) {
        ByteArrayOutputStream buffer =new ByteArrayOutputStream(DEFAULT_PACKET_SIZE);
        try {
            new ObjectOutputStream(buffer).writeObject(packet);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new DatagramPacket(buffer.toByteArray(),buffer.size());
    }
    public static void sendPacketTo(@NotNull DatagramSocket socket, @NotNull DatagramPacket packet, InetAddress addr, short port) {
        packet.setAddress(addr);
        packet.setPort(port);
        try {
            socket.send(packet);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Waits for a packet
     * @param socket socket to wait on
     * @return the packet we got
     * @throws PacketDecodeError if we cant decode the packet
     */
    public static Packet waitForPacket(@NotNull DatagramSocket socket) throws PacketDecodeError, SocketException {
        return decodePacket(receivePacket(socket));
    }

    /**
     * Use this to wait for a packet
     * @param socket socket to listen on
     * @return the packet that was received
     */
    private static DatagramPacket receivePacket(@NotNull DatagramSocket socket) throws SocketException {
        byte[] buffer = new byte[DEFAULT_PACKET_SIZE];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        try {
            socket.receive(packet);
        }
        catch (IOException e) {
            // We really shouldn't be getting these.
            // I don't want to quit silently so lets exit in a noisy and visible way
            throw new RuntimeException(e);
        }
        return packet;
    }

    /**
     * Convert a datagram packet to a usable internal packet
     * @param p datagram packet to convert
     * @return packet that was converted
     * @throws PacketDecodeError If the packet can't be decoded or if we received something that wasn't a packet
     */
    private static Packet decodePacket(@NotNull DatagramPacket p) throws PacketDecodeError {
        try {
            Object recievedObject = new ObjectInputStream(new ByteArrayInputStream(p.getData())).readObject();
            if (!(recievedObject instanceof Packet)) {
                throw new PacketDecodeError("Received something that wasn't a packet");
            }
            return (Packet)recievedObject;
        } catch (IOException | ClassNotFoundException e) {
            throw new PacketDecodeError(e);
        }
    }
    public static Packet waitForAck(@NotNull DatagramSocket socket, Class<? extends Packet> ack) throws ConnectionFailedException {
        // Have we succeeded yet
        AtomicBoolean successful = new AtomicBoolean(false);
        // Threadsafe way to do all of this
        final Packet[] result = new Packet[1];
        // wait for the callback
        Thread waitingThread = new Thread(()->{
            try {
                result[0] = waitForPacket(socket);
                successful.set(ack.isInstance(result[0]));
            } catch (PacketDecodeError | SocketException e) {
                successful.set(false);
            }
        });

        waitingThread.start();
        long startTime = System.currentTimeMillis();
        while (!successful.get()) {
            long elapsedTime = System.currentTimeMillis() - startTime;
            if (elapsedTime >= IPeer.DEFAULT_CONNECTION_TIMEOUT * 1000L) {
                waitingThread.interrupt();
                throw new ConnectionFailedException("Connection Failed, Timed out");
            }
        }
        return result[0];
    }

}
