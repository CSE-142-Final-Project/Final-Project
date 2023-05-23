package com.csefinalproject.github.multiplayer.networking;

import com.csefinalproject.github.multiplayer.networking.exceptions.PacketDecodeError;
import com.csefinalproject.github.multiplayer.networking.packet.Packet;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public interface IPeer {
    int DEFAULT_TPS = 20;
    /**
     * Time in seconds that a peer waits for a response from the other peer
     */
    int DEFAULT_CONNECTION_TIMEOUT = 10;
    int DEFAULT_PACKET_SIZE = 256;
    Packet getNextPacket();
    boolean hasNextPacket();
    default DatagramPacket receivePacket(@NotNull DatagramSocket socket) {
        byte[] buffer = new byte[DEFAULT_PACKET_SIZE];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        try {
            socket.receive(packet);
        }
        catch (IOException e) {
            // We really shouldn't be getting these.
            // I don't want to quit silently
            throw new RuntimeException(e);
        }
        return packet;
    }
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

}
