package com.csefinalproject.github.multiplayer.networking.exceptions;

/**
 * This exception is thrown when the connection fails
 */
public class ConnectionFailedException extends Exception {
    /**
     * This constructor is used to create a new connection failed exception
     * @param s the message
     */
    public ConnectionFailedException(String s) {
        super(s);
    }
}
