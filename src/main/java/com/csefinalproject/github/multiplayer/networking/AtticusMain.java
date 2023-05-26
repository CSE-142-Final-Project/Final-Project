package com.csefinalproject.github.multiplayer.networking;

import com.csefinalproject.github.multiplayer.networking.server.Server;

public class AtticusMain {
    public static void main(String[] args) {
        Server server = new Server();
        server.start((short) 7777);
    }
}
