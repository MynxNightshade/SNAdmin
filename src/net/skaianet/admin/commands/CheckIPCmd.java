package net.skaianet.admin.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import net.skaianet.admin.SNAdmin;
import net.skaianet.admin.api.IPInfo;
import net.skaianet.admin.api.SNAdminAPI;
import net.skaianet.utils.ChatUtils;
import net.skaianet.utils.StringUtils;

public class CheckIPCmd implements CommandExecutor {
	private FileConfiguration CONFIG;

	public CheckIPCmd(SNAdmin plugin) {
		this.CONFIG = plugin.getConfig();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!sender.hasPermission("skaianet.admin.check.mutes")) {
			sender.sendMessage(ChatColor.RED + SNAdmin.NO_PERMISSION);
			return true;
		}
		if (args.length < 1) {
			sender.sendMessage(ChatColor.RED + "Usage: " + cmd.getUsage());
			return true;
		}
		//Check for valid IP's
		if (!StringUtils.isValidIp(args[0])) {
			sender.sendMessage(ChatColor.RED + "Please enter a valid IP address!");
			return true;
		}
		
		IPInfo ip = SNAdminAPI.getIPInfo(args[0]);
		//Check to make sure the player exists
		if (ip == null) {
			String cleanMessage = this.CONFIG.getString("messages.check.cleanIP","%ip% is not banned from the ser!").replaceAll("%ip%", args[0]);
			cleanMessage = ChatUtils.colorize(cleanMessage);
			sender.sendMessage(cleanMessage);
			return true;
		}
		
		String senderMessage = "";
		if(ip.isBanned()) {
			senderMessage = this.CONFIG.getString("messages.check.bannedIP","%ip% is banned from the server - Reason: %reason%").replaceAll("%ip%", ip.getIP()).replaceAll("%reason%", ip.getReason());
		} else {
			senderMessage = this.CONFIG.getString("messages.check.cleanIP","%ip% is not banned from the server!").replaceAll("%ip%", ip.getIP());
		}
		//Send info to sender
		senderMessage = ChatUtils.colorize(senderMessage);
		sender.sendMessage(senderMessage);
		return true;
	}
}
