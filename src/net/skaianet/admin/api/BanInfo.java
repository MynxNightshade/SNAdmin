package net.skaianet.admin.api;

public class BanInfo {
	private final String NAME, REASON, IP;
	private final long BAN_START_TIME, BAN_END_TIME;
	private final int TYPE;

	public BanInfo(String name, String ip, String reason, long startTime, long endTime, int type) {
		this.NAME = name;
		this.IP = ip;
		this.REASON = reason;
		this.BAN_START_TIME = startTime;
		this.BAN_END_TIME = endTime;
		this.TYPE = type;
	}
	
	public String getName() {
		return this.NAME;
	}
	
	public String getIP() {
		return this.IP;
	}
	
	public String getReason() {
		return this.REASON;
	}
	
	public int getType() {
		return this.TYPE;
	}
	
	public long getStartTime() {
		return this.BAN_START_TIME;
	}
	
	public long getEndTime() {
		return this.BAN_END_TIME;
	}
}
