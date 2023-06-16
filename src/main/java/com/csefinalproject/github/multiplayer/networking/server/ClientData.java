package com.csefinalproject.github.multiplayer.networking.server;

import java.util.HashMap;

/**
 * This class is used to store data about a connected client
 * It stores information about the various clients
 * It also contains a helper method that allows a user to find a client based on their ip and port
 */
public class ClientData {
	private final String ip;
	private final int port;
	private final String username;
	private final short id;
	private static short nextClientId = 0;
	private long lastKeepAliveTime;
	private static final HashMap<String,ClientData> ipPortToClientData = new HashMap<>();

	/**
	 * This constructor is used to create a new client data object.
	 * @param ip ip of the client
	 * @param port port of the client
	 * @param username username of the client
	 */
	public ClientData(String ip, int port, String username) {
		this.ip = ip;
		this.port = port;
		this.username = username;
		this.id = ++nextClientId;
		this.lastKeepAliveTime = System.currentTimeMillis();
		ipPortToClientData.put(ip+":"+port,this);
	}

	/**
	 * This method is used to get the ip of the client
	 * @return the ip of the client
	 */
	public String getIP() {
		return ip;
	}

	/**
	 * This method is used to get the port of the client
	 * @return the port of the client
	 */
	public int getPort() {
		return port;
	}

	/**
	 * This method is used to get the username of the client
	 * @return the username of the client
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * This method is used to get the id of the client
	 * @return the id of the client
	 */
	public short getClientID() {
		return id;
	}

	/**
	 * This method is used to get the last time a keep alive packet was received from the client
	 * @return the last time a keep alive packet was received from the client
	 */
	public long getLastReceivedPacketTime() {
		return lastKeepAliveTime;
	}

	/**
	 * This method is used to set the last time a keep alive packet was received from the client
	 * @param time the last time a keep alive packet was received from the client
	 */
	protected void setLastReceivedPacketTime(long time) {
		lastKeepAliveTime = time;
	}

	/**
	 * This method is used to get a client data object from an ip and port. The client data object must already have been created. If it hasn't been created, this method will return null
	 * @param ip the ip of the client
	 * @param port the port of the client
	 * @return the client data object
	 */
	protected static ClientData getFromIpAndPort(String ip, int port) {
		return ipPortToClientData.get(ip+":"+port);
	}

	/**
	 * This method returns a string representation of the client data object in the format of
	 * ClientData{ip='ip', port=port, username='username', id=id, lastKeepAliveTime=lastKeepAliveTime}
	 * @return a string representation of the client data object
	 */
	@Override
	public String toString() {
		return "ClientData{" +
				"ip='" + ip + '\'' +
				", port=" + port +
				", username='" + username + '\'' +
				", id=" + id +
				", lastKeepAliveTime=" + lastKeepAliveTime +
				'}';
	}
}
