package net.skaianet.admin.listeners;

import net.skaianet.admin.SNAdmin;
import net.skaianet.admin.api.SNAdminAPI;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class SNAdminNewPlayerListener implements Listener {
	private SNAdmin PLUGIN;

	public SNAdminNewPlayerListener(SNAdmin plugin) {
		this.PLUGIN = plugin;
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerJoin(PlayerLoginEvent e) {
		Player player = e.getPlayer();
		if (!SNAdminAPI.hasPlayedBefore(player.getName())) {
			SNAdminAPI.addNewPlayer(player.getName(), e.getAddress().toString().replaceAll("/", ""));
			this.PLUGIN.getLogger().info("Player " + player.getName() + " has been added to the player_list");
			return;
		}
		SNAdminAPI.updatePlayer(player.getName(), e.getAddress().toString().replaceAll("/", ""));
		return;
	}
}
