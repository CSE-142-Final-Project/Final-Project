package com.csefinalproject.github.multiplayer.networking.packet;

import com.csefinalproject.github.multiplayer.networking.IPeer;

import java.awt.*;

public class PositionPacket extends Packet {
	public static final long serialVersionUID = SerialIds.POSITION_PACKET;
	final Point position;
	final short clientId;

	public PositionPacket(IPeer peerToSendFrom, Point position, short clientId) {
		super(peerToSendFrom);
		this.position = position;
		this.clientId = clientId;
	}

	public Point getPosition() {
		return position;
	}

	public short getClientId() {
		return clientId;
	}
}
