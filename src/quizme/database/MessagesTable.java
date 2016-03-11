package quizme.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import quizme.DBConnection;
import quizme.links.MessageLink;

public class MessagesTable {
private DBConnection db;

public static final int NOTE = 1;
public static final int CHALLENGE = 2;
public static final int REQUEST = 3;
	
	public MessagesTable(DBConnection db) {
		this.db = db;
		createMessagesTable();
	}
	
	private void createMessagesTable() {
		try {
			PreparedStatement pstmt = db.getPreparedStatement("CREATE TABLE IF NOT EXISTS messages (messageid INT AUTO_INCREMENT "
															+ "primary key NOT NULL, toUsername VARCHAR(128), fromUsername VARCHAR(128), "
															+ "date Timestamp, content TEXT, subject VARCHAR(128), type INT, seen BOOL)");
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
			
			PreparedStatement pstmt1 = db.getPreparedStatement("INSERT INTO messages (toUsername, fromUsername, date, content, subject, type, seen) VALUES(?, ?, ?, ?, ?, ?, 0)");
			pstmt1.setString(1, toUsername);
			pstmt1.setString(2, fromUsername);
			pstmt1.setTimestamp(3, date);
			pstmt1.setString(4, content);
			pstmt1.setString(5, subject);
			pstmt1.setInt(6, type);
			pstmt1.executeUpdate();
			
			PreparedStatement pstmt2 = db.getPreparedStatement("SELECT messageid FROM messages ORDER BY messageid ASC");
			ResultSet rs = pstmt2.executeQuery();
			rs.last();
			return rs.getInt(1);
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
	
	public void removeRequestMessage(String toUsername, String fromUsername) {
		try {
			PreparedStatement pstmt = db.getPreparedStatement("DELETE FROM messages WHERE toUsername = ? AND fromUsername = ? "
					+ "AND type = ?");
			pstmt.setString(1, toUsername);
			pstmt.setString(2, fromUsername);
			pstmt.setInt(3, REQUEST);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void removeChallenge(String toUsername, String fromUsername, String quizLink) {
 		try {
 			PreparedStatement pstmt = db.getPreparedStatement("DELETE from messages WHERE toUsername = ? AND fromUsername = ? AND type = ? AND subject = ?");
 			pstmt.setString(1, toUsername);
 			pstmt.setString(2, fromUsername);
 			pstmt.setInt(3, CHALLENGE);
 			pstmt.setString(4, quizLink);
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
	
	/* HomePage related functions */
	/**
	 * Return a list of all unseen messages sent to a user
	 * @param username the user name of the user.
	 * @param n maximum number of messages to be returned.
	 * @return A list of MessageLink objects sorted chronologically.
	 */
	public List<MessageLink> getAllUnseenMessages( String username, int n ) {
		try {
			PreparedStatement pstmt = 
					db.getPreparedStatement("SELECT * FROM messages "
							+ "WHERE toUsername = ? AND seen = 0 ORDER BY date DESC LIMIT ?");
			pstmt.setString(1, username);
			pstmt.setInt(2, n);
			ResultSet rs = pstmt.executeQuery(); // Query
			List<MessageLink> messageLinks = new ArrayList<MessageLink>();
			while( rs.next() ) {				
				MessageLink messageLink = new MessageLink( rs.getInt("messageid"), rs.getString("fromUsername"),
						rs.getString("subject"), rs.getTimestamp("date"), rs.getString("content"), 
						rs.getBoolean("seen"), MessageLink.MType.values()[rs.getInt("type")-1]);
				messageLinks.add(messageLink);
			}
			return messageLinks;
		} catch( SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Return a list of all messages sent to a user
	 * @param username the user name of the user.
	 * @param n maximum number of messages to be returned.
	 * @return A list of MessageLink objects sorted chronologically.
	 */
	public List<MessageLink> getAllReceivedMessages( String username, int n ) {
		try {
			PreparedStatement pstmt = 
					db.getPreparedStatement("SELECT * FROM messages "
							+ "WHERE toUsername = ? ORDER BY date DESC LIMIT ?");
			pstmt.setString(1, username);
			pstmt.setInt(2, n);
			ResultSet rs = pstmt.executeQuery(); // Query
			List<MessageLink> messageLinks = new ArrayList<MessageLink>();
			while( rs.next() ) {				
				MessageLink messageLink = new MessageLink( rs.getInt("messageid"), rs.getString("fromUsername"),
						rs.getString("subject"), rs.getTimestamp("date"), rs.getString("content"), 
						rs.getBoolean("seen"), MessageLink.MType.values()[rs.getInt("type")-1]);
				messageLinks.add(messageLink);
			}
			return messageLinks;
		} catch( SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Check if username1 has requested to be friend with username2
	 * @param username1
	 * @param username2
	 * @return true/false, null if exception occurs.
	 */
	public Boolean hasRequested( String username1, String username2 ) {
		try {
			PreparedStatement pstmt = 
					db.getPreparedStatement("SELECT * FROM messages "
							+ "WHERE fromUsername = ? AND toUsername = ? AND type = 3");
			pstmt.setString(1, username1);
			pstmt.setString(1, username2);
			ResultSet rs = pstmt.executeQuery(); // Query
			rs.last();
			if ( rs.getRow() > 0 ) {
				return true;
			}
			else {
				return false;
			}
		} catch( SQLException e) {
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
