package quizme.database;

import java.sql.*;
import quizme.DBConnection;

public class AchievementGuidelinesTable {
	private DBConnection db;

	public AchievementGuidelinesTable(DBConnection db) {
		this.db = db;
		createAchievementTable();
	}

	private void createAchievementTable() {
		try {
			PreparedStatement pstmt = db.getPreparedStatement("SOURCE achievement-guidelines.sql");
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/* Use achievement types specified in Achievement.java */
	public ResultSet getAchievementGuidelinesByType(int type) {
		PreparedStatement pstmt = db.getPreparedStatement("SELECT * FROM achievementGuidelines WHERE type = ?");
		try {
			pstmt.setInt(1, type);
			return pstmt.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public ResultSet getAchievementGuidelineByName(String name) {
		PreparedStatement pstmt = db.getPreparedStatement("SELECT * FROM achievementGuidelines WHERE name = ?");
		try {
			pstmt.setString(1, name);
			return pstmt.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}

