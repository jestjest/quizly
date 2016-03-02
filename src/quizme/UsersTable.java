package quizme.database;

import java.sql.*;
import java.util.*;
import quizme.DBConnection;

public class UsersTable {
	private DBConnection db;
	
	public UsersTable(DBConnection db) {
		this.db = db;
		createUserTable();
	}
	
	private void createUserTable() {
		try {
			PreparedStatement pstmt = db.getPreparedStatement("CREATE TABLE IF NOT EXISTS users (username VARCHAR(128), password VARCHAR(128), admin BOOL)");
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public boolean addUser(String username, String password) {
		/* boolean indicates whether the update was successful */
		try {
			PreparedStatement pstmt = db.getPreparedStatement("INSERT INTO users VALUES(?, ?, 0)");
			pstmt.setString(1, username);
			pstmt.setString(2, password);
			pstmt.executeUpdate();
			return true;
			//return (rows == 1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public void removeUser(String username) {
		try {
			PreparedStatement pstmt = db.getPreparedStatement("DELETE FROM users WHERE username = ?");
			pstmt.setString(1, username);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public boolean correctPassword(String username, String password) {
		try {
			PreparedStatement stmt = db.getPreparedStatement("SELECT * FROM users WHERE username=? AND password=?");
			stmt.setString(1, username);
			stmt.setString(2, password);
			ResultSet rs = stmt.executeQuery();
			return rs.next();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean usernameAlreadyExists(String username) {
		try {
			PreparedStatement pstmt = db.getPreparedStatement("SELECT * FROM users WHERE username=?");
			pstmt.setString(1, username);
			ResultSet rs = pstmt.executeQuery();
			return (rs.next());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public void setPassword(String username, String password) {
		/* assumes the new password has already been hashed */
		setString(username, "password", password);
	}
	
	public String getPassword(String username) {
		return getString(username, "password");
	}
	
	public void setAdmin(String username, boolean admin) {
		int adminNum = (admin) ? 1 : 0;
		setInt(username, "admin", adminNum);
	}
	
	public boolean getAdmin(String username) {
		return getInt(username, "admin") > 0 ? true : false;
	}
	
	public List<String> getUsernameMatches(String username) {
		try {
			List<String> potentialMatches = new LinkedList<String>();
			PreparedStatement pstmt = db.getPreparedStatement("SELECT username FROM users WHERE username REGEXP ?");
			pstmt.setString(1, "[[:alnum:]]*" + username + "[[:alnum:]]*");
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				potentialMatches.add(rs.getString(1));
			}
			return potentialMatches;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null; /* indicates a database error */
		
	}
	
	/* helper functions */
	
	private void setString(String username, String field, String value) {
		try {
			PreparedStatement pstmt = db.getPreparedStatement("UPDATE users SET " + field + " = ?  WHERE username = ?");
			pstmt.setString(1, value);
			pstmt.setString(2, username);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private String getString(String username, String field) {
		try {
			PreparedStatement pstmt = db.getPreparedStatement("SELECT " + field + " FROM users WHERE username = ?");
			pstmt.setString(1, username);
			ResultSet rs = pstmt.executeQuery();
			rs.first();
			return rs.getString(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null; /* indicates database error */
	}
	
	private void setInt(String username, String field, int value) {
		try {
			PreparedStatement pstmt = db.getPreparedStatement("UPDATE users SET " + field + " = ? WHERE username = ?");
			pstmt.setInt(1, value);
			pstmt.setString(2, username);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private int getInt(String username, String field) {
		try {
			PreparedStatement pstmt = db.getPreparedStatement("SELECT " + field + " FROM users WHERE username = ?");
			pstmt.setString(1, username);
			ResultSet rs = pstmt.executeQuery();
			rs.first();
			return rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1; /* indicates database error */
	}
}
