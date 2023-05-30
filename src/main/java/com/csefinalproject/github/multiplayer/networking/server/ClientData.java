package com.csefinalproject.github.multiplayer.networking.server;

import java.util.HashMap;

public class ClientData {
	private final String ip;
	private final int port;
	private final String username;
	private final short id;
	private static short nextClientId = 0;
	private long lastKeepAliveTime;
	private static final HashMap<String,ClientData> ipPortToClientData = new HashMap<>();
	public ClientData(String ip, int port, String username) {
		this.ip = ip;
		this.port = port;
		this.username = username;
		id = ++nextClientId;
		lastKeepAliveTime = System.currentTimeMillis();
		ipPortToClientData.put(ip+":"+port,this);
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
	protected static ClientData getFromIpAndPort(String ip, int port) {
		return ipPortToClientData.get(ip+":"+port);
	}
	protected static ClientData getFromIpAndPort(String ipAndPort) {
		return ipPortToClientData.get(ipAndPort);
	}
}
