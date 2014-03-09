package net.skaianet.db;

import java.io.File;
import java.io.IOException;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import net.skaianet.admin.SNAdmin;

public class SQLite extends Database {
	private final File DB_FILE;
	
	public SQLite(SNAdmin plugin, String file) {
		super(plugin.getLogger());
		this.DB_FILE = new File(plugin.getDataFolder() + "/" + file);
		if (!this.DB_FILE.exists()) {
			try {
				this.DB_FILE.createNewFile();
			} catch (IOException ex) {
				this.LOG.severe("Could not create Database file: " + ex.getMessage());
			}
		}
	}

	@Override
	protected boolean initialize() {
		try {
			Class.forName("org.sqlite.JDBC");
			return true;
		} catch (ClassNotFoundException ex) {
			this.LOG.severe("SQL library not found: " + ex.getMessage());
			return false;
		}
	}

	@Override
	public boolean open() {
		if(this.initialize()) {
			try {
				this.CONN = DriverManager.getConnection("jdbc:sqlite:" + this.DB_FILE);
				return true;
			} catch (SQLException ex) {
				this.LOG.severe("Could not establish a connection: " + ex.getMessage());
				return false;
			}
		}
		return false;
	}
	
	@Override
	public boolean contains(String table, String col, String val) {
		ResultSet rs = null;
		try {
			rs = this.query("SELECT * FROM " + table + " WHERE " + col + "='" + val + "';");
			if (!rs.isBeforeFirst()) {
				rs.close();
				return false;
			}
			rs.close();
			return true;
		} catch (SQLException ex) {
			this.LOG.severe("Error querying Database: " + ex.getMessage());
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					this.LOG.severe("Error closing ResultSet: " + e.getMessage());
				}
			}
			return false;
		}
	}

	@Override
	public boolean containsTable(String table) {
		DatabaseMetaData md = null;
		try {
			md = this.CONN.getMetaData();
			ResultSet tables = md.getTables(null, null, table, null);
			if (tables.next()) {
				tables.close();
				return true;
			}
			tables.close();
			return false;
		} catch (SQLException ex) {
			this.LOG.severe("Could not check if \"" + table + "\" exists: " + ex.getMessage());
			return false;
		}
	}

	@Override
	public boolean truncate(String table) {
		Statement s = null;
		String query = null;
		try {
			if(!this.containsTable(table)) {
				this.LOG.warning("\"" + table + "\" does not exist");
				return false;
			}
			s = this.CONN.createStatement();
			query = "DELETE FROM " + table + ";";
			s.executeQuery(query);
			s.close();
			return true;
		} catch (SQLException ex) {
			this.LOG.severe("Error wipping table \"" + table + "\": " + ex.getMessage());
			return false;
		}
	}
}
