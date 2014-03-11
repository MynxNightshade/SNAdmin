package net.skaianet.admin.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import net.skaianet.admin.SNAdmin;
import net.skaianet.admin.api.PlayerInfo;
import net.skaianet.admin.api.SNAdminAPI;
import net.skaianet.admin.types.MuteType;
import net.skaianet.utils.ChatUtils;
import net.skaianet.utils.StringUtils;

public class MuteCmd implements CommandExecutor {
	private SNAdmin PLUGIN;
	private FileConfiguration CONFIG;

	public MuteCmd(SNAdmin plugin) {
		this.PLUGIN = plugin;
		this.CONFIG = plugin.getConfig();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!sender.hasPermission("skaianet.admin.mute")) {
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
		String globalMessage = this.CONFIG.getString("messages.mute.global", "%victim% was muted by %admin% - Reason: %reason%").replaceAll("%victim%", victim.getName()).replaceAll("%admin%", sender.getName()).replaceAll("%reason%", reason);
		globalMessage = ChatUtils.colorize(globalMessage);
		String victimMessage = this.CONFIG.getString("messages.mute.victim", "You were muted by %admin% - Reason: %reason%").replaceAll("%admin%", sender.getName()).replaceAll("%reason%", reason);
		victimMessage = ChatUtils.colorize(victimMessage);
		this.PLUGIN.getServer().broadcastMessage(globalMessage);
		SNAdminAPI.sendMessage(victim.getName(), victimMessage);
		//Mute the player
		SNAdminAPI.mute(victim.getName(), reason, -1L, MuteType.MUTE);
		return true;
	}
}
