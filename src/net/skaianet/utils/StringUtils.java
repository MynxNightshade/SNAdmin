package net.skaianet.utils;

public final class StringUtils {

	public static final String combineArray(String[] str, int start, int end) {
		String oString = "";
		for (int i = start; i < end; i++) {
			oString += str[i] + " ";
		}
		return oString.trim();
	}
}
