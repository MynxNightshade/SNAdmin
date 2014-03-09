package net.skaianet.admin.listeners;

import net.skaianet.admin.SNAdmin;
import net.skaianet.admin.api.BanInfo;
import net.skaianet.admin.api.SNAdminAPI;
import net.skaianet.utils.ChatUtils;
import net.skaianet.utils.TimeUtils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class SNAdminBanListener implements Listener {
	private SNAdmin PLUGIN;
	private FileConfiguration CONFIG;

	public SNAdminBanListener(SNAdmin plugin) {
		this.PLUGIN = plugin;
		this.CONFIG = plugin.getConfig();
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerLogin(PlayerLoginEvent e) {
		Player player = e.getPlayer();
		BanInfo ban = SNAdminAPI.getBanInfo(player.getName());
		String ip = e.getAddress().toString().replaceAll("/", "");
		if (ban == null) { return; }
		
		//Check for IP Bans first
		if(SNAdminAPI.isIpBanned(ip)) {
			//Check if user can override bans
			if (player.hasPermission("skaianet.admin.override.ipban")) {
				//Allow user through
				this.PLUGIN.getLogger().info("Player " + e.getPlayer().getName() + " has IP Ban override permission... Allowing login.");
				e.allow();
				return;
			}

			//Deny IP Bans
			String ipBanResponse = this.CONFIG.getString("messages.ipban","Your IP (%ip%) is banned from this server!").replaceAll("%ip%", ip);
			ipBanResponse = ChatUtils.colorize(ipBanResponse);
			this.PLUGIN.getLogger().info("Player " + player.getName() + " was denied permission to login. Reason: BanType.IP");
			e.disallow(PlayerLoginEvent.Result.KICK_BANNED, ipBanResponse);
			return;
		}

		//Check if user can override bans
		if (ban.getType() > 0) { 
			if (player.hasPermission("skaianet.admin.override.ban")) {
				//Allow user through
				this.PLUGIN.getLogger().info("Player " + e.getPlayer().getName() + " has Ban override permission... Allowing login.");
				e.allow();
				return;
			}
		}
		
		//Check bans
		switch (ban.getType()) {
		case 1:
			//Check Temp Ban Time
			if (ban.getEndTime() - System.currentTimeMillis() <= 0) {
				//Clear ban and allow login if time has expired
				SNAdminAPI.unban(player.getName());
				e.allow();
				return;
			}
			//Else deny login
			String endTime = TimeUtils.millisToString(ban.getEndTime() - System.currentTimeMillis());
			String tempBanResponse = this.CONFIG.getString("messages.tempBan.victim","You are banned for %time% - Reason: %reason%").replaceAll("%time%", endTime).replaceAll("%reason%", ban.getReason());
			tempBanResponse = ChatUtils.colorize(tempBanResponse);
			this.PLUGIN.getLogger().info("Player " + player.getName() + " was denied permission to login. Reason: BanType.TEMP");
			e.disallow(PlayerLoginEvent.Result.KICK_BANNED, tempBanResponse);
			return;
		case 2:
			//Deny Normal Bans
			String banResponse = this.CONFIG.getString("messages.ban.victim","You are banned from this server - Reason: %reason%").replaceAll("%reason%", ban.getReason());
			banResponse = ChatUtils.colorize(banResponse);
			this.PLUGIN.getLogger().info("Player " + player.getName() + " was denied permission to login. Reason: BanType.BAN");
			e.disallow(PlayerLoginEvent.Result.KICK_BANNED, banResponse);
			return;
		case 3:
			//Deny Normal Bans
			String ipBanResponse = this.CONFIG.getString("messages.ipban.victim","Your IP banned from this server!").replaceAll("%ip%", ip);
			ipBanResponse = ChatUtils.colorize(ipBanResponse);
			this.PLUGIN.getLogger().info("Player " + player.getName() + " was denied permission to login. Reason: BanType.IP");
			e.disallow(PlayerLoginEvent.Result.KICK_BANNED, ipBanResponse);
			return;
		}
	}
}
