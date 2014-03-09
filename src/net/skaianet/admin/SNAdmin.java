package net.skaianet.admin;

import java.util.logging.Level;

import net.skaianet.admin.commands.*;
import net.skaianet.admin.listeners.*;
import net.skaianet.db.Database;
import net.skaianet.db.SQLite;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class SNAdmin extends JavaPlugin {
	public static final String NO_PERMISSION = "You do not have permission to use that command.";
	public static final String DEFAULT_AMDIN = "server";
	public static final String DEFAULT_REASON = "No Reason";
	public static Database PLAYER_LIST, BAN_LIST, MUTE_LIST, WARN_LIST, BANNED_IPS;
	
	@Override
	public void onEnable() {
		long time = System.currentTimeMillis();
		this.getDataFolder().mkdir();
		this.saveDefaultConfig();
		
		//Register Listeners
		this.registerListeners();
		
		//Register CommandExecutors
		this.registerCommands();
		
		//Load Database
		PLAYER_LIST = new SQLite(this, "playerlist.db");
		BAN_LIST = new SQLite(this, "banlist.db");
		MUTE_LIST = new SQLite(this, "mutelist.db");
		WARN_LIST = new SQLite(this, "warnlist.db");
		BANNED_IPS = new SQLite(this, "bannedips.db");
		this.setupSqlConnections();
		this.checkSqlTables();
		
		this.getLogger().info("SNAdmin Loaded! (" + (System.currentTimeMillis() - time) + "ms)");
	}
	
	@Override
	public void onDisable() {
		this.getServer().getScheduler().cancelTasks(this);
		this.saveConfig();
	}
	
	private void registerListeners() {
		PluginManager pm = this.getServer().getPluginManager();
		pm.registerEvents(new SNAdminNewPlayerListener(this), this);
		pm.registerEvents(new SNAdminBanListener(this), this);
		pm.registerEvents(new SNAdminMuteListener(this), this);
	}
	
	private void registerCommands() {
		this.getCommand("snadmin").setExecutor(new SNAdminCmd(this));
		this.getCommand("ban").setExecutor(new BanCmd(this));
		this.getCommand("tempban").setExecutor(new TempBanCmd(this));
		this.getCommand("ipban").setExecutor(new IPBanCmd(this));
		this.getCommand("unban").setExecutor(new UnBanCmd(this));
		this.getCommand("mute").setExecutor(new MuteCmd(this));
		this.getCommand("tempmute").setExecutor(new TempMuteCmd(this));
		this.getCommand("unmute").setExecutor(new UnMuteCmd(this));
		this.getCommand("kick").setExecutor(new KickCmd(this));
		this.getCommand("warn").setExecutor(new WarnCmd(this));
		this.getCommand("clearwarns").setExecutor(new ClearWarnsCmd(this));
	}
	
	private void setupSqlConnections() {
		PLAYER_LIST.open();
		BAN_LIST.open();
		MUTE_LIST.open();
		WARN_LIST.open();
		BANNED_IPS.open();
	}
	
	private void checkSqlTables() {
		if (!PLAYER_LIST.containsTable("player_list")) {
			PLAYER_LIST.query("CREATE TABLE player_list (playername TEXT PRIMARY KEY, ip TEXT, lastlogin INTEGER);");
			this.getLogger().log(Level.INFO, "Created 'player_list' Database");
		}
		if (!BAN_LIST.containsTable("ban_list")) {
			BAN_LIST.query("CREATE TABLE ban_list (playername TEXT PRIMARY KEY, ip TEXT, reason TEXT, start INTEGER, end INTEGER, type INTEGER);");
			this.getLogger().log(Level.INFO, "Created 'ban_list' Database");
		}
		if (!MUTE_LIST.containsTable("mute_list")) {
			MUTE_LIST.query("CREATE TABLE mute_list (playername TEXT PRIMARY KEY, reason TEXT, start INTEGER, end INTEGER, type INTEGER);");
			this.getLogger().log(Level.INFO, "Created 'mute_list' Database");
		}
		if (!WARN_LIST.containsTable("warn_list")) {
			WARN_LIST.query("CREATE TABLE warn_list (playername TEXT PRIMARY KEY, warns INTEGER);");
			this.getLogger().log(Level.INFO, "Created 'warn_list' Database");
		}
		if (!BANNED_IPS.containsTable("banned_ips")) {
			BANNED_IPS.query("CREATE TABLE banned_ips (ip TEXT PRIMARY KEY, reason TEXT);");
			this.getLogger().log(Level.INFO, "Created 'banned_ips' Database");
		}
		return;
	}
}
