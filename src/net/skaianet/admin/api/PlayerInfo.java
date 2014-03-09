package net.skaianet.admin.api;

public class PlayerInfo  {
	private final String NAME, LAST_LOGIN_IP;
	private final long LAST_LOGIN_TIME;

	public PlayerInfo(String name, String lastLoginIp, long lastLoginTime) {
		this.NAME = name;
		this.LAST_LOGIN_IP = lastLoginIp;
		this.LAST_LOGIN_TIME = lastLoginTime;
	}
	
	public PlayerInfo(String player, String lastLoginIp) {
		this(player, lastLoginIp, System.currentTimeMillis());
	}
	
	public PlayerInfo(String player) {
		this(player, "0.0.0.0", System.currentTimeMillis());
	}
	
	public String getName() {
		return this.NAME;
	}
	
	public String getIP() {
		return this.LAST_LOGIN_IP;
	}
	
	public long getLastLogin() {
		return this.LAST_LOGIN_TIME;
	}
}
