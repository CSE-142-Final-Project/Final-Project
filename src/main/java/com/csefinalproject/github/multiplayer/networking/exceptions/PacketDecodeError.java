package com.csefinalproject.github.multiplayer.networking.exceptions;

/**
 * This exception is thrown when the packet fails to decode
 */
public class PacketDecodeError extends Exception{
    /**
     * This constructor is used to create a new packet decode error
     * @param e the exception
     */
    public PacketDecodeError(Exception e) {
        super(e);
    }

    /**
     * This constructor is used to create a new packet decode error
     * @param s the message
     */
    public PacketDecodeError(String s) {
        super(s);
    }
}
