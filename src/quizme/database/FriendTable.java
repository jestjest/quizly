package quizme.database;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

import quizme.DBConnection;

public class FriendTable {
private DBConnection db;
	
	public FriendTable(DBConnection db) {
		this.db = db;
		createFriendTable();
	}
	
	private void createFriendTable() {
		try {
			PreparedStatement pstmt = db.getPreparedStatement("CREATE TABLE IF NOT EXISTS friends (username1 VARCHAR(128), username2 VARCHAR(128))");
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void addFriends(String username1, String username2) {
		if (areFriends(username1, username2)) return; /* already friends */
		try {	
			PreparedStatement pstmt = db.getPreparedStatement("INSERT INTO friends VALUES(?, ?)");
			if (username1.compareTo(username2) <= 0) { 
				/* this guarantees usernames are stored in alphabetical order*/
				pstmt.setString(1, username1);
				pstmt.setString(2, username2);
			} else {
				pstmt.setString(1, username2);
				pstmt.setString(2, username1);
			}
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void removeFriends(String username1, String username2) {
		try {
			PreparedStatement pstmt = db.getPreparedStatement("DELETE FROM friends WHERE username1 = ? AND username2 = ?");
			if (username1.compareTo(username2) <= 0) { 
				/* this guarantees usernames are stored in alphabetical order */
				pstmt.setString(1, username1);
				pstmt.setString(2, username2);
			} else {
				pstmt.setString(1, username2);
				pstmt.setString(2, username1);
			}
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public boolean areFriends(String username1, String username2) {
		try {
			String searchName1 = (username1.compareTo(username2) < 0) ? username1 : username2; 
			String searchName2 = (username1.compareTo(username2) < 0) ? username2 : username1; 
			PreparedStatement pstmt = db.getPreparedStatement("SELECT * FROM friends WHERE username1 = ? AND username2 = ?");
			pstmt.setString(1, searchName1);
			pstmt.setString(2, searchName2);
			ResultSet rs = pstmt.executeQuery();
			return rs.next();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false; /* indicates database error */
	}
	
	public int numOfFriendRelationships() {
		try {
			PreparedStatement pstmt = db.getPreparedStatement("SELECT COUNT(username1) FROM friends");
			ResultSet rs = pstmt.executeQuery();
			rs.first();
			return rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	public List<String> friendsList(String username) {
		try {
			PreparedStatement pstmt = db.getPreparedStatement("SELECT * FROM friends WHERE username1 = ? UNION ALL "
					+ "SELECT * FROM friends WHERE username2 = ?");
			pstmt.setString(1, username);
			pstmt.setString(2, username);
			ResultSet rs = pstmt.executeQuery();
			
			List<String> friends = new LinkedList<String>();
			while(rs.next()) {
				if (rs.getString(1).equals(username)) { /* username is listed first */
					friends.add(rs.getString(2));
				} else { /* username is listed second */
					friends.add(rs.getString(1));
				}
			}
			return friends;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null; /* indicates database error */
	}
	
}
