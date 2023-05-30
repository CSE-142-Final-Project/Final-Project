package com.csefinalproject.github.multiplayer.libtest;

import com.csefinalproject.github.multiplayer.networking.IPeer;
import com.csefinalproject.github.multiplayer.networking.NetworkEventManager;
import com.csefinalproject.github.multiplayer.networking.client.Client;
import com.csefinalproject.github.multiplayer.networking.exceptions.ConnectionFailedException;
import com.csefinalproject.github.multiplayer.networking.packet.Packet;
import com.csefinalproject.github.multiplayer.networking.server.Server;

import java.net.UnknownHostException;

public class AtticusMain {
    public static void main(String[] args) {
        Client client = new Client();
        NetworkEventManager clientHandle = new NetworkEventManager(client);
        Server server = new Server();
        NetworkEventManager serverHandle = new NetworkEventManager(server);
        server.start(7777);
        try {
            client.connect("127.0.0.1",7777,"user1");
        } catch (ConnectionFailedException | UnknownHostException e) {
            throw new RuntimeException(e);
        }
        serverHandle.subscribeEvent(ChatPacket.class,(ChatPacket packet) -> {
            System.out.println("Server received message: "+packet.getMessage());
            server.broadcast(new ChatPacket(server,server.getUserFromPacket(packet).getUsername()+": "+packet.getMessage()));
        });
        clientHandle.subscribeEvent(ChatPacket.class, (ChatPacket packet) -> {
            System.out.println(packet.getMessage());
        });
        client.sendPacket(new ChatPacket(client,"Hello from me"));

    }
}
class ChatPacket extends Packet {
    final String message;
    public ChatPacket(IPeer peerToSendFrom, String message) {
        super(peerToSendFrom);
        this.message = message;
    }
    public String getMessage() {
        return message;
    }
}
