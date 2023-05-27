package com.csefinalproject.github.multiplayer.networking;

import com.csefinalproject.github.multiplayer.networking.client.Client;
import com.csefinalproject.github.multiplayer.networking.exceptions.ConnectionFailedException;
import com.csefinalproject.github.multiplayer.networking.server.Server;

import java.net.UnknownHostException;

public class AtticusMain {
    public static void main(String[] args) {
        Client client = new Client();
        Server server = new Server();
        server.start(7777);
        try {
            client.connect("127.0.0.1",7777,"user");
        } catch (ConnectionFailedException | UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }
}
