package net.skaianet.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class StringUtils {
	private static final String IP_PATTERN = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

	public static final boolean isValidIp(String ip) {
		Matcher m = Pattern.compile(IP_PATTERN).matcher(ip);
		return m.matches();
	}
	
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
