package quiz;

import java.sql.*;

public class DBConnection {
	private Connection con;
	private Statement stmt;
	
	public DBConnection() {
		super();
		openDatabase();
	}
	
	private void openDatabase() {
		try {
		    Class.forName("com.mysql.jdbc.Driver"); 
		    
		    con = DriverManager.getConnection
		    		( "jdbc:mysql://" + MyDBInfo.MYSQL_DATABASE_SERVER, 
		    	    MyDBInfo.MYSQL_USERNAME, MyDBInfo.MYSQL_PASSWORD);
		    
		    stmt = con.createStatement();
		    stmt.executeQuery("USE " + MyDBInfo.MYSQL_DATABASE_NAME);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public Statement getStatement() {
		return stmt;
	}
	
	public void closeDatabase() {
		try {
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
