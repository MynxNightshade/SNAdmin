package net.skaianet.admin.commands;

import net.skaianet.admin.SNAdmin;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class SNAdminCmd implements CommandExecutor {
	private SNAdmin PLUGIN;

	public SNAdminCmd(SNAdmin plugin) {
		this.PLUGIN = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length == 0) {
			if (!sender.hasPermission("skaianet.admin.version")) {
				sender.sendMessage(ChatColor.RED + SNAdmin.NO_PERMISSION);
				return true;
			}
			sender.sendMessage(ChatColor.GREEN + "SNAdmin v" + this.PLUGIN.getDescription().getVersion());
			return true;
		} else if (args[0].equalsIgnoreCase("reload")) {
			if (!sender.hasPermission("skaianet.admin.reload")) {
				sender.sendMessage(ChatColor.RED + SNAdmin.NO_PERMISSION);
				return true;
			}
			if (args.length > 2) {
				sender.sendMessage(ChatColor.RED + "Usage: " + cmd.getUsage());
				return true;
			}
			this.PLUGIN.reloadConfig();
			sender.sendMessage(ChatColor.GREEN + "SNAdmin reloaded!");
			return true;
		} else {
			sender.sendMessage(ChatColor.RED + "Usage: " + cmd.getUsage());
			return true;
		}
	}
}
