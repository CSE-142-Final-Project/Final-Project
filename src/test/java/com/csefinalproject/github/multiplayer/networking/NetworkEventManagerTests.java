package com.csefinalproject.github.multiplayer.networking;

import com.csefinalproject.github.multiplayer.networking.client.Client;
import com.csefinalproject.github.multiplayer.networking.exceptions.ConnectionFailedException;
import com.csefinalproject.github.multiplayer.networking.server.Server;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.UnknownHostException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;
public class NetworkEventManagerTests {
    Client client;
    Server server;
    @BeforeEach
    public void setup() throws UnknownHostException, ConnectionFailedException {
        client = new Client();
        server = new Server();
        server.start((short)7777);
        client.connect("localhost",(short)7777,"testing");
    }
    @AfterEach
    public void teardown() {
        if (client.isActive()) {
            client.disconnect();
        }

        if (server.isActive()) {
            server.stop();
        }

    }

    @Test
    public void testNetworkManagerCanSubscribeGoodEvents() {
        NetworkEventManager manager = new NetworkEventManager(client);
        Consumer<ClientServerTests.SuperPacket> onSuperPacket = packet -> {};
        manager.subscribeEvent(ClientServerTests.SuperPacket.class,onSuperPacket);
    }
    @Test
    public void testNetworkManagerEventsHappen() {
        NetworkEventManager manager = new NetworkEventManager(server);
        AtomicBoolean happened = new AtomicBoolean(false);
        Consumer<ClientServerTests.SuperPacket> onSuperPacket = packet -> {
            assertEquals(12,packet.number,"Data inside the packet should match");
            assertEquals(12.0,packet.otherNumber,"Data inside the packet should match");
            happened.set(true);
        };
        manager.subscribeEvent(ClientServerTests.SuperPacket.class,onSuperPacket);
        client.sendPacket(new ClientServerTests.SuperPacket(client));
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        assertTrue(happened.get(),"The event should have happened");
    }
    @Test
    public void testNetworkManagerMultipleEventsHappen() {
        NetworkEventManager manager = new NetworkEventManager(server);
        AtomicInteger happened = new AtomicInteger();
        Consumer<ClientServerTests.SuperPacket> onSuperPacket = packet -> {
            assertEquals(12,packet.number,"Data inside the packet should match");
            assertEquals(12.0,packet.otherNumber,"Data inside the packet should match");
            happened.getAndIncrement();
        };
        manager.subscribeEvent(ClientServerTests.SuperPacket.class,onSuperPacket);
        manager.subscribeEvent(ClientServerTests.SuperPacket.class,onSuperPacket);
        manager.subscribeEvent(ClientServerTests.SuperPacket.class,onSuperPacket);
        client.sendPacket(new ClientServerTests.SuperPacket(client));
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        assertEquals(3,happened.get(),"The event should have happened 3 times");
    }

}
