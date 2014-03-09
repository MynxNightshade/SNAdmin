package net.skaianet.utils;

public final class StringUtils {

	public static final String combineArray(String[] str, int start, int end) {
		String oString = "";
		for (int i = start; i < end; i++) {
			oString += str[i] + " ";
		}
		return oString.trim();
	}
	
	public static final String replaceVariables(String str, String admin, String ip, String reason, String time, String victim) {
		return ChatUtils.colorize(str.replaceAll("%admin%", admin).replaceAll("%ip%", ip).replaceAll("%reason%", reason).replaceAll("%time%", time).replaceAll("%victim%", victim));
	}
}
