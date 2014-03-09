package net.skaianet.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

public abstract class Database {
	protected Logger LOG;
	protected Connection CONN;
	
	public Database(Logger log) {
		this.LOG = log;
	}
	
	protected abstract boolean initialize();
	
	public abstract boolean open();
	
	public boolean close() {
		if (this.CONN != null) {
			try {
				this.CONN.close();
				return true;
			} catch (SQLException ex) {
				this.LOG.severe("Could not close connection, SQLException: " + ex.getMessage());
				return false;
			}
		}
		this.LOG.warning("Could not close connection - it is null");
		return false;
	}
	
	public final Connection getConnection() {
		return this.CONN;
	}
	
	public final boolean isOpen() {
		return this.isOpen(1);
	}
	
	public final boolean isOpen(int seconds) {
		if (this.CONN != null) {
			try {
				return this.CONN.isValid(seconds);
			} catch (SQLException ex) {
				return false;
			}
		}
		return false;
	}
	
	public final ResultSet query(String query) {
		try {
			Statement s = this.getConnection().createStatement();
			if (s.execute(query)) {
				return s.getResultSet();
			}
			return null;
		} catch (SQLException ex) {
			this.LOG.severe("Error querying Database: " + ex.getMessage());
			return null;
		}
	}
	
	public abstract boolean contains(String table, String col, String val);
	public abstract boolean containsTable(String table);
	public abstract boolean truncate(String table);
}
