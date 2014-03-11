package net.skaianet.admin.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import net.skaianet.admin.SNAdmin;
import net.skaianet.admin.api.MuteInfo;
import net.skaianet.admin.api.SNAdminAPI;
import net.skaianet.utils.ChatUtils;

public class UnMuteCmd implements CommandExecutor {
	private SNAdmin PLUGIN;
	private FileConfiguration CONFIG;

	public UnMuteCmd(SNAdmin plugin) {
		this.PLUGIN = plugin;
		this.CONFIG = plugin.getConfig();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!sender.hasPermission("skaianet.admin.unmute")) {
			sender.sendMessage(ChatColor.RED + SNAdmin.NO_PERMISSION);
			return true;
		}
		if (args.length < 1 && args.length > 3) {
			sender.sendMessage(ChatColor.RED + "Usage: " + cmd.getUsage());
			return true;
		}
		MuteInfo mute = SNAdminAPI.getMuteInfo(args[0]);
		//Check to make sure the player exists
		if (mute == null) {
			String notMutedMessage = this.CONFIG.getString("messages.unmute.notMuted","%victim% is not muted!").replaceAll("%victim%", args[0]);
			notMutedMessage = ChatUtils.colorize(notMutedMessage);
			sender.sendMessage(notMutedMessage);
			return true;
		}
		
		String globalMessage = this.CONFIG.getString("messages.unmute.global", "%victim% was unmuted by %admin%!").replaceAll("%victim%", mute.getName()).replaceAll("%admin%", sender.getName());
		globalMessage = ChatUtils.colorize(globalMessage);
		String victimMessage = this.CONFIG.getString("messages.unmute.victim", "You were unmuted by %admin%!").replaceAll("%admin%", sender.getName());
		victimMessage = ChatUtils.colorize(victimMessage);
		this.PLUGIN.getServer().broadcastMessage(globalMessage);
		SNAdminAPI.sendMessage(mute.getName(), victimMessage);
		//Unmute the player
		SNAdminAPI.unmute(mute.getName());
		return true;
	}
}
