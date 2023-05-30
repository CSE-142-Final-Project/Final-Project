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
        System.out.println("Setting up");
//        Client client = new Client();
        Server server = new Server();
        server.start(7777);
//        try {
//            client.connect("127.0.0.1",7777,"user546");
//        } catch (ConnectionFailedException | UnknownHostException e) {
//            throw new RuntimeException(e);
//        }
//        NetworkEventManager clientHandle = new NetworkEventManager(client);
        NetworkEventManager serverHandle = new NetworkEventManager(server);

        serverHandle.subscribeEvent(ChatPacket.class,(ChatPacket packet) -> {
            server.broadcast(new ChatPacket(server,server.getUserFromPacket(packet).getUsername()+": "+packet.getMessage()));
            System.out.println(server.getUserFromPacket(packet).getUsername()+": "+packet.getMessage());
        });
//        clientHandle.subscribeEvent(ChatPacket.class, (ChatPacket packet) -> {
//            System.out.println(packet.getMessage());
//        });
//        System.out.println("Sending packets");
//        for (int i = 0; i < 1000; i++) {
//            client.sendPacket(new ChatPacket(client,"Haha echo go brrr" + i));
//        }
        try {
            Thread.sleep(10000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
//        client.disconnect();
//        server.stop();
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
