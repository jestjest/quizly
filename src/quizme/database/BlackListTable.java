package quizme.database;

import java.sql.*;
import quizme.DBConnection; 

public class BlackListTable {
	private DBConnection db;
	
	public BlackListTable(DBConnection db) {
		this.db = db;
		createBlackListTable();
	}
	
	private void createBlackListTable() {
		try {
			PreparedStatement pstmt = db.getPreparedStatement("CREATE TABLE IF NOT EXISTS blacklist (username VARCHAR(128))");
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public boolean addUser(String username) {
		/* boolean indicates whether the update was successful */
		try {
			PreparedStatement pstmt = db.getPreparedStatement("INSERT INTO blacklist VALUES(?)");
			pstmt.setString(1, username);
			pstmt.executeUpdate();
			return true;
			//return (rows == 1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean inBlacklist(String username) {
		try {
			PreparedStatement pstmt = db.getPreparedStatement("SELECT * FROM blacklist WHERE username=?");
			pstmt.setString(1, username);
			ResultSet rs = pstmt.executeQuery();
			return (rs.next());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
}