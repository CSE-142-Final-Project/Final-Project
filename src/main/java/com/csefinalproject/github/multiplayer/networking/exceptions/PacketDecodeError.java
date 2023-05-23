package com.csefinalproject.github.multiplayer.networking.exceptions;

public class PacketDecodeError extends Exception{
    public PacketDecodeError(Exception e) {
        super(e);
    }
    public PacketDecodeError(String s) {
        super(s);
    }
}
