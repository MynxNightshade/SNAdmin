package net.skaianet.admin.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import net.skaianet.admin.SNAdmin;
import net.skaianet.admin.api.BanInfo;
import net.skaianet.admin.api.SNAdminAPI;
import net.skaianet.admin.types.BanType;
import net.skaianet.utils.ChatUtils;

public class UnBanCmd implements CommandExecutor {
	private SNAdmin PLUGIN;
	private FileConfiguration CONFIG;

	public UnBanCmd(SNAdmin plugin) {
		this.PLUGIN = plugin;
		this.CONFIG = plugin.getConfig();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!sender.hasPermission("skaianet.admin.unban")) {
			sender.sendMessage(ChatColor.RED + SNAdmin.NO_PERMISSION);
			return true;
		}
		if (args.length < 1 && args.length > 3) {
			sender.sendMessage(ChatColor.RED + "Usage: " + cmd.getUsage());
			return true;
		}
		BanInfo ban = SNAdminAPI.getBanInfo(args[0]);
		//Check to make sure the player exists
		if (ban == null) {
			String notBannedMessage = this.CONFIG.getString("messages.unban.notBanned","%victim% is not banned!").replaceAll("%victim%", args[0]);
			notBannedMessage = ChatUtils.colorize(notBannedMessage);
			sender.sendMessage(notBannedMessage);
			return true;
		}
		
		if (ban.getType() == BanType.IP) {
			if (!sender.hasPermission("skaianet.admin.unban.ip")) {
				sender.sendMessage(ChatColor.RED + "You do not have permission to unban players who are IP banned.");
				return true;
			}
		}
		
		String globalMessage = this.CONFIG.getString("messages.unban.global", "%victim% was unbanned by %admin%!").replaceAll("%victim%", ban.getName()).replaceAll("%admin%", sender.getName());
		globalMessage = ChatUtils.colorize(globalMessage);
		this.PLUGIN.getServer().broadcastMessage(globalMessage);
		//Unban the player
		SNAdminAPI.unban(ban.getName());
		SNAdminAPI.unbanIp(ban.getIP());
		return true;
	}
}
