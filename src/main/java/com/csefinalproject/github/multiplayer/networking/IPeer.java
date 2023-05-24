package com.csefinalproject.github.multiplayer.networking;

import com.csefinalproject.github.multiplayer.networking.exceptions.ConnectionFailedException;
import com.csefinalproject.github.multiplayer.networking.exceptions.PacketDecodeError;
import com.csefinalproject.github.multiplayer.networking.packet.Packet;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.concurrent.atomic.AtomicBoolean;

public interface IPeer {
    int DEFAULT_TPS = 20;
    /**
     * Time in seconds that a peer waits for a response from the other peer
     */
    int DEFAULT_CONNECTION_TIMEOUT = 10;
    int DEFAULT_PACKET_SIZE = 256;

    Packet getNextPacket();
    boolean hasNextPacket();

    /**
     * Use this to wait for a packet
     * @param socket socket to listen on
     * @return the packet that was recieved
     */
    default DatagramPacket receivePacket(@NotNull DatagramSocket socket) {
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
    default Packet decodePacket(@NotNull DatagramPacket p) throws PacketDecodeError {

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
    default Packet waitForAck(@NotNull DatagramSocket socket, Class<? extends Packet> ack) throws ConnectionFailedException {
        // Have we succeeded yet
        AtomicBoolean successful = new AtomicBoolean(false);
        // Threadsafe way to do all of this
        final Packet[] result = new Packet[1];
        // wait for the callback
        Thread waitingThread = new Thread(()->{
            DatagramPacket receivedPacket = receivePacket(socket);
            try {
                result[0] = decodePacket(receivedPacket);
                successful.set(ack.isInstance(result[0]));
            } catch (PacketDecodeError e) {
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

    default
}
