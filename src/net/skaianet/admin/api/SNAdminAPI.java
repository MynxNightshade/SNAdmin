package net.skaianet.admin.api;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.skaianet.admin.SNAdmin;
import net.skaianet.admin.types.*;

public final class SNAdminAPI {
	
	public static final boolean addNewPlayer(String player, String ip) {
		player = player.toLowerCase();
		SNAdmin.PLAYER_LIST.query("INSERT INTO player_list(playername, ip, lastlogin) VALUES('" + player + "','" + ip + "','" + System.currentTimeMillis() + "');");
		return true;
	}
	
	public static final boolean updatePlayer(String player, String ip) {
		player = player.toLowerCase();
		SNAdmin.PLAYER_LIST.query("UPDATE player_list SET ip='" + ip + "', lastlogin='" + System.currentTimeMillis() + "' WHERE playername='" + player + "';");
		return true;
	}

	public static final boolean ban(String player, String ip, String reason, long endTime, int type) {
		player = player.toLowerCase();
		if (type == BanType.IP) { SNAdminAPI.ipBan(ip, reason); }
		if (SNAdmin.BAN_LIST.contains("ban_list", "playername", player)) {
			SNAdmin.BAN_LIST.query("UPDATE ban_list SET ip='" + ip  + "', reason='" + reason + "', start='" + System.currentTimeMillis() + "', end='" + endTime + "', type='" + type + "' WHERE playername='" + player + "';");
			return true;
		}
		SNAdmin.BAN_LIST.query("INSERT INTO ban_list(playername, ip, reason, start, end, type) VALUES('" + player + "','" + ip + "','" + reason + "','" + System.currentTimeMillis() + "','" + endTime + "','" + type + "');");
		return true;
	}
	
	public static boolean ipBan(String ip, String reason) {
		if (!SNAdmin.BANNED_IPS.contains("banned_ips", "ip", ip)) {
			SNAdmin.BANNED_IPS.query("INSERT INTO banned_ips(ip, reason) VALUES('" + ip + "','" + reason +"');");
			return true;
		}
		return false;
	}
	
	public static boolean unban(String player) {
		player = player.toLowerCase();
		if (SNAdmin.BAN_LIST.contains("ban_list", "playername", player)) {
			SNAdmin.BAN_LIST.query("DELETE FROM ban_list WHERE playername='" + player + "';");
			return true;
		}		
		return false;
	}
	
	public static boolean unbanIp(String ip) {
		if (SNAdmin.BANNED_IPS.contains("banned_ips", "ip", ip)) {
			SNAdmin.BANNED_IPS.query("DELETE FROM banned_ips WHERE ip='" + ip + "';");
			return true;
		}
		return false;
	}
	
	public static final boolean mute(String player, String reason, long endTime, int type) {
		player = player.toLowerCase();
		if (SNAdmin.MUTE_LIST.contains("mute_list", "playername", player)) {
			SNAdmin.MUTE_LIST.query("UPDATE mute_list SET reason='" + reason + "', start='" + System.currentTimeMillis() + "', end='" + endTime + "', type='" + type + "' WHERE playername='" + player + "';");
			return true;
		}
		SNAdmin.MUTE_LIST.query("INSERT INTO mute_list(playername, reason, start, end, type) VALUES('" + player + "','" + reason + "','" + System.currentTimeMillis() + "','" + endTime + "','" + type + "');");
		return true;
	}
	
	public static final boolean unmute(String player) {
		player = player.toLowerCase();
		if (SNAdmin.MUTE_LIST.contains("mute_list", "playername", player)) {
			SNAdmin.MUTE_LIST.query("DELETE FROM mute_list WHERE playername='" + player + "';");
			return true;
		}		
		return false;
	}
	
	public static final boolean warn(String player) {
		player = player.toLowerCase();
		if (SNAdmin.WARN_LIST.contains("warn_list", "playername", player)) {
			SNAdmin.WARN_LIST.query("UPDATE warn_list SET warns=warns+1;");
			return true;
		}
		SNAdmin.WARN_LIST.query("INSERT INTO warn_list(playername, warns) VALUES('" + player + "','1');");
		return true;
	}
	
	public static final boolean clearWarns(String player) {
		player = player.toLowerCase();
		if (player.equals("*")) {
			SNAdminAPI.clearAllWarns();
			return true;
		}
		if (SNAdmin.WARN_LIST.contains("warn_list", "playername", player)) {
			SNAdmin.WARN_LIST.query("DELETE FROM warn_list WHERE playername='" + player + "';");
			return true;
		}
		return false;
	}
	
	public static final boolean clearAllWarns() {
		SNAdmin.WARN_LIST.truncate("warn_list");
		return true;
	}
	
	public static final boolean kick(String player, String message) {
		Player p = Bukkit.getServer().getPlayer(player);
		if (p != null) {
			p.kickPlayer(message);
			return true;
		}
		return false;
	}
	
	public static final boolean sendMessage(String player, String message) {
		Player p = Bukkit.getServer().getPlayer(player);
		if (p != null) {
			p.sendMessage(message);
			return true;
		}
		return false;
	}
	
	public static final boolean hasPlayedBefore(String player) {
		player = player.toLowerCase();
		return SNAdmin.PLAYER_LIST.contains("player_list", "playername", player);
	}
	
	public static final boolean isIpBanned(String ip) {
		return SNAdmin.BANNED_IPS.contains("banned_ips", "ip", ip);
	}
	
	public static final PlayerInfo getPlayer(String player) {
		player = player.toLowerCase();
		if (SNAdmin.PLAYER_LIST.contains("player_list", "playername", player)) {
			try {
				ResultSet rs = SNAdmin.PLAYER_LIST.query("SELECT * FROM player_list WHERE playername='" + player + "';");
				PlayerInfo p = new PlayerInfo(player, rs.getString("ip"), rs.getLong("lastlogin"));
				rs.close();
				return p;
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
			return null;
		}
		return null;
	}
	
	public static final BanInfo getBanInfo(String player) {
		player = player.toLowerCase();
		if (SNAdmin.BAN_LIST.contains("ban_list", "playername", player)) {
			try {
				ResultSet rs = SNAdmin.BAN_LIST.query("SELECT * FROM ban_list WHERE playername='" + player + "';");
				BanInfo b = new BanInfo(player, rs.getString("ip"), rs.getString("reason"), rs.getLong("start"), rs.getLong("end"), rs.getInt("type"));
				//rs.close();
				return b;
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
			return null;
		}
		return null;
	}
	
	public static final MuteInfo getMuteInfo(String player) {
		player = player.toLowerCase();
		if (SNAdmin.MUTE_LIST.contains("mute_list", "playername", player)) {
			try {
				ResultSet rs = SNAdmin.MUTE_LIST.query("SELECT * FROM mute_list WHERE playername='" + player + "';");
				MuteInfo m = new MuteInfo(player, rs.getString("reason"), rs.getLong("start"), rs.getLong("end"), rs.getInt("type"));
				rs.close();
				return m;
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
			return null;
		}
		return null;
	}
	
	public static final WarnInfo getWarnInfo(String player) {
		player = player.toLowerCase();
		if (SNAdmin.WARN_LIST.contains("warn_list", "playername", player)) {
			try {
				ResultSet rs = SNAdmin.WARN_LIST.query("SELECT * FROM warn_list WHERE playername='" + player + "';");
				WarnInfo warns = new WarnInfo(player, rs.getInt("warns"));
				rs.close();
				return warns;
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
			return null;
		} else if (player.equals("*")) {
			return new WarnInfo("*", -1);
		}
		return null;
	}
	
	public static final IPInfo getIPInfo(String ip) {
		if (SNAdmin.BANNED_IPS.contains("banned_ips", "ip", ip)) {
			try {
				ResultSet rs = SNAdmin.BANNED_IPS.query("SELECT * FROM banned_ips WHERE ip='" + ip + "';");
				IPInfo warns = new IPInfo(ip, rs.getString("ip"));
				rs.close();
				return warns;
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
			return null;
		}
		return null;
	}
}
