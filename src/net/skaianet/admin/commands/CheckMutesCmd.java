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
import net.skaianet.utils.TimeUtils;

public class CheckMutesCmd implements CommandExecutor {
	private FileConfiguration CONFIG;

	public CheckMutesCmd(SNAdmin plugin) {
		this.CONFIG = plugin.getConfig();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!sender.hasPermission("skaianet.admin.check.mutes")) {
			sender.sendMessage(ChatColor.RED + SNAdmin.NO_PERMISSION);
			return true;
		}
		if (args.length < 1) {
			sender.sendMessage(ChatColor.RED + "Usage: " + cmd.getUsage());
			return true;
		}
		MuteInfo mute = SNAdminAPI.getMuteInfo(args[0]);
		//Check to make sure the player exists
		if (mute == null) {
			String notMutedMessage = this.CONFIG.getString("messages.check.notMuted","%victim% is not muted!").replaceAll("%victim%", args[0]);
			notMutedMessage = ChatUtils.colorize(notMutedMessage);
			sender.sendMessage(notMutedMessage);
			return true;
		}
		String senderMessage = "";
		long time;
		switch(mute.getType()) {
		case 1:
			time = mute.getEndTime() - System.currentTimeMillis();
			if (time <= 0L) {
				senderMessage = this.CONFIG.getString("messages.check.notMuted","%victim% is not muted!").replaceAll("%victim%", args[0]);
				break;
			}
			senderMessage = this.CONFIG.getString("messages.check.tempmute", "%victim% is muted for %time% - Reason: %reason%").replaceAll("%victim%", mute.getName()).replaceAll("%admin%", sender.getName()).replaceAll("%time%", TimeUtils.millisToString(time)).replaceAll("%reason%", mute.getReason());
			break;
		case 2:
			time = System.currentTimeMillis() - mute.getStartTime();
			senderMessage = this.CONFIG.getString("messages.check.mute", "%victim% was muted %time% ago - Reason: %reason%").replaceAll("%victim%", mute.getName()).replaceAll("%admin%", sender.getName()).replaceAll("%time%", TimeUtils.millisToString(time)).replaceAll("%reason%", mute.getReason());
			break;
		}
		senderMessage = ChatUtils.colorize(senderMessage);
		//Send info to sender
		sender.sendMessage(senderMessage);
		return true;
	}
}
