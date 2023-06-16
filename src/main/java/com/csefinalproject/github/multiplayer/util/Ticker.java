package com.csefinalproject.github.multiplayer.util;
import java.util.List;
import java.util.ArrayList;
import java.lang.Thread;

/**
 * This object is used to call functions at a regular interval
 */
public class Ticker {
    final int timeBetweenTicks;
    List<Runnable> subscribed;
    boolean _running;
    Thread threadToRun;

    /**
     * Creates a ticker that can run functions at a regular interval
     * @param TPS the number of times to run the subscribed function each second
     */
    public Ticker(int TPS) {
        subscribed = new ArrayList<>();
        timeBetweenTicks = 1000 / TPS;
    }

    /**
     * Runs this function every tick. If the ticker is currently running it will run it in the next step.
     * @param r Function to run
     */
    public void subscribe(Runnable r) {
        subscribed.add(r);
    }

    /**
     * Start the ticker running subscribed
     * @throws IllegalStateException if the ticker is already running
     */
    public void start() {
        if (!_running)
        {
            _running = true;
            threadToRun = new Thread(() -> {
                while (_running)
                {
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
                }
            });
            threadToRun.start();
        }
        else
        {
            throw new IllegalStateException("You cannot start a ticker that is already running");
        }
    }

    /**
     * Stops the ticker. The way everything is implemented
     * @throws IllegalStateException if the ticker is currently stopped
     */
    public void stop() {
        if (_running)
        {
            _running = false;
            threadToRun = null;
        }
        else
        {
            throw new IllegalStateException("You cannot stop a ticker that is already stopped");
        }
    }

    /**
     * @return whether or not the ticker is running.
     */
    public boolean isRunning() {
        return _running;
    }
}