package quizme.database;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import quizme.DBConnection;

public class FriendRequestTable {
private DBConnection db;
	
	public FriendRequestTable(DBConnection db) {
		this.db = db;
		createFriendTable();
	}
	
	private void createFriendTable() {
		try {
			PreparedStatement pstmt = db.getPreparedStatement("CREATE TABLE IF NOT EXISTS friendrequests (sender VARCHAR(128), receiver VARCHAR(128))");
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void addFriendRequest(String sender, String receiver) {
		try {	
			PreparedStatement pstmt = db.getPreparedStatement("INSERT INTO friendrequests VALUES(?, ?)");
			pstmt.setString(1, sender);
			pstmt.setString(2, receiver);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void removeFriendRequest(String sender, String receiver) {
		try {
			PreparedStatement pstmt = db.getPreparedStatement("DELETE FROM friendrequests WHERE sender = ? AND receiver = ?");
			pstmt.setString(1, sender);
			pstmt.setString(2, receiver);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public boolean pendingFriendRequest(String sender, String receiver) {
		try {
			PreparedStatement pstmt = db.getPreparedStatement("SELECT * from friendrequests WHERE sender = ? AND receiver = ?");
			pstmt.setString(1, sender);
			pstmt.setString(2, receiver);
			ResultSet rs = pstmt.executeQuery();
			return rs.next();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

}
