package com.csefinalproject.github.multiplayer.networking;

import com.csefinalproject.github.multiplayer.networking.packet.Packet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

/**
 * This class is responsible for managing events that are called when a packet is received
 */
public class NetworkEventManager {
    private final HashMap<Class<? extends Packet>, List<Consumer<Packet>>> subscribedEvents;
    private Thread eventMonitor;
    private final IPeer peerToWatch;
    private ArrayList<Thread.UncaughtExceptionHandler> handlers;
    Thread.UncaughtExceptionHandler handle;

    /**
     * Creates a new NetworkEventManager. As this object is created it will start a thread to watch for packets
     * @param peer The peer to watch for packets
     */
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

    /**
     * This function will be run in a thread on the creation of the object and is used to watch for packets
     */
    private void watchForPackets() {
        // While our peer is alive lets watch for packets
        while (peerToWatch.isActive()) {
            // Wait until there is a packet
            while (!peerToWatch.hasNextPacket()) {
                if (!peerToWatch.isActive()) {
                    return;
                }
            }

            Packet packet = peerToWatch.getNextPacket();
            Class<? extends Packet> packetClass = packet.getClass();
            // Call any events
            if (subscribedEvents.containsKey(packetClass)) {
                // Call each event on their own thread
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

    /**
     * Internally used to wait and see if the peer comes back online
     */
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

    /**
     * Adds a handler for any exceptions that occur in the execution off events.
     * @param handle the way we should handle exceptions
     */
    public void subscribeErrorHandler(Thread.UncaughtExceptionHandler handle) {
        handlers.add(handle);
    }
}
