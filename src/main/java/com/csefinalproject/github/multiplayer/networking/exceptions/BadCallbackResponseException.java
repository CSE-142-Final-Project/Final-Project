package com.csefinalproject.github.multiplayer.networking.exceptions;

/**
 * This exception is thrown when the callback response is bad
 */
public class BadCallbackResponseException extends RuntimeException {
    /**
     * This constructor is used to create a new bad callback response exception
     * @param s the message
     */
    public BadCallbackResponseException(String s) {
        super(s);
    }
}
