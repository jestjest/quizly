package quizme.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import quizme.DBConnection;
import quizme.links.AchievementLink;

public class AchievementsTable {
private DBConnection db;
	
	public AchievementsTable(DBConnection db) {
		this.db = db;
		createAchievementTable();
	}
	
	private void createAchievementTable() {
		try {
			PreparedStatement pstmt = db.getPreparedStatement("CREATE TABLE IF NOT EXISTS achievements (username VARCHAR(128), achievement VARCHAR(128), date DATETIME)");
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public int addAchievement(String username, String achievement, Timestamp date) {
		try {
			PreparedStatement pstmt = db.getPreparedStatement("INSERT INTO achievements VALUES(?, ?, ?)");
			pstmt.setString(1, username);
			pstmt.setString(2, achievement);
			pstmt.setTimestamp(3, date);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1; /* indicates database error */
	}
	
	public void removeAchievement(String username, String achievement) {
		try {
			PreparedStatement pstmt = db.getPreparedStatement("DELETE FROM achievements WHERE username = ? AND achievement = ?");
			pstmt.setString(1, username);
			pstmt.setString(2, achievement);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/* ONLY USE FOR TESTING */
	public void clearAllAchievements() {
		try {
			PreparedStatement pstmt = db.getPreparedStatement("DELETE FROM achievements");
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public boolean hasAchievement(String username, String achievement) {
		try {
			PreparedStatement pstmt = db.getPreparedStatement("SELECT * FROM achievements WHERE username = ? and achievement = ?");
			pstmt.setString(1, username);
			pstmt.setString(2, achievement);
			ResultSet rs = pstmt.executeQuery();
			return rs.next();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public ResultSet getAllUserAchievements(String username) {
		try {
			PreparedStatement pstmt = db.getPreparedStatement("SELECT * FROM achievements WHERE username = ? ORDER BY date DESC");
			pstmt.setString(1, username);
			return pstmt.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public ResultSet getRecentUserAchievements(String username, int numOfResults) {
		try {
			PreparedStatement pstmt = db.getPreparedStatement("SELECT * FROM achievements WHERE username = ? ORDER BY date DESC LIMIT ? ");
			pstmt.setString(1, username);
			pstmt.setInt(2, numOfResults);
			return pstmt.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/* HomePage related functions */
	/**
	 * Provide chronologically ordered list of all achievements of a user
	 * @param username
	 * @return a list of AchievementLink of the specified user.
	 */
	public List<AchievementLink> getAllUserAchievementsLinkList( String username ) {
		ResultSet rs = getAllUserAchievements( username );
		List<AchievementLink> achieveList = new ArrayList<AchievementLink>();
		try {
			while ( rs.next() ) {
				AchievementLink achieveLink = new AchievementLink( rs.getString("achievement"), 
						rs.getString("username"), rs.getTimestamp("date") );
				achieveList.add( achieveLink );
			}
			return achieveList;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}

