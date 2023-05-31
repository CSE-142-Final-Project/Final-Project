package com.csefinalproject.github.multiplayer.networking;

import com.csefinalproject.github.multiplayer.networking.client.Client;
import com.csefinalproject.github.multiplayer.networking.exceptions.ConnectionFailedException;
import com.csefinalproject.github.multiplayer.networking.packet.Packet;
import com.csefinalproject.github.multiplayer.networking.server.Server;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.net.UnknownHostException;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

public class ClientServerTests {
    Client client;
    Server server;

    @Test
    public void testConnectionFailsIfServerNotRunning() {
        client = new Client();
        assertThrows(ConnectionFailedException.class,() -> client.connect("127.0.0.1",(short)7777,"testing"),"Should get an exception if it can't connect");
    }

    @Test
    public void testConnectionCanBeEstablished() throws ConnectionFailedException, UnknownHostException {
        client = new Client();
        server = new Server();
        server.start((short)7777);
        client.connect("localhost",(short)7777,"testing");
    }
    @Test
    public void testConnectionInfoIsOnServer() throws ConnectionFailedException, UnknownHostException {
        client = new Client();
        server = new Server();
        server.start((short)7777);
        client.connect("localhost",(short)7777,"L (gotem)");
        assertEquals(1,server.getClientData().length,"There should be one client connected");
        assertEquals("L (gotem)", server.getClientData()[0].getUsername(),"The name should be correct server side");
    }


    @Test
    public void canSendPacketsAfterConnecting() throws UnknownHostException, ConnectionFailedException {
        client = new Client();
        server = new Server();
        server.start((short)7777);
        client.connect("localhost",(short)7777,"testing");
        client.sendPacket(new SuperPacket(client));
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        boolean hasAnotherPacket = server.hasNextPacket();
        assertTrue(hasAnotherPacket,"The server didn't receive a packet");
        Packet nextPacket = server.getNextPacket();
        assertInstanceOf(SuperPacket.class,nextPacket, "The packet recieved is not of the correct type");
        SuperPacket recieved = (SuperPacket) nextPacket;
        assertEquals(12,recieved.number,"Data inside the packet should match");
        assertEquals(12.0,recieved.otherNumber,"Data inside the packet should match");
        assertEquals("I am a string",recieved.daString,"Data inside the packet should match");

    }
    @Test
    public void clientDcsAfterTimeout() throws UnknownHostException, ConnectionFailedException {
        client = new Client();
        server = new Server();
        server.start(7777);
        client.connect("127.0.0.1",7777,"I am a human who does testing");
        server.stop();
        try {
            // Wait a little bit
            Thread.sleep(1000L * (IPeer.DEFAULT_KEEP_ALIVE_GRACE + IPeer.DEFAULT_KEEP_ALIVE_INTERVAL + 3));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        assertFalse(client.isActive(),"Client should auto disconnect after a certain period");

    }
    @Test
    public void serverDcsClientsAfterTimeout() throws UnknownHostException, ConnectionFailedException {
        server = new Server();
        client = new Client();
        server.start(7777);
        client.connect("127.0.0.1",7777,"I am a human who does testing");
        client.disconnect();
        try {
            // Wait a little bit
            Thread.sleep(1000L * (IPeer.DEFAULT_KEEP_ALIVE_GRACE + IPeer.DEFAULT_KEEP_ALIVE_INTERVAL + 3));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        assertEquals(0, server.getClientData().length, "The server should disconnect hanging clients");
    }
    @Test
    public void testServerDoesntBreakAfterManyConnections() {
        AtomicInteger timesFailed = new AtomicInteger();
        server = new Server();
        server.start(7777);
        NetworkEventManager eventManager = new NetworkEventManager(server);
        eventManager.subscribeEvent(SuperPacket.class,(SuperPacket p) -> {
            server.broadcast(new SuperPacket(server));
        });
        eventManager.subscribeHandler((Thread thread, Throwable e) -> {
            timesFailed.incrementAndGet();
        });
        for (int i = 0; i < 20; i++) {// This number may seriously impact the trout population
            client = new Client();
            try {
                client.connect("127.0.0.1",7777,"Client "+i);
                client.sendPacket(new SuperPacket(client));
            } catch (ConnectionFailedException | UnknownHostException e) {
                throw new RuntimeException(e);
            }
            client.disconnect();
        }
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        assertEquals(0,timesFailed.get(),"We should fail zero times");
    }

    @AfterEach
    public void stopServerAndClient() {
        if (client != null)
        {
            if (client.isActive())
            {
                client.disconnect();
            }
        }
        if (server != null)
        {
            if (server.isActive())
            {
                server.stop();
            }
        }
    }
    static class SuperPacket extends Packet {
        final int number;
        final double otherNumber;
        final String daString;

        public SuperPacket(IPeer peer) {
            super(peer);
            number =12;
            otherNumber = 12.0;
            daString = "I am a string";
        }

    }
}
