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
		if (args[0].equalsIgnoreCase("reload")) {
			if (!sender.hasPermission("skaianet.admin.reload")) {
				sender.sendMessage(ChatColor.RED + SNAdmin.NO_PERMISSION);
				return true;
			}
			if (args.length > 2) {
				sender.sendMessage(ChatColor.RED + "Usage: " + cmd.getUsage() + " reload");
				return true;
			}
			this.PLUGIN.reloadConfig();
			this.PLUGIN.saveConfig();
			sender.sendMessage(ChatColor.GREEN + "SNAdmin reloaded!");
			return true;
		}
		if (args[0].equalsIgnoreCase("version") || args[0].equalsIgnoreCase("ver")) {
			if (!sender.hasPermission("skaianet.admin.version")) {
				sender.sendMessage(ChatColor.RED + SNAdmin.NO_PERMISSION);
				return true;
			}
			if (args.length > 2) {
				sender.sendMessage(ChatColor.RED + "Usage: " + cmd.getUsage() + " version");
				return true;
			}
			sender.sendMessage(ChatColor.GREEN + "SNAdmin v" + this.PLUGIN.getDescription().getVersion());
			return true;
		}
		return true;
	}
}
