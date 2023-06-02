package com.csefinalproject.github.multiplayer.networking.packet;

import com.csefinalproject.github.multiplayer.networking.IPeer;

public class InputDataPacket extends Packet{
    public static final long serialVersionUID = SerialIds.INPUT_PACKET;
    final boolean forward;
    final boolean backward;
    final boolean left;
    final boolean right;
    final double degrees;
    public InputDataPacket(IPeer peerToSendFrom, boolean forward, boolean backward, boolean left, boolean right, double degrees) {
        super(peerToSendFrom);
        this.forward = forward;
        this.backward = backward;
        this.left = left;
        this.right = right;
        this.degrees = degrees;
    }
}
