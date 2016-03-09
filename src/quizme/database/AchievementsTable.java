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
			PreparedStatement pstmt = db.getPreparedStatement("CREATE TABLE IF NOT EXISTS "
					+ "achievements (username VARCHAR(128), achievement VARCHAR(128), date DATETIME)");
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
			PreparedStatement pstmt = db.getPreparedStatement("SELECT * FROM achievements "
					+ "WHERE username = ? ORDER BY date DESC LIMIT ? ");
			pstmt.setString(1, username);
			pstmt.setInt(2, numOfResults);
			return pstmt.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public int numOfAchievementsHelper(Timestamp t) {
		try {
			PreparedStatement pstmt = db.getPreparedStatement("SELECT COUNT(username) FROM achievements " 
				+ "WHERE date > t");
			ResultSet rs = pstmt.executeQuery();
			rs.next();
			return rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	private static final long dayDuration = 24 * 60 * 60 * 1000;
	private static final long weekDuration = 7 * 24 * 60 * 60 * 1000;
	public int[] numOfAchievements() {
		int[] numOfAchievements = new int[3];
		
		Timestamp lastDay = new Timestamp(System.currentTimeMillis() - dayDuration);
		numOfAchievements[0] = numOfAchievementsHelper(lastDay); 
		
		Timestamp lastWeek = new Timestamp(System.currentTimeMillis() - weekDuration);
		numOfAchievements[1] = numOfAchievementsHelper(lastWeek);  
		
		Timestamp allTime = new Timestamp(0);
		numOfAchievements[2] = numOfAchievementsHelper(allTime); 
		return numOfAchievements;
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

	/**
	 * Provide chronologically ordered list of recent achievements of a user
	 * @param username
	 * @param n maximum number of results
	 * @param t a time (Timestamp) after which is considered as recent
	 * @return List<AchievementLink>
	 */
	public List<AchievementLink>getRecentUserAchievements( 
			String username, int n, Timestamp t) {
		try {
			PreparedStatement pstmt = 
					db.getPreparedStatement("SELECT * FROM achievements "
							+ "WHERE username = ? AND date > ? ORDER BY date DESC LIMIT ?");
			pstmt.setString(1, username);
			pstmt.setTimestamp(2, t);
			pstmt.setInt(3, n);
			ResultSet rs = pstmt.executeQuery(); // Query

			List<AchievementLink> achieveList = new ArrayList<AchievementLink>();
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

