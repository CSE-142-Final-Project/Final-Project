package com.csefinalproject.github.multiplayer.networking;

import com.csefinalproject.github.multiplayer.networking.packet.Packet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

public class NetworkEventManager {
    private final HashMap<Class<? extends Packet>, List<Consumer<Packet>>> subscribedEvents;
    private Thread eventMonitor;
    private final IPeer peerToWatch;
    private ArrayList<Thread.UncaughtExceptionHandler> handlers;
    Thread.UncaughtExceptionHandler handle;
    public NetworkEventManager(IPeer peer) {
        subscribedEvents = new HashMap<>();
        peerToWatch = peer;
        eventMonitor = new Thread(this::watchForPackets);
        eventMonitor.start();
        handle = (t, e) -> {
            for (Thread.UncaughtExceptionHandler handler : handlers) {
                handler.uncaughtException(t, e);
            }
        };
        handlers = new ArrayList<>();
    }
    private void watchForPackets() {
        while (peerToWatch.isActive()) {
            while (!peerToWatch.hasNextPacket()) {
                if (!peerToWatch.isActive()) {
                    return;
                }
            }
            Packet packet = peerToWatch.getNextPacket();
            Class<? extends Packet> packetClass = packet.getClass();
            if (subscribedEvents.containsKey(packetClass)) {
                for (Consumer<Packet> event : subscribedEvents.get(packetClass)) {
                    Thread thread = new Thread(() -> event.accept(packet));
                    thread.setUncaughtExceptionHandler(handle);
                    thread.start();

                }
            }
        }
        Thread restartWatcher = new Thread(this::waitForRestart);
        restartWatcher.setDaemon(true);
        restartWatcher.start();
    }

    private void waitForRestart() {
        while (!peerToWatch.isActive()) {
            try {
                Thread.sleep(100L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        eventMonitor = new Thread(this::watchForPackets);// Can't start a thread twice so instead just make a new one
        eventMonitor.start();
    }

    /**
     * Subscribe an event. It will be called when the peer receives a packet of the specified type.
     * These individual functions will be run in their own threads, and there is no guarantee of order.
     * @param packetType Type of the packet to listen for
     * @param onPacket Function to run when the packet is received
     */
    @SuppressWarnings("unchecked")// We should be able to downcast a packet every time. Not sure why this warning is here. If this is actually problematic, please let me know
    public void subscribeEvent(Class<? extends Packet> packetType, Consumer<? extends Packet> onPacket) {
        if (!subscribedEvents.containsKey(packetType)) {
            subscribedEvents.put(packetType, new ArrayList<>());
        }
        subscribedEvents.get(packetType).add((Consumer<Packet>) onPacket);
    }
    public void subscribeHandler(Thread.UncaughtExceptionHandler handle) {
        handlers.add(handle);
    }
}
