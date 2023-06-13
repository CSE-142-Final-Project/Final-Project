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
        for (int i = 0; i < 20; i++) {
            Client client = new Client();
            try {
                client.connect("10.177.199.35",7777,"Client "+i);
                client.sendPacket(new ChatPacket(client,"Hello I am a person"));
            } catch (ConnectionFailedException | UnknownHostException e) {
                throw new RuntimeException(e);
            }
            client.disconnect();
        }

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
