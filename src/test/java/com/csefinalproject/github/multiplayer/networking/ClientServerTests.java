package com.csefinalproject.github.multiplayer.networking;

import com.csefinalproject.github.multiplayer.networking.exceptions.ConnectionFailedException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.UnknownHostException;

public class ClientServerTests {
    @Test
    public void testConnectionFailsIfServerNotRunning() {
        Client client = new Client(port);
        Assertions.assertThrows(ConnectionFailedException.class,() -> client.connect("127.0.0.1",(short)7777,"testing"),"Should get an exception if it can't connect");
    }

    @Test
    public void testConnectionCanBeEstablished() throws ConnectionFailedException, UnknownHostException {
        Client client = new Client(port);
        Server server = new Server();
        server.start((short)7777);
        client.connect("127.0.0.1",(short)7777,"testing");
    }
}
