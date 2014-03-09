package net.skaianet.admin.api;

public class MuteInfo {
	private final String NAME, REASON;
	private final long MUTE_START_TIME, MUTE_END_TIME;
	private final int TYPE;

	public MuteInfo(String name, String reason, long startTime, long endTime, int type) {
		this.NAME = name;
		this.REASON = reason;
		this.MUTE_START_TIME = startTime;
		this.MUTE_END_TIME = endTime;
		this.TYPE = type;
	}
	
	public String getName() {
		return this.NAME;
	}
	
	public String getReason() {
		return this.REASON;
	}
	
	public int getType() {
		return this.TYPE;
	}
	
	public long getStartTime() {
		return this.MUTE_START_TIME;
	}
	
	public long getEndTime() {
		return this.MUTE_END_TIME;
	}
}
