package com.csefinalproject.github.multiplayer.ticker;

import com.csefinalproject.github.multiplayer.util.Ticker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;

public class TickerTests {
    @Test
    public void testScheduling() {
        Ticker ticker = new Ticker(100);
        AtomicBoolean happened = new AtomicBoolean(false);
        ticker.subscribe(() -> {
            happened.set(true);
        });
        Assertions.assertFalse(happened.get(),"Scheduled functions should not be called until after the ticker starts");
        ticker.start();
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertTrue(happened.get(), "Function should be called");
    }
    @Test()
    public void testExceptions() {
        Ticker ticker = new Ticker(100);
        ticker.start();
        Assertions.assertThrows(IllegalStateException.class,ticker::start);
        ticker.stop();
        Assertions.assertThrows(IllegalStateException.class,ticker::stop);
    }

}