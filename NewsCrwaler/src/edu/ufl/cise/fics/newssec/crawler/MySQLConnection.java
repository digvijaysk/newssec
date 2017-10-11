package edu.ufl.cise.fics.newssec.crawler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * 
 * 
 * 
 *
 */
public class MySQLConnection {

	
	private static final String DB_DRIVER = "com.mysql.jdbc.Driver";
	private static final String DB_URL = "jdbc:mysql://localhost:3306/news_security";
	private static final String MAX_POOL_SIZE = "250";
	private static final String USERNAME = "crawler";
	private static final String PASSWORD = "Ready2go";
	
	
	private Connection connection;
	private Properties properties;

	private Properties getProperties() {
		if (properties == null) {
			properties = new Properties();
			properties.setProperty("user", USERNAME);
			properties.setProperty("password", PASSWORD);
			properties.setProperty("MaxPooledStatements", MAX_POOL_SIZE);
		}
		return properties;
	}

	// connect
	public Connection connect() {
		if (connection == null) {
			try {
				Class.forName(DB_DRIVER);
				connection = DriverManager.getConnection(DB_URL, getProperties());
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		}
		return connection;
	}

	// disconnect
	public void disconnect() {
		if (connection != null) {
			try {
				connection.close();
				connection = null;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
