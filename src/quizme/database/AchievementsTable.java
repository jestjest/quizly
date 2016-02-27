package quizme.database;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import quizme.DBConnection;

public class AchievementsTable {
private DBConnection db;
	
	public AchievementsTable(DBConnection db) {
		this.db = db;
		createAchievementTable();
	}
	
	private void createAchievementTable() {
		try {
			PreparedStatement pstmt = db.getPreparedStatement("CREATE TABLE IF NOT EXISTS achievements (username VARCHAR(128), achievement VARCHAR(128), date DATE)");
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public int addAchievement(String username, String achievement, Date date) {
		try {
			PreparedStatement pstmt = db.getPreparedStatement("INSERT INTO achievements VALUES(?, ?, ?)");
			pstmt.setString(1, username);
			pstmt.setString(2, achievement);
			pstmt.setDate(3, date);
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
			System.out.println(pstmt);
			return pstmt.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}

