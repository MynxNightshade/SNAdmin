package net.skaianet.utils;

import org.bukkit.ChatColor;

public final class ChatUtils {

	public static final String colorize(String str) {
		return ChatColor.translateAlternateColorCodes('&', str);
	}
	
	@Deprecated
	public static final String format(String str) {
		return str.replaceAll("&([0-9a-f])", "\u00A7$1");
	}
}
