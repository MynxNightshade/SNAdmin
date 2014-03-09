package net.skaianet.admin.api;

public class WarnInfo {
	private final String NAME;
	private final int WARNS;

	public WarnInfo(String name, int warns) {
		this.NAME = name;
		this.WARNS = warns;
	}
	
	public String getName() {
		return this.NAME;
	}
	
	public int getTotalWarns() {
		return this.WARNS;
	}
}
