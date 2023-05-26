package com.csefinalproject.github.multiplayer.networking.server;

public class ClientData {
	String ip;
	int port;
	String username;
	short id;
	static short nextClientId = 0;
	long lastKeepAliveTime;
	public ClientData(String ip, int port, String username) {
		this.ip = ip;
		this.port = port;
		this.username = username;
		id = ++nextClientId;
		lastKeepAliveTime = System.currentTimeMillis();
	}

	public String getIP() {
		return ip;
	}

	public int getPort() {
		return port;
	}

	public String getUsername() {
		return username;
	}

	public short getClientID() {
		return id;
	}

	public long getLastReceivedPacketTime() {
		return lastKeepAliveTime;
	}
	protected void setLastReceivedPacketTime(long time) {
		lastKeepAliveTime = time;
	}
}
