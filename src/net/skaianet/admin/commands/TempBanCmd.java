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

public class TempBanCmd implements CommandExecutor {
	private SNAdmin PLUGIN;
	private FileConfiguration CONFIG;

	public TempBanCmd(SNAdmin plugin) {
		this.PLUGIN = plugin;
		this.CONFIG = plugin.getConfig();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!sender.hasPermission("skaianet.admin.ban.temp")) {
			sender.sendMessage(ChatColor.RED + SNAdmin.NO_PERMISSION);
			return true;
		}
		if (args.length < 1) {
			sender.sendMessage(ChatColor.RED + "Usage: " + cmd.getUsage());
			return true;
		}
		long time = 0L;
		String reason = SNAdmin.DEFAULT_REASON;
		PlayerInfo victim = SNAdminAPI.getPlayer(args[0]);
		//Check to make sure the player exists
		if (victim == null) {
			sender.sendMessage(ChatColor.RED + "Player does not exist!");
			return true;
		}
		//Parse time
		try {
			time = TimeUtils.createFutureTime(Integer.parseInt(args[1]), args[2]);
		} catch (IllegalArgumentException ex) {
			sender.sendMessage(ChatColor.RED + "Invalid time interval. Please use s,min,h,m or y.");
			return true;
		} catch (Exception ex) {
			sender.sendMessage(ChatColor.RED + "Invalid time interval. Please use a valid integer.");
			return true;
		}
		
		//Parse reason
		if (args.length > 3) {
			reason = StringUtils.combineArray(args, 3, args.length);
		}
		String globalMessage = this.CONFIG.getString("messages.tempban.global", "%victim% was banned for %time% by %admin% - Reason: %reason%").replaceAll("%victim%", victim.getName()).replaceAll("%admin%", sender.getName()).replaceAll("%time%", TimeUtils.millisToString(time - System.currentTimeMillis())).replaceAll("%admin%", sender.getName()).replaceAll("%reason%", reason);
		globalMessage = ChatUtils.colorize(globalMessage);
		String victimMessage = this.CONFIG.getString("messages.tempban.victim", "You were banned for %time% by %admin% - Reason: %reason%").replaceAll("%time%", TimeUtils.millisToString(time - System.currentTimeMillis())).replaceAll("%admin%", sender.getName()).replaceAll("%reason%", reason);
		victimMessage = ChatUtils.colorize(victimMessage);
		this.PLUGIN.getServer().broadcastMessage(globalMessage);
		//Kick, then ban the player
		SNAdminAPI.kick(victim.getName(), victimMessage);
		SNAdminAPI.ban(victim.getName(), reason, time);
		return true;
	}
}
