package com.csefinalproject.github.multiplayer.util;

import com.csefinalproject.github.multiplayer.networking.IPeer;
import com.csefinalproject.github.multiplayer.networking.exceptions.BadCallbackResponseException;
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

/**
 * This class contains lots off static methods for simplifying interaction with the socket
 */
public class MessageUtils {
    /**
     * This method creates a {@link DatagramPacket} from a {@link Packet}
     * @param packet packet to encode
     * @return the created packet
     * @throws IllegalArgumentException if the packet is too big
     * @throws RuntimeException if there is some weird write error
     */
    @Contract("_ -> new")
    public static @NotNull DatagramPacket encodePacket(@NotNull Packet packet) {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try {
            ObjectOutputStream stream = new ObjectOutputStream(buffer);
            stream.writeObject(packet);
            stream.flush();
            if (buffer.size() > IPeer.DEFAULT_PACKET_SIZE) {
                throw new IllegalArgumentException("The passed packet was too big, Either make it smaller or increase the default max");
            }
            return new DatagramPacket(buffer.toByteArray(),buffer.size());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sends a {@link Packet} on the specified {@link DatagramSocket} to another socket
     * @param socket socket to send from
     * @param packetToSend the packet we want to send
     * @param addr the ip address of the other socket
     * @param port the port the other socket is listening on
     */
    public static void sendPacketTo(@NotNull DatagramSocket socket, @NotNull Packet packetToSend, InetAddress addr, int port) {
        DatagramPacket packet = encodePacket(packetToSend);
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
     * @throws SocketException if the socket is closed
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
        byte[] buffer = new byte[IPeer.DEFAULT_PACKET_SIZE];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        try {
            socket.receive(packet);
        }
        catch (IOException e) {
            if (e instanceof SocketException) {
                throw (SocketException) e;
            }
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
            ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(p.getData()));
            Object recievedObject = objectInputStream.readObject();
            if (!(recievedObject instanceof Packet)) {
                throw new PacketDecodeError("Received something that wasn't a packet");
            }
            return (Packet)recievedObject;
        } catch (IOException | ClassNotFoundException e) {
            throw new PacketDecodeError(e);
        }
    }

    /**
     * Sends a {@link Packet} to another socket and waits for a response. If we receive a packet before a certain amount
     * off time we return the packet. This method blocks until we recieve the callback
     * @param socket the socket to end on
     * @param toSend the packet to send
     * @param address the address of the other socket to send to
     * @param port the port off the other socket
     * @param ack the packet type we expect
     * @return the callback they sent
     * @throws ConnectionFailedException if we don't receive a packet before we time out or if we receive a packet of the wrong type
     */
    public static Packet sendMessageWaitingForAck(@NotNull DatagramSocket socket,Packet toSend,InetAddress address, int port, Class<? extends Packet> ack) throws ConnectionFailedException {
        // Have we succeeded yet
        AtomicBoolean successful = new AtomicBoolean(false);

        // Threadsafe way to do all of this
        final Packet[] result = new Packet[1];
        // wait for the callback
        Thread waitingThread = new Thread(()->{
            try {
                result[0] = waitForPacket(socket);
                successful.set(true);// We got something
            } catch (PacketDecodeError | SocketException e) {
                successful.set(false);
            }
        });

        waitingThread.start();

        sendPacketTo(socket,toSend,address,port);

        long startTime = System.currentTimeMillis();
        while (!successful.get()) {
            long elapsedTime = System.currentTimeMillis() - startTime;
            if (elapsedTime >= IPeer.DEFAULT_CONNECTION_TIMEOUT * 1000L) {
                waitingThread.interrupt();
                throw new ConnectionFailedException("Connection Failed, Timed out");
            }
        }
        if (ack.isInstance(result[0])) {
            return result[0];
        } else {
            throw new BadCallbackResponseException("Recieved a packet of the type "+result[0].getClass() +" but expected "+ack);
        }
    }

}
