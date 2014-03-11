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

public class CheckWarnsCmd implements CommandExecutor {
	private FileConfiguration CONFIG;

	public CheckWarnsCmd(SNAdmin plugin) {
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
		WarnInfo warns = SNAdminAPI.getWarnInfo(args[0]);
		//Check to make sure the player exists
		if (warns == null) {
			String noWarnsMessage = this.CONFIG.getString("messages.check.noWarns","%victim% has no warnings!").replaceAll("%victim%", args[0]);
			noWarnsMessage = ChatUtils.colorize(noWarnsMessage);
			sender.sendMessage(noWarnsMessage);
			return true;
		}
		String senderMessage = "";
		int totalWarns = warns.getTotalWarns();
		if (totalWarns == 0) {
			senderMessage = this.CONFIG.getString("messages.check.noWarns","%victim% has no warnings!").replaceAll("%victim%", warns.getName());
		} else if (totalWarns == 1) {
			senderMessage = this.CONFIG.getString("messages.check.warn", "%victim% has %warns% warning").replaceAll("%victim%", warns.getName()).replaceAll("%warns%", "" + totalWarns);
		} else {
			senderMessage = this.CONFIG.getString("messages.check.warns", "%victim% has %warns% warnings").replaceAll("%victim%", warns.getName()).replaceAll("%warns%", "" + totalWarns);
		}
		senderMessage = ChatUtils.colorize(senderMessage);
		//Send info to sender
		sender.sendMessage(senderMessage);
		return true;
	}
}
