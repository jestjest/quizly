package quizme.database;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import quizme.DBConnection;

public class MessagesTable {
private DBConnection db;

public static int NOTE = 1;
public static int CHALLENGE = 2;
public static int REQUEST = 3;
	
	public MessagesTable(DBConnection db) {
		this.db = db;
		createMessagesTable();
	}
	
	private void createMessagesTable() {
		try {
			PreparedStatement pstmt = db.getPreparedStatement("CREATE TABLE IF NOT EXISTS messages (messageid INT, toUsername VARCHAR(128), fromUsername VARCHAR(128), date Timestamp, content TEXT, subject VARCHAR(128), type INT, seen BOOL)");
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public int addMessage(String toUsername, String fromUsername, Timestamp date, String content, String subject, int type) {
		try {
			if (type > REQUEST || type < NOTE) {
				System.out.println("invalid type");
				return -1;
			}
			
			PreparedStatement pstmt1 = db.getPreparedStatement("SELECT messageid FROM messages");
			ResultSet rs = pstmt1.executeQuery();
			rs.last();
			int messageid = rs.getRow() + 1;
			
			PreparedStatement pstmt2 = db.getPreparedStatement("INSERT INTO messages VALUES(?, ?, ?, ?, ?, ?, ?, 0)");
			pstmt2.setInt(1, messageid);
			pstmt2.setString(2, toUsername);
			pstmt2.setString(3, fromUsername);
			pstmt2.setTimestamp(4, date);
			pstmt2.setString(5, content);
			pstmt2.setString(6, subject);
			pstmt2.setInt(7, type);
			pstmt2.executeUpdate();
			return messageid;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1; /* indicates type input or database error */
	}
	
	public void removeMessage(int messageid) {
		try {
			PreparedStatement pstmt = db.getPreparedStatement("DELETE FROM messages WHERE messageid = ?");
			pstmt.setInt(1, messageid);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/* ONLY USE FOR TESTING */
	public void clearAllMessages() {
		try {
			PreparedStatement pstmt = db.getPreparedStatement("DELETE FROM messages");
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void setSeen(int messageid, boolean seen) {
		int seenNum = seen ? 1 : 0;
		setInt(messageid, "seen", seenNum);
	}
	
	public boolean getSeen(int messageid) {
		return (getInt(messageid, "seen") > 0);
	}
	
	public ResultSet getAllUserReceivedMessages(String toUsername) {
		try {
			PreparedStatement pstmt = db.getPreparedStatement("SELECT * FROM messages WHERE toUsername = ? ORDER BY date DESC");
			pstmt.setString(1, toUsername);
			return pstmt.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public ResultSet getRecentUserReceivedMessages(String toUsername, int numOfResults) {
		try {
			PreparedStatement pstmt = db.getPreparedStatement("SELECT * FROM messages WHERE toUsername = ? ORDER BY date DESC LIMIT ?");
			pstmt.setString(1, toUsername);
			pstmt.setInt(2, numOfResults);
			return pstmt.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public ResultSet getAllUserSentMessages(String fromUsername) {
		try {
			PreparedStatement pstmt = db.getPreparedStatement("SELECT * FROM messages WHERE fromUsername = ? ORDER BY date DESC");
			pstmt.setString(1, fromUsername);
			return pstmt.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/* helper functions */
	private void setInt(int messageid, String field, int value) {
		try {
			PreparedStatement pstmt = db.getPreparedStatement("UPDATE messages SET " + field + " = ? WHERE messageid = ?");
			pstmt.setInt(1, value);
			pstmt.setInt(2, messageid);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private int getInt(int messageid, String field) {
		try {
			PreparedStatement pstmt = db.getPreparedStatement("SELECT " +  field + " FROM messages WHERE messageid = ?");
			pstmt.setInt(1, messageid);
			ResultSet rs = pstmt.executeQuery();
			rs.first();
			return rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1; /* indicates database error */
	}
}
