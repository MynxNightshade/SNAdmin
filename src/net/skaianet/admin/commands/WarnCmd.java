package net.skaianet.admin.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import net.skaianet.admin.SNAdmin;
import net.skaianet.admin.api.PlayerInfo;
import net.skaianet.admin.api.SNAdminAPI;
import net.skaianet.utils.ChatUtils;
import net.skaianet.utils.StringUtils;
import net.skaianet.utils.TimeUtils;

public class WarnCmd implements CommandExecutor {
	private SNAdmin PLUGIN;
	private FileConfiguration CONFIG;

	public WarnCmd(SNAdmin plugin) {
		this.PLUGIN = plugin;
		this.CONFIG = plugin.getConfig();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!sender.hasPermission("skaianet.admin.warn")) {
			sender.sendMessage(ChatColor.RED + SNAdmin.NO_PERMISSION);
			return true;
		}
		if (args.length < 1) {
			sender.sendMessage(ChatColor.RED + "Usage: " + cmd.getUsage());
			return true;
		}
		String reason = SNAdmin.DEFAULT_REASON;
		PlayerInfo victim = SNAdminAPI.getPlayer(args[0]);
		//Check to make sure the player exists
		if (victim == null) {
			sender.sendMessage(ChatColor.RED + "Player does not exist!");
			return true;
		}
		//Parse reason
		if (args.length > 1) {
			reason = StringUtils.combineArray(args, 1, args.length);
		}
		String victimMessage = this.CONFIG.getString("messages.warn.victim", "You were warn by %admin% - Reason: %reason%").replaceAll("%admin%", sender.getName()).replaceAll("%reason%", reason);
		victimMessage = ChatUtils.colorize(victimMessage);
		String senderMessage = this.CONFIG.getString("messages.warn.sender", "You warned %victim% - Reason: %reason%").replaceAll("%victim%", victim.getName()).replaceAll("%reason%", reason);
		senderMessage = ChatUtils.colorize(senderMessage);
		sender.sendMessage(senderMessage);
		SNAdminAPI.sendMessage(victim.getName(), victimMessage);
		//Warn player
		SNAdminAPI.warn(victim.getName());
		
		//Check total warnings
		if (this.CONFIG.getBoolean("warnings.enableAutoBan", false)) {
			int warns = SNAdminAPI.getWarnInfo(victim.getName()).getTotalWarns();
			reason = this.CONFIG.getString("warnings.reason", "Max Warnings");
			
			if (warns >= this.CONFIG.getInt("warnings.maxWarns", 3)) {
				String banType = this.CONFIG.getString("warnings.banType");
				long time = TimeUtils.createFutureTime(this.CONFIG.getInt("warnings.time.length", 1), this.CONFIG.getString("warnings.time.type", "day"));
				String banMessage = this.CONFIG.getString("warnings.banMessage", "You are banned! - Reason: %reason%").replaceAll("%reason%", reason).replaceAll("%time%", TimeUtils.millisToString(time - System.currentTimeMillis())).replaceAll("%admin%", "Server");
				banMessage = ChatUtils.colorize(banMessage);
				SNAdminAPI.kick(victim.getName(), banMessage);
				
				if (banType.equalsIgnoreCase("ban")) {
					String globalMessage = this.CONFIG.getString("messages.ban.global", "%victim% was banned by %admin% - Reason: %reason%").replaceAll("%victim%", victim.getName()).replaceAll("%admin%", "Server").replaceAll("%reason%", reason);
					globalMessage = ChatUtils.colorize(globalMessage);
					this.PLUGIN.getServer().broadcastMessage(globalMessage);
					SNAdminAPI.ban(victim.getName(), reason);	
					return true;
				} else if (banType.equalsIgnoreCase("tempban")) {
					String globalMessage = this.CONFIG.getString("messages.tempban.global", "%victim% was banned for %time% by %admin% - Reason: %reason%").replaceAll("%victim%", victim.getName()).replaceAll("%admin%", "Server").replaceAll("%time%", TimeUtils.millisToString(time - System.currentTimeMillis())).replaceAll("%admin%", sender.getName()).replaceAll("%reason%", reason);
					globalMessage = ChatUtils.colorize(globalMessage);
					this.PLUGIN.getServer().broadcastMessage(globalMessage);
					SNAdminAPI.ban(victim.getName(), reason, time);	
					return true;
				} else if (banType.equalsIgnoreCase("ipban")) {
					String globalMessage = this.CONFIG.getString("messages.ipban.global", "%victim% was IP banned by %admin% - Reason: %reason%").replaceAll("%victim%", victim.getName()).replaceAll("%admin%", "Server").replaceAll("%reason%", reason);
					globalMessage = ChatUtils.colorize(globalMessage);
					this.PLUGIN.getServer().broadcastMessage(globalMessage);
					SNAdminAPI.ipBan(victim.getName(), victim.getIP(), reason);	
					return true;
				} else {
					sender.sendMessage(ChatColor.RED + "ERROR: AutoBanning is not setup correctly. Please consult your server administrator to resolve this.");
					return true;
				}
			}
		}
		return true;
	}
}
