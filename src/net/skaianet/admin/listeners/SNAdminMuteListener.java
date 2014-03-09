package net.skaianet.admin.listeners;

import net.skaianet.admin.SNAdmin;
import net.skaianet.admin.api.MuteInfo;
import net.skaianet.admin.api.SNAdminAPI;
import net.skaianet.utils.ChatUtils;
import net.skaianet.utils.TimeUtils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class SNAdminMuteListener implements Listener {
	private SNAdmin PLUGIN;
	private FileConfiguration CONFIG;

	public SNAdminMuteListener(SNAdmin plugin) {
		this.PLUGIN = plugin;
		this.CONFIG = plugin.getConfig();
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerChat(AsyncPlayerChatEvent e) {
		Player player = e.getPlayer();
		MuteInfo mute = SNAdminAPI.getMuteInfo(player.getName());
		if (mute == null) { return; }
		
		switch (mute.getType()) {
		case 1:
			//Check if user can override mutes
			if (player.hasPermission("skaianet.admin.override.mute")) {
				//Allow message
				this.PLUGIN.getLogger().info("Player " + e.getPlayer().getName() + " has Mute override permission... Allowing login.");
				e.setCancelled(false);
				return;
			}
			
			//Check Temp Mute Time
			if (mute.getEndTime() - System.currentTimeMillis() <= 0) {
				//Unmute the player
				SNAdminAPI.unmute(player.getName());
				e.setCancelled(false);
				return;
			}
			String endTime = TimeUtils.millisToString(mute.getEndTime() - System.currentTimeMillis());
			String tempMuteResponse = this.CONFIG.getString("messages.tempMute","You are muted for %time%").replaceAll("%time%", endTime);
			tempMuteResponse = ChatUtils.colorize(tempMuteResponse);
			player.sendMessage(tempMuteResponse);
			this.PLUGIN.getLogger().info("Player " + player.getName() + " was denied permission to talk. Reason: MuteType.TEMP");
			e.setCancelled(true);
			return;
		case 2:
			//Check if user can override mutes
			if (player.hasPermission("skaianet.admin.override.mute")) {
				//Allow message
				this.PLUGIN.getLogger().info("Player " + e.getPlayer().getName() + " has Mute override permission... Allowing login.");
				e.setCancelled(false);
				return;
			}
			
			String muteResponse = this.CONFIG.getString("messages.mute.response", "You are muted!");
			muteResponse = ChatUtils.colorize(muteResponse);
			this.PLUGIN.getLogger().info("Player " + player.getName() + " was denied permission to talk. Reason: MuteType.MUTE");
			player.sendMessage(muteResponse);
			e.setCancelled(true);
			return;
		}
	}
}
