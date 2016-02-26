package quizme;

import java.sql.*;
import java.util.Date;

public class QuizResultsDB {
	private DBConnection db;
	
	public QuizResultsDB(DBConnection db) {
		this.db = db;
		createQuizResultsTable();
	}
	
	private void createQuizResultsTable() {
		try {
			PreparedStatement pstmt = db.getPreparedStatement("CREATE TABLE IF NOT EXISTS results (resultid INT, quizid INT, username VARCHAR(128), score DECIMAL(6, 3), time BIGINT, date DATE)");
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public int addResult(int quizid, String username, double score, long time, java.sql.Date date) {
		try {
			PreparedStatement pstmt1 = db.getPreparedStatement("SELECT resultid FROM results");
			ResultSet rs = pstmt1.executeQuery();
			rs.last();
			int resultid = rs.getRow() + 1;
			
			PreparedStatement pstmt2 = db.getPreparedStatement("INSERT INTO results VALUES (?, ?, ?, ?, ?, ?)");
			pstmt2.setInt(1, resultid);
			pstmt2.setInt(2, quizid);
			pstmt2.setString(3, username);
			pstmt2.setDouble(4, score);
			pstmt2.setFloat(5, time);
			pstmt2.setDate(6, date); 
			pstmt2.executeUpdate();
			return resultid;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1; /* indicates database error */
	}
	
	public void removeResult(int resultid) {
		try {
			PreparedStatement pstmt = db.getPreparedStatement("DELETE FROM results WHERE resultid = ?");
			pstmt.setInt(1, resultid);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public String getUsername(int resultid) {
		return getString(resultid, "username");
	}
	
	public int getQuizID(int resultid) {
		return getInt(resultid, "quizid");
	}
	
	public double getScore(int resultid) {
		return getDouble(resultid, "score");
	}
	
	public long getTime(int resultid) {
		return getLong(resultid, "time");
	}
	
	public Date getDate(int resultid) {
		return getDate(resultid, "date");
	}
	
	/* helper functions */
	
	private String getString(int resultid, String field) {
		try {
			PreparedStatement pstmt = db.getPreparedStatement("SELECT " + field + " FROM results WHERE resultid = ?");
			pstmt.setInt(1, resultid);
			ResultSet rs = pstmt.executeQuery();
			rs.first();
			return rs.getString(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null; /* indicates database error */
	}
	
	private int getInt(int resultid, String field) {
		try {
			PreparedStatement pstmt = db.getPreparedStatement("SELECT " + field + " FROM results WHERE resultid = ?");
			pstmt.setInt(1, resultid);
			ResultSet rs = pstmt.executeQuery();
			rs.first();
			return rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1; /* indicates database error */
	}
	
	private double getDouble(int resultid, String field) {
		try {
			PreparedStatement pstmt = db.getPreparedStatement("SELECT " + field + " FROM results WHERE resultid = ?");
			pstmt.setInt(1, resultid);
			ResultSet rs = pstmt.executeQuery();
			rs.first();
			return rs.getDouble(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1; /* indicates database error */
	}
	
	private long getLong(int resultid, String field) {
		try {
			PreparedStatement pstmt = db.getPreparedStatement("SELECT " + field + " FROM results WHERE resultid = ?");
			pstmt.setInt(1, resultid);
			ResultSet rs = pstmt.executeQuery();
			rs.first();
			return rs.getLong(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1; /* indicates database error */
	}
	
	private Date getDate(int resultid, String field) {
		try {
			PreparedStatement pstmt = db.getPreparedStatement("SELECT " + field + " FROM results WHERE resultid = ?");
			pstmt.setInt(1, resultid);
			ResultSet rs = pstmt.executeQuery();
			rs.first();
			return rs.getTimestamp(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null; /* indicates database error */
	}
}

//Notes: 
// score -- number of questions correct / total number of questions (expressed as a percentage)
// time -- how long it took the user to complete the quiz
// date -- date when the user took the quiz
