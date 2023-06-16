package com.csefinalproject.github.multiplayer.networking.packet;

import com.csefinalproject.github.multiplayer.networking.IPeer;

/**
 * This packet is used to tell the clients that a player has joined
 */
public class InputDataPacket extends Packet{
    /**
     * For serialization
     */
    public static final long serialVersionUID = SerialIds.INPUT_PACKET;
    private final boolean forward;
    private final boolean backward;
    private final boolean left;
    private final boolean right;
    private final double degrees;

    /**
     * This constructor is used to create a new player input packet
     * @param peerToSendFrom the peer to send from
     * @param forward whether the player is holding the forward key
     * @param backward whether the player is holding the backward key
     * @param left whether the player is holding the left key
     * @param right whether the player is holding the right key
     * @param degrees the degrees the player is facing
     */
    public InputDataPacket(IPeer peerToSendFrom, boolean forward, boolean backward, boolean left, boolean right, double degrees) {
        super(peerToSendFrom);
        this.forward = forward;
        this.backward = backward;
        this.left = left;
        this.right = right;
        this.degrees = degrees;
    }

    /**
     * This method is used to get the state of the forward key
     * @return the state of the forward key
     */
    public boolean isForward() {
        return forward;
    }

    /**
     * This method is used to get the state of the backward key
     * @return the state of the backward key
     */
    public boolean isBackward() {
        return backward;
    }

    /**
     * This method is used to get the state of the left key
     * @return the state of the left key
     */
    public boolean isLeft() {
        return left;
    }

    /**
     * This method is used to get the state of the right key
     * @return the state of the right key
     */
    public boolean isRight() {
        return right;
    }

    /**
     * This method is used to get the degrees the player is facing
     * @return the degrees the player is facing
     */
    public double getDegrees() {
        return degrees;
    }
}
