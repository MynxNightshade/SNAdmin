package net.skaianet.admin.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import net.skaianet.admin.SNAdmin;
import net.skaianet.utils.ChatUtils;
import net.skaianet.utils.StringUtils;

public class KickCmd implements CommandExecutor {
	private SNAdmin PLUGIN;
	private FileConfiguration CONFIG;

	public KickCmd(SNAdmin plugin) {
		this.PLUGIN = plugin;
		this.CONFIG = plugin.getConfig();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!sender.hasPermission("skaianet.admin.kick")) {
			sender.sendMessage(ChatColor.RED + SNAdmin.NO_PERMISSION);
			return true;
		}
		if (args.length < 1) {
			sender.sendMessage(ChatColor.RED + "Usage: " + cmd.getUsage());
			return true;
		}
		String reason = SNAdmin.DEFAULT_REASON;
		//Parse reason
		if (args.length > 1) {
			reason = StringUtils.combineArray(args, 1, args.length);
		}
		
		//Kick all players
		if (args[0].equals("*")) {
			if (!sender.hasPermission("skaianet.admin.kick.all")) {
				sender.sendMessage(ChatColor.RED + SNAdmin.NO_PERMISSION);
				return true;
			}
			String globalMessage = this.CONFIG.getString("messages.kick.all", "Everyone was kicked by %admin% - Reason: %reason%").replaceAll("%admin%", sender.getName()).replaceAll("%reason%", reason);
			globalMessage = ChatUtils.colorize(globalMessage);
			//Kick all players
			for (Player p : Bukkit.getServer().getOnlinePlayers()) {
				p.kickPlayer(globalMessage);
			}
			return true;
		}

		Player victim = Bukkit.getServer().getPlayer(args[0]);
		//Check to make sure the player exists
		if (victim == null) {
			sender.sendMessage(ChatColor.RED + "Player must be online!");
			return true;
		}
		//Kick single player
		String globalMessage = this.CONFIG.getString("messages.kick.global", "%victim% was kicked by %admin% - Reason: %reason%").replaceAll("%victim%", victim.getName()).replaceAll("%admin%", sender.getName()).replaceAll("%reason%", reason);
		globalMessage = ChatUtils.colorize(globalMessage);
		String victimMessage = this.CONFIG.getString("messages.kick.victim", "You were kicked by %admin% - Reason: %reason%").replaceAll("%admin%", sender.getName()).replaceAll("%reason%", reason);
		victimMessage = ChatUtils.colorize(victimMessage);
		this.PLUGIN.getServer().broadcastMessage(globalMessage);
		//Kick the player
		victim.kickPlayer(victimMessage);
		return true;
	}
}
