package com.csefinalproject.github.multiplayer.networking.packet;

import com.csefinalproject.github.multiplayer.networking.IPeer;

import java.awt.*;

/**
 * This packet is used to send a position to the clients
 */
public class PositionPacket extends Packet {
	/**
	 * For serialization
	 */
	public static final long serialVersionUID = SerialIds.POSITION_PACKET;
	/**
	 * The position to send
	 */
	// TODO: check to see whether this takes more memory than 2 floats
	final Point position;
	/**
	 * The client id of the player who sent this packet
	 */
	final short clientId;

	/**
	 * This constructor is used to create a new position packet
	 * @param peerToSendFrom the peer to send from
	 * @param position the position to send
	 * @param clientId the client id of the player who sent this packet
	 */
	public PositionPacket(IPeer peerToSendFrom, Point position, short clientId) {
		super(peerToSendFrom);
		this.position = position;
		this.clientId = clientId;
	}

	/**
	 * This method is used to get the position
	 * @return the position
	 */
	public Point getPosition() {
		return position;
	}

	/**
	 * This method is used to get the client id
	 * @return the client id
	 */
	public short getClientId() {
		return clientId;
	}
}
