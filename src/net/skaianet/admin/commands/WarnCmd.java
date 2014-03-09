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
		SNAdminAPI.sendMessage(victim.getName(), victimMessage);
		//Warn player
		SNAdminAPI.warn(victim.getName());
		
		//Check total warnings
		if (this.CONFIG.getBoolean("warnings.enableAutoBan")) {
			int warns = SNAdminAPI.getWarnInfo(victim.getName()).getTotalWarns();
			reason = this.CONFIG.getString("warnings.reason");
			
			if (warns >= this.CONFIG.getInt("warnings.maxWarns")) {
				String banType = this.CONFIG.getString("warnings.banType");
				SNAdminAPI.kick(victim.getName(), reason);
				
				if (banType.equalsIgnoreCase("ban")) {
					String globalMessage = this.CONFIG.getString("messages.ban.global", "%victim% was banned by %admin% - Reason: %reason%").replaceAll("%victim%", victim.getName()).replaceAll("%admin%", "Server").replaceAll("%reason%", reason);
					globalMessage = ChatUtils.colorize(globalMessage);
					this.PLUGIN.getServer().broadcastMessage(globalMessage);
					SNAdminAPI.ban(victim.getName(), reason);	
					return true;
				} else if (banType.equalsIgnoreCase("tempban")) {
					long time = TimeUtils.createFutureTime(this.CONFIG.getInt("warnings.time.amount"), this.CONFIG.getString("warnings.time.interval"));
					String globalMessage = this.CONFIG.getString("messages.tempBan.global", "%victim% was banned for %time% by %admin% - Reason: %reason%").replaceAll("%victim%", victim.getName()).replaceAll("%admin%", "Server").replaceAll("%time%", TimeUtils.millisToString(time - System.currentTimeMillis())).replaceAll("%admin%", sender.getName()).replaceAll("%reason%", reason);
					globalMessage = ChatUtils.colorize(globalMessage);
					this.PLUGIN.getServer().broadcastMessage(globalMessage);
					SNAdminAPI.ban(victim.getName(), reason, time);	
					return true;
				} else if (banType.equalsIgnoreCase("ipban")) {
					String globalMessage = this.CONFIG.getString("messages.ipBan.global", "%victim% was IP banned by %admin% - Reason: %reason%").replaceAll("%victim%", victim.getName()).replaceAll("%admin%", "Server").replaceAll("%reason%", reason);
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
