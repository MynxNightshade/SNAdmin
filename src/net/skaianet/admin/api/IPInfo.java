package net.skaianet.admin.api;

public class IPInfo {
	private final String IP, REASON;

	public IPInfo(String ip, String reason) {
		this.IP = ip;
		this.REASON = reason;
	}
	
	public String getIP() {
		return this.IP;
	}
	
	public String getReason() {
		return this.REASON;
	}
	
	public boolean isBanned() {
		return SNAdminAPI.isIpBanned(this.IP);
	}
}
