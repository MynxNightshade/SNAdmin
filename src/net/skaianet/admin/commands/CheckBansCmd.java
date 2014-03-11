package net.skaianet.admin.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import net.skaianet.admin.SNAdmin;
import net.skaianet.admin.api.BanInfo;
import net.skaianet.admin.api.SNAdminAPI;
import net.skaianet.utils.ChatUtils;
import net.skaianet.utils.TimeUtils;

public class CheckBansCmd implements CommandExecutor {
	private FileConfiguration CONFIG;

	public CheckBansCmd(SNAdmin plugin) {
		this.CONFIG = plugin.getConfig();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!sender.hasPermission("skaianet.admin.check.bans")) {
			sender.sendMessage(ChatColor.RED + SNAdmin.NO_PERMISSION);
			return true;
		}
		if (args.length < 1) {
			sender.sendMessage(ChatColor.RED + "Usage: " + cmd.getUsage());
			return true;
		}
		BanInfo ban = SNAdminAPI.getBanInfo(args[0]);
		//Check to make sure the player exists
		if (ban == null) {
			String notBannedMessage = this.CONFIG.getString("messages.check.notBanned","%victim% is not banned!").replaceAll("%victim%", args[0]);
			notBannedMessage = ChatUtils.colorize(notBannedMessage);
			sender.sendMessage(notBannedMessage);
			return true;
		}
		String senderMessage = "";
		long time;
		switch(ban.getType()) {
		case 1:
			time = ban.getEndTime() - System.currentTimeMillis();
			if (time <= 0L) {
				senderMessage = this.CONFIG.getString("messages.check.notBanned","%victim% is not banned!").replaceAll("%victim%", args[0]);
				break;
			}
			senderMessage = this.CONFIG.getString("messages.check.tempban", "%victim% is banned for %time% - Reason: %reason%").replaceAll("%victim%", ban.getName()).replaceAll("%admin%", sender.getName()).replaceAll("%time%", TimeUtils.millisToString(time)).replaceAll("%reason%", ban.getReason());
			break;
		case 2:
			time = System.currentTimeMillis() - ban.getStartTime();
			senderMessage = this.CONFIG.getString("messages.check.ban", "%victim% was banned %time% ago - Reason: %reason%").replaceAll("%victim%", ban.getName()).replaceAll("%admin%", sender.getName()).replaceAll("%time%", TimeUtils.millisToString(time)).replaceAll("%reason%", ban.getReason());
			break;
		case 3:
			time = System.currentTimeMillis() - ban.getStartTime();
			senderMessage = this.CONFIG.getString("messages.check.ipban", "%victim% was IP banned %time% ago - Reason: %reason%").replaceAll("%victim%", ban.getName()).replaceAll("%admin%", sender.getName()).replaceAll("%time%", TimeUtils.millisToString(time)).replaceAll("%reason%", ban.getReason());
			break;
		}
		senderMessage = ChatUtils.colorize(senderMessage);
		//Send info to sender
		sender.sendMessage(senderMessage);
		return true;
	}
}
