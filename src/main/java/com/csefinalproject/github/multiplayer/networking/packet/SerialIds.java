package com.csefinalproject.github.multiplayer.networking.packet;

/**
 * This class is used to store the serial ids of the various packets
 */
public class SerialIds {
    /**
     * Connection packets are used to establish a connection between the client and the server
     */
    public static final long CONNECTION_PACKET = 1L;
    /**
     * These packets are used to indicate that the connection attempt was successful
     */
    public static final long CONNECTION_SUCCESSFUL_PACKET = 2L;
    /**
     * These packets are used to keep the connection alive
     */
    public static final long KEEP_ALIVE_PACKET = 3L;
    /**
     * These packets are used to send chat messages
     */
    public static final long CHAT_PACKET = 4L;
    /**
     * These packets are used to send input data
     */
    public static final long INPUT_PACKET = 5L;
    /**
     * These packets are used to send position data
     */
    public static final long POSITION_PACKET = 6L;
    /**
     * These packets are used to send join requests
     */
    public static final long JOIN_REQUEST_PACKET = 7L;
    /**
     * These packets are used to tell clients a player has joined
     */
    public static final long PLAYER_JOINED_PACKET = 8L;
    /**
     * These packets are used to tell clients a player has left
     */
    public static final long PLAYER_LEFT_PACKET = 9L;
}
