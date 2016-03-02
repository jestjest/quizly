package quizme.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import quizme.DBConnection;
import quizme.links.QuizLink;

public class QuizResultsTable {
	private DBConnection db;
	
	public QuizResultsTable(DBConnection db) {
		this.db = db;
		createQuizResultsTable();
	}
	
	private void createQuizResultsTable() {
		try {
			PreparedStatement pstmt = db.getPreparedStatement("CREATE TABLE IF NOT EXISTS results (resultid INT, quizid INT, username VARCHAR(128), score DECIMAL(6, 3), time BIGINT, date DATETIME)");
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public int addResult(int quizid, String username, double score, long time, Timestamp date) {
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
			pstmt2.setTimestamp(6, date); 
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
	
	public Timestamp getDate(int resultid) {
		return getDate(resultid, "date");
	}
	
	/* HomePage related functions */
	
	
	public ResultSet getRecentQuizzesTakenHelper(String username, int n, Timestamp t ) {
		try {
			PreparedStatement pstmt =  db.getPreparedStatement("SELECT * FROM results INNER JOIN quizes USING(quizid)"
					+ "WHERE date > ? AND username = ? ORDER BY date DESC LIMIT ?");
			
			pstmt.setTimestamp(1, t);
			pstmt.setString(2, username);
			pstmt.setInt(3, n);
			return pstmt.executeQuery();
		} catch ( SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public ResultSet getPopularQuizzesHelper( String username, int n, Timestamp t ) {
		try {
			PreparedStatement pstmt = 
					db.getPreparedStatement("SELECT *, COUNT(quizid) AS quiz_count FROM results INNER JOIN quizes USING(quizid) "
							+ "WHERE date > ? GROUP BY quizid ORDER BY quiz_count DESC LIMIT ?");
			pstmt.setTimestamp(1, t);
			pstmt.setInt(2, n);
			return pstmt.executeQuery(); 
		} catch ( SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Quarry list of most popular quizzes among the recently taken quizzes
	 * @param n an integer determining maximum number of quizzes to be returned
	 * @param t a Timstamp object determining the time after which is considered recent.
	 * @return an ordered by frequency list of QuizLink of the popular quizzes.
	 */
	/*public List<QuizLink> getPopularQuizzes( int n, Timestamp t ) {
		try {
			PreparedStatement pstmt = 
					db.getPreparedStatement("SELECT *, COUNT(quizid) AS quiz_count FROM results "
							+ "INNER JOIN quizes USING(quizid) "
							+ "WHERE date > ? GROUP BY quizid ORDER BY quiz_count DESC LIMIT ?");
			pstmt.setTimestamp(1, t);
			pstmt.setInt(2, n);
			ResultSet rs = pstmt.executeQuery(); // Quarry

			List<QuizLink> quizLinks = new ArrayList<QuizLink>();
			while( rs.next() ) {
				QuizLink quizLink = new QuizLink( rs.getInt("quizid"), rs.getString("name"),
						rs.getString("creatorUsername"), rs.getTimestamp("createdDate"), 
						rs.getTimestamp("date"), rs.getInt("numOfTimesTaken"), 
						rs.getString("username"), rs.getFloat("score"));
				quizLinks.add(quizLink);
			}
			return quizLinks;
		} catch( SQLException e) {
			e.printStackTrace();
		}
		return null;
	}*/
	
	
	/**
	 * Quarry list of recently taken quizzes by a person determined by its user name
	 * @param username A String containing user name of a person
	 * @param n an integer determining maximum number of quizzes to be returned
	 * @param t a Timstamp object determining the time after which is considered recent.
	 * @return a chronologically ordered list of QuizLink of the recently taken quizzes
	 * by a specific person.
	 */
	/*public List<QuizLink> getRecentQuizzesTaken( String username, int n, Timestamp t ) {
		try {
			PreparedStatement pstmt = 
					db.getPreparedStatement("SELECT * FROM results "
							+ "INNER JOIN quizes USING(quizid) "
							+ "WHERE date > ? AND username = ? "
							+ "ORDER BY createdDate DESC LIMIT ?");
			pstmt.setTimestamp(1, t);
			pstmt.setString(2, username);
			pstmt.setInt(3, n);
			ResultSet rs = pstmt.executeQuery(); // Quarry
			
			ArrayList<QuizLink> quizLinks = new ArrayList<QuizLink>();
			while( rs.next() ) {
				QuizLink quizLink = new QuizLink( rs.getInt("quizid"), rs.getString("name"),
						rs.getString("creatorUsername"), rs.getTimestamp("createdDate"), 
						rs.getTimestamp("date"), rs.getInt("numOfTimesTaken"), 
						rs.getString("username"), rs.getFloat("score"));
				quizLinks.add(quizLink);
			}
			return quizLinks;
		} catch( SQLException e) {
			e.printStackTrace();
		}
		return null;
	}*/
	
	
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
	
	private Timestamp getDate(int resultid, String field) {
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
