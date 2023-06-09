package com.csefinalproject.github.multiplayer.networking.packet;

import com.csefinalproject.github.multiplayer.networking.IPeer;

import java.awt.*;

public class PositionPacket extends Packet {
	public static final long serialVersionUID = SerialIds.POSITION_PACKET;
	final Point position;

	public PositionPacket(IPeer peerToSendFrom, Point position) {
		super(peerToSendFrom);
		this.position = position;
	}

	public Point getPosition() {
		return position;
	}
}
