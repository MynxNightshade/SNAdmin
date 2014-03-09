package net.skaianet.admin.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import net.skaianet.admin.SNAdmin;
import net.skaianet.admin.api.SNAdminAPI;
import net.skaianet.admin.api.WarnInfo;
import net.skaianet.utils.ChatUtils;

public class ClearWarnsCmd implements CommandExecutor {
	private SNAdmin PLUGIN;
	private FileConfiguration CONFIG;

	public ClearWarnsCmd(SNAdmin plugin) {
		this.PLUGIN = plugin;
		this.CONFIG = plugin.getConfig();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!sender.hasPermission("skaianet.admin.warn.clear")) {
			sender.sendMessage(ChatColor.RED + SNAdmin.NO_PERMISSION);
			return true;
		}
		if (args.length < 1 && args.length > 3) {
			sender.sendMessage(ChatColor.RED + "Usage: " + cmd.getUsage());
			return true;
		}
		
		//Clear all warns
		if (args[0].equals("*")) {
			if (!sender.hasPermission("skaianet.admin.warn.clear.all")) {
				sender.sendMessage(ChatColor.RED + SNAdmin.NO_PERMISSION);
				return true;
			}
			String globalBanMessage = this.CONFIG.getString("messages.warn.clearAll", "All warnings have been cleared by %admin%!").replaceAll("%admin%", sender.getName());
			globalBanMessage = ChatUtils.colorize(globalBanMessage);
			this.PLUGIN.getServer().broadcastMessage(globalBanMessage);
			SNAdminAPI.clearAllWarns();
			return true;
		}
		
		WarnInfo warns = SNAdminAPI.getWarnInfo(args[0]);
		//Check to make sure the player exists
		if (warns == null) {
			sender.sendMessage(ChatColor.RED + "Player does not exist or doesn't have any warnings!");
			return true;
		}
		//Clear warnings for a single player
		String victimMessage = this.CONFIG.getString("messages.warn.clear", "All of your warnings have been cleared by %admin%!").replaceAll("%admin%", sender.getName());
		victimMessage = ChatUtils.colorize(victimMessage);
		SNAdminAPI.sendMessage(warns.getName(), victimMessage);
		SNAdminAPI.clearWarns(warns.getName());
		return true;
	}
}
