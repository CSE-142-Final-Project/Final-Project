package com.csefinalproject.github.multiplayer.util;
import java.util.List;
import java.util.ArrayList;
import java.lang.Thread;

public class Ticker {
    final int timeBetweenTicks;
    List<Runnable> subscribed;
    boolean _running;
    Thread threadToRun;
    public Ticker(int TPS) {
        subscribed = new ArrayList<>();
        timeBetweenTicks = 1000 / TPS;
    }

    /**
     * Runs this function every tick
     * @param r Function to run
     */
    public void subscribe(Runnable r) {
        subscribed.add(r);
    }

    public void start() {
        if (_running == false)
        {
            _running = true;
            threadToRun = new Thread(() -> {
                try
                {
                    Thread.sleep(timeBetweenTicks);
                    for (Runnable toRun : subscribed) {
                        toRun.run();
                    }
                }
                catch(InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
            threadToRun.start();
        }
        else
        {
            throw new IllegalStateException("You cannot start a ticker that is already running");
        }
    }
    public void stop() {
        if (_running == true)
        {
            _running = false;
            threadToRun = null;
        }
        else
        {
            throw new IllegalStateException("You cannot stop a ticker that is already stopped");
        }
    }
}