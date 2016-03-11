// DBConnection.java
package quizme;

import java.sql.*;

/**
 * Wrapper class around a Connection to a database. Connects to MYSQL and
 * closes the connection once a context ends. Gives any servlets that need
 * to interact with the database a Statement object.
 */
public class DBConnection {
	static String account = MyDBInfo.MYSQL_USERNAME;
	static String password = MyDBInfo.MYSQL_PASSWORD;
	static String server = MyDBInfo.MYSQL_DATABASE_SERVER;
	static String database = MyDBInfo.MYSQL_DATABASE_NAME;
	
	private Connection con;
	
	public DBConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection
					("jdbc:mysql://" + server, account, password);
			con.createStatement().executeQuery("USE " + database);
		} catch (SQLException e) {
			e.printStackTrace();
			return;
		} 
		catch (ClassNotFoundException e) {
			e.printStackTrace();
			return;
		}
	}
	
	public void closeConnection() {
		try {
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public PreparedStatement getPreparedStatement(String query) {
		PreparedStatement stmt = null;
		try {
			stmt = con.prepareStatement(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return stmt;
	}
}