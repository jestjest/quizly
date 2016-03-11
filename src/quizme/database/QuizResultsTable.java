package quizme.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import quizme.DBConnection;
import quizme.links.QuizLink;
import quizme.links.SummaryStat;

public class QuizResultsTable {
	private DBConnection db;
	
	public QuizResultsTable(DBConnection db) {
		this.db = db;
		createQuizResultsTable();
	}
	
	private void createQuizResultsTable() {
		try {
			PreparedStatement pstmt = db.getPreparedStatement("CREATE TABLE IF NOT EXISTS results (resultid INT AUTO_INCREMENT primary key "
											+ "NOT NULL	, quizid INT, username VARCHAR(128), score DECIMAL(6, 3), time BIGINT, date DATETIME)");
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public int addResult(int quizid, String username, double score, long time, Timestamp date) {
		try {
			PreparedStatement pstmt1 = db.getPreparedStatement("INSERT INTO results (quizid, username, score, time, date) VALUES (?, ?, ?, ?, ?)");
			pstmt1.setInt(1, quizid);
			pstmt1.setString(2, username);
			pstmt1.setDouble(3, score);
			pstmt1.setFloat(4, time);
			pstmt1.setTimestamp(5, date); 
			pstmt1.executeUpdate();
			
			PreparedStatement pstmt2 = db.getPreparedStatement("SELECT resultid FROM results ORDER BY resultid ASC");
			ResultSet rs = pstmt2.executeQuery();
			rs.last();
			return rs.getInt(1);
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
	
	public void removeAllQuizResultsByName(String quizName) {
		try {
			PreparedStatement pstmt1 = db.getPreparedStatement("SELECT quizid FROM quizzes WHERE name = ?");
			pstmt1.setString(1, quizName);
			ResultSet rs = pstmt1.executeQuery();
			rs.next();
			int quizid = rs.getInt(1); 
			PreparedStatement pstmt2 = db.getPreparedStatement("DELETE FROM results WHERE quizid = ?");
			pstmt2.setInt(1, quizid);
			pstmt2.executeUpdate();
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
	
	public int numOfQuizzesTakenHelper(Timestamp t) {
		try {
			PreparedStatement pstmt = db.getPreparedStatement("SELECT COUNT(resultid) FROM results " 
				+ "WHERE date > ?");
			pstmt.setTimestamp(1, t);
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
	public int[] numOfQuizzesTaken() {
		int[] numOfQuizzesTaken = new int[3];
		
		Timestamp lastDay = new Timestamp(System.currentTimeMillis() - dayDuration);
		numOfQuizzesTaken[0] = numOfQuizzesTakenHelper(lastDay); 
		
		Timestamp lastWeek = new Timestamp(System.currentTimeMillis() - weekDuration);
		numOfQuizzesTaken[1] = numOfQuizzesTakenHelper(lastWeek);  
		
		Timestamp allTime = new Timestamp(0);
		numOfQuizzesTaken[2] = numOfQuizzesTakenHelper(allTime); 
		return numOfQuizzesTaken;
	}
	
	public double getHighScore(int quizid) {
		try {
			PreparedStatement pstmt = db.getPreparedStatement("SELECT score FROM results WHERE quizid = ? ORDER BY score DESC");
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) return rs.getDouble("score");
		} catch ( SQLException e) {
			e.printStackTrace();
		} 
		return -1;
	}
	
	/* HomePage related functions */
	
	
	public ResultSet getRecentQuizzesTakenHelper(String username, int n, Timestamp t ) {
		try {
			PreparedStatement pstmt =  db.getPreparedStatement("SELECT * FROM results INNER JOIN quizzes USING(quizid)"
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
					db.getPreparedStatement("SELECT *, COUNT(quizid) AS quiz_count FROM results INNER JOIN quizzes USING(quizid) "
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
	public List<QuizLink> getPopularQuizzes( int n, Timestamp t ) {
		try {
			PreparedStatement pstmt = 
					db.getPreparedStatement("SELECT *, COUNT(quizid) AS quiz_count FROM results "
							+ "INNER JOIN quizzes USING(quizid) "
							+ "WHERE date > ? GROUP BY quizid ORDER BY quiz_count DESC LIMIT ?");
			pstmt.setTimestamp(1, t);
			pstmt.setInt(2, n);
			ResultSet rs = pstmt.executeQuery(); // Quarry

			List<QuizLink> quizLinks = new ArrayList<QuizLink>();
			while( rs.next() ) {
				QuizLink quizLink = new QuizLink( rs.getInt("quizid"), rs.getString("name"),
						rs.getString("creatorUsername"), rs.getTimestamp("modifiedDate"), 
						rs.getTimestamp("date"), rs.getInt("numOfTimesTaken"), 
						rs.getString("username"), rs.getFloat("score"));
				quizLinks.add(quizLink);
			}
			return quizLinks;
		} catch( SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Quarry list of recently taken quizzes by a person determined by its user name
	 * @param username A String containing user name of a person
	 * @param n an integer determining maximum number of quizzes to be returned
	 * @param t a Timstamp object determining the time after which is considered recent.
	 * @return a chronologically ordered list of QuizLink of the recently taken quizzes
	 * by a specific person.
	 */
	public List<QuizLink> getRecentQuizzesTaken( String username, int n, Timestamp t ) {
		try {
			PreparedStatement pstmt = 
					db.getPreparedStatement("SELECT * FROM results "
							+ "INNER JOIN quizzes USING(quizid) "
							+ "WHERE date > ? AND username = ? "
							+ "ORDER BY modifiedDate DESC LIMIT ?");
			pstmt.setTimestamp(1, t);
			pstmt.setString(2, username);
			pstmt.setInt(3, n);
			ResultSet rs = pstmt.executeQuery(); // Quarry
			
			ArrayList<QuizLink> quizLinks = new ArrayList<QuizLink>();
			while( rs.next() ) {
				QuizLink quizLink = new QuizLink( rs.getInt("quizid"), rs.getString("name"),
						rs.getString("creatorUsername"), rs.getTimestamp("modifiedDate"), 
						rs.getTimestamp("date"), rs.getInt("numOfTimesTaken"), 
						rs.getString("username"), rs.getFloat("score"));
				quizLinks.add(quizLink);
			}
			return quizLinks;
		} catch( SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public SummaryStat getUserSummaryStat( String username ) {
		try {
			SummaryStat mySummaryStat = new SummaryStat();
			// first get count
			PreparedStatement pstmt = db.getPreparedStatement("SELECT COUNT(username) AS num_taken FROM results "
					+ "WHERE username = ?");
			pstmt.setString(1, username);
			ResultSet rs = pstmt.executeQuery();
			if ( rs.next() ) {
				mySummaryStat.numberTaken = rs.getInt("num_taken");
			}
			// get other stats
			pstmt = db.getPreparedStatement("SELECT MIN(score) AS minScore, MAX(score) AS maxScore, "
					+ "AVG(score) AS meanScore, MIN(time) as minTime, MAX(time) AS maxTime, "
					+ "AVG(time) AS meanTime FROM results WHERE username = ?");
			pstmt.setString(1, username);		
			rs = pstmt.executeQuery();
			if ( rs.next() ) {
				mySummaryStat.minScore = rs.getFloat("minScore");
				mySummaryStat.maxScore = rs.getFloat("maxScore");
				mySummaryStat.meanScore = rs.getFloat("meanScore");
				mySummaryStat.minTime = rs.getLong("minTime");
				mySummaryStat.maxTime = rs.getLong("maxTime");
				mySummaryStat.meanTime = rs.getDouble("meanTime");
			}
			return mySummaryStat;
		} catch ( SQLException e ) {
			e.printStackTrace();
		}
		return null;
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
