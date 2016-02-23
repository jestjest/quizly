package quiz;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class QuizResultsDB {
private Statement stmt;
	
	public QuizResultsDB(DBConnection db) {
		stmt = db.getStatement();
		createQuizResultsTable();
	}
	
	private void createQuizResultsTable() {
		try {
			stmt.executeUpdate("CREATE TABLE IF NOT EXISTS results (resultid INT, quizid INT, userid INT, score DECIMAL(6, 3), time BIGINT, date DATETIME)");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public int addResult(int quizid, int userid, double score, long time, Date date) {
		try {
			ResultSet tableRS = stmt.executeQuery("SELECT resultid FROM results");
			tableRS.last();
			int resultid = tableRS.getRow() + 1;
			
			DateFormat df = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
			
			stmt.executeUpdate("INSERT INTO results VALUES (" + resultid + ", " + quizid + ", " + userid + ", " + score + ", " + time + ", \'" + df.format(date) + "\')" );
			return resultid;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1; /* indicates database error */
	}
	
	public void removeResult(int resultid) {
		try {
			stmt.executeUpdate("DELETE FROM results WHERE resultid = " + resultid);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public int getUserID(int resultid) {
		return getInt(resultid, "userid");
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
	
	private int getInt(int resultid, String field) {
		try {
			ResultSet rs = stmt.executeQuery("SELECT " + field + " FROM results WHERE resultid = " + resultid);
			rs.first();
			return rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1; /* indicates database error */
	}
	
	private double getDouble(int resultid, String field) {
		try {
			ResultSet rs = stmt.executeQuery("SELECT " + field + " FROM results WHERE resultid = " + resultid);
			rs.first();
			return rs.getDouble(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1; /* indicates database error */
	}
	
	private long getLong(int resultid, String field) {
		try {
			ResultSet rs = stmt.executeQuery("SELECT " + field + " FROM results WHERE resultid = " + resultid);
			rs.first();
			return rs.getLong(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1; /* indicates database error */
	}
	
	private Date getDate(int resultid, String field) {
		try {
			ResultSet rs = stmt.executeQuery("SELECT " + field + " FROM results WHERE resultid = " + resultid);
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
// date -- date and time when the user took the quiz

