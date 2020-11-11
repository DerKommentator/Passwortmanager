package model.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectionToDatabase {
	
	private Connection connection;
	private String dbURL = "jdbc:sqlserver://vwagwosac132:1435;databaseName=PT_PVL;integratedSecurity=true;";
	
	public ConnectionToDatabase() {
		if(connectToDatabase()) {
			System.out.println("Verbindung zum SQL-Server wurder erfolgreich hergestellt.");
		} else {
			System.out.println("Verbindung zum SQL-Server konnte nicht hergestellt werden.");
		}
	}

	/**
	 * Verbindung zur Datenbank PT_PVL vom SQL-Server herstellen
	 * @return <code>true</code>, wenn es geklappt hat, andernfalls <code>false</code>.
	 */
	public boolean connectToDatabase() {
		
		try {
			connection = DriverManager.getConnection(dbURL);
			if (connection != null) {
//				DatabaseMetaData dm = (DatabaseMetaData) connection.getMetaData();
//				System.out.println("Driver name: " + dm.getDriverName());
//				System.out.println("Driver version: " + dm.getDriverVersion());
//				System.out.println("Product name: " + dm.getDatabaseProductName());
//				System.out.println("Product version: " + dm.getDatabaseProductVersion());
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		
	}
	
	/**
	 * SQL-Befehl ausfuehren
	 * @param sql SQL-Befehlstext
	 * @throws SQLException 
	 */
	public void executeSql(String sql) throws SQLException {
		System.out.println("Statement: " + sql);
		Statement statement = connection.createStatement();
		statement.execute(sql);
	}
	
	public ResultSet executeSqlQuery(String sql) throws SQLException {
		System.out.println("Query: " + sql);
		Statement statement = connection.createStatement();
		return statement.executeQuery(sql);
	}
	
	/**
	 * Prueft, ob die Connection zum SQL-Server (noch) besteht.
	 * @return <code>true</code>, wenn Connection besteht, andernfalls <code>false</code>.
	 */
	public boolean isConnected() {
		try {
			if (connection == null || connection.isClosed() || !connection.isValid(2000)) {
				return false;
			} else {
				return true;
			}
		} catch (SQLException e) {
			return false;
		}
	}
	
	/**
	 * Verbindung zum Server schliessen
	 */
	public void closeConnection() {
		if (isConnected()) {
			try {
				connection.close();
				connection = null;
			} catch (SQLException e) {
				connection = null;
			}
		}
	}
	
}
