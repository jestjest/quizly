package quizme.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import quizme.DBConnection;
import quizme.links.*;

public class QuizTable {
	private DBConnection db;

	public QuizTable(DBConnection db) {
		this.db = db;
		createQuizTable();
	}

	private void createQuizTable() {
		try {
			PreparedStatement pstmt = db.getPreparedStatement("CREATE TABLE IF NOT EXISTS quizzes"
					+ " (quizid INT AUTO_INCREMENT primary key NOT NULL, name VARCHAR(128), description VARCHAR(128), numOfQuestions INT, randomOrder BOOL, "
					+ "onePage BOOL, immediateCorrection BOOL, practiceMode BOOL, "
					+ "creatorUsername VARCHAR(128), modifiedDate DATETIME, numOfTimesTaken INT)");
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public int addQuiz(String name, String description, int numOfQuestions, String creatorUsername, Timestamp modifiedDate, boolean randomOrder, 
			boolean immediateCorrection, boolean onePage, boolean practiceMode, int numOfTimesTaken) {
		try{
			PreparedStatement pstmt1 = db.getPreparedStatement("INSERT INTO quizzes (name, description, numOfQuestions, randomOrder, "
					+ "onePage, immediateCorrection, practiceMode, creatorUsername, modifiedDate, numOfTimesTaken) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt1.setString(1, name);
			pstmt1.setString(2, description);
			pstmt1.setInt(3, numOfQuestions);
			pstmt1.setInt(4, (randomOrder) ? 1 : 0);
			pstmt1.setInt(5, (onePage) ? 1 : 0);
			pstmt1.setInt(6, (immediateCorrection) ? 1 : 0);
			pstmt1.setInt(7, (practiceMode) ? 1 : 0); 
			pstmt1.setString(8, creatorUsername);
			pstmt1.setTimestamp(9, modifiedDate);
			pstmt1.setInt(10, numOfTimesTaken);
			pstmt1.executeUpdate();

			PreparedStatement pstmt2 = db.getPreparedStatement("SELECT MAX(quizid) as quizid FROM quizzes");
			ResultSet rs = pstmt2.executeQuery();
			rs.last();
			return rs.getInt("quizid");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1; /* indicates database error */
	}

	public void removeQuiz(int quizid) {
		try {
			PreparedStatement pstmt = db.getPreparedStatement("DELETE FROM quizzes WHERE quizid = ?");
			pstmt.setInt(1, quizid);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public ResultSet getEntry(int quizid) {
		try {
			PreparedStatement pstmt = db.getPreparedStatement("SELECT * FROM quizzes where quizid = ?");
			pstmt.setInt(1, quizid);
			ResultSet rs = pstmt.executeQuery();
			rs.first();
			return rs;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null; /* indicates database error */
	}

	public String getName(int quizid) {
		return getString(quizid, "name");
	}

	public void setDescription(int quizid, String description) {
		setString(quizid, "description", description);
	}

	public String getDescription(int quizid) {
		return getString(quizid, "description");
	}

	public void incNumOfTimesTaken(int quizid) {
		int priorNumOfTimesTaken = getNumOfTimesTaken(quizid);
		setInt(quizid, "numOfTimesTaken", priorNumOfTimesTaken + 1);
	}

	public int getNumOfTimesTaken(int quizid) {
		return getInt(quizid, "numOfTimesTaken");
	}

	public void setNumOfQuestions(int quizid, int numOfQuestions) {
		setInt(quizid, "numOfQuestions", numOfQuestions);
	}

	public int getNumOfQuestions(int quizid) {
		return getInt(quizid, "numOfQuestions");
	}

	public void setRandomOrder(int quizid, boolean randomOrder) {
		int randomOrderNum = (randomOrder) ? 1 : 0;
		setInt(quizid, "randomOrder", randomOrderNum);
	}

	public boolean getRandomOrder(int quizid) {
		return (getInt(quizid, "randomOrder") > 0) ? true : false;
	}

	public void setOnePage(int quizid, boolean multiplePages) {
		int multiplePagesNum = (multiplePages) ? 1 : 0;
		setInt(quizid, "onePage", multiplePagesNum);
	}

	public boolean getOnePage(int quizid) {
		return (getInt(quizid, "onePage") > 0) ? true : false;
	}

	public void setImmediateCorrection(int quizid, boolean immediateCorrection) {
		int immediateCorrectionNum = (immediateCorrection) ? 1 : 0;
		setInt(quizid, "immediateCorrection", immediateCorrectionNum);
	}

	public boolean getImmediateCorrection(int quizid) {
		return (getInt(quizid, "immediateCorrection") > 0) ? true : false;
	}

	public void setPracticeMode(int quizid, boolean practiceMode) {
		int practiceModeNum = (practiceMode) ? 1 : 0;
		setInt(quizid, "practiceMode", practiceModeNum);
	}

	public boolean getPracticeMode(int quizid) {
		return (getInt(quizid, "practiceMode") > 0) ? true : false;
	}

	public String getCreatorUsername(int quizid) {
		return getString(quizid, "creatorUsername");
	}

	public void setModifiedDate(int quizid, Timestamp date) {
		setDate(quizid, "modifiedDate", date);
	}

	public Timestamp getModifiedDate(int quizid) {
		return getDate(quizid, "modifiedDate");
	}

	public int numOfQuizzesCreatedHelper(Timestamp t) {
		try {
			PreparedStatement pstmt = db.getPreparedStatement("SELECT COUNT(quizid) FROM quizzes " 
				+ "WHERE modifiedDate > ?");
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
	public int[] numOfQuizzesCreated() {
		int[] numOfQuizzesCreated = new int[3];
		
		Timestamp lastDay = new Timestamp(System.currentTimeMillis() - dayDuration);
		numOfQuizzesCreated[0] = numOfQuizzesCreatedHelper(lastDay); 
		
		Timestamp lastWeek = new Timestamp(System.currentTimeMillis() - weekDuration);
		numOfQuizzesCreated[1] = numOfQuizzesCreatedHelper(lastWeek);  
		
		Timestamp allTime = new Timestamp(0);
		numOfQuizzesCreated[2] = numOfQuizzesCreatedHelper(allTime); 
		return numOfQuizzesCreated;
	}
	
	/* HomePage related functions */
	/**
	 * Query list of recently created quizzes
	 * @param n an integer determining maximum number of quizzes to be returned
	 * @param t a Timestamp object determining the time after which is considered recent.
	 * @return a chronologically ordered list of QuizLink of the recently created quizzes.
	 */
	public List<QuizLink> getRecentQuizzesCreated( int n, Timestamp t ) {
		try {
			PreparedStatement pstmt = 
					db.getPreparedStatement("SELECT * FROM quizzes "
							+ "WHERE modifiedDate > ? ORDER BY modifiedDate DESC LIMIT ?");
			pstmt.setTimestamp(1, t);
			pstmt.setInt(2, n);
			ResultSet rs = pstmt.executeQuery(); // Query

			List<QuizLink> quizLinks = new ArrayList<QuizLink>();
			while( rs.next() ) {
				QuizLink quizLink = new QuizLink( rs.getInt("quizid"), rs.getString("name"),
						rs.getString("creatorUsername"), rs.getTimestamp("modifiedDate"), null, 
						rs.getInt("numOfTimesTaken"), null, 0);
				quizLinks.add(quizLink);
			}
			return quizLinks;
		} catch( SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<QuizLink> getRecentValidQuizzesCreated( int n, Timestamp t ) {
		try {
			PreparedStatement pstmt = 
					db.getPreparedStatement("SELECT * FROM quizzes "
							+ "WHERE modifiedDate > ? AND numOfQuestions <> -1 ORDER BY modifiedDate DESC LIMIT ?");
			pstmt.setTimestamp(1, t);
			pstmt.setInt(2, n);
			ResultSet rs = pstmt.executeQuery(); // Query

			List<QuizLink> quizLinks = new ArrayList<QuizLink>();
			while( rs.next() ) {
				QuizLink quizLink = new QuizLink( rs.getInt("quizid"), rs.getString("name"),
						rs.getString("creatorUsername"), rs.getTimestamp("modifiedDate"), null, 
						rs.getInt("numOfTimesTaken"), null, 0);
				quizLinks.add(quizLink);
			}
			return quizLinks;
		} catch( SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Query list of recently created quizzes by a person determined by its user name
	 * @param username A String containing user name of a person
	 * @param n an integer determining maximum number of quizzes to be returned
	 * @param t a Timstamp object determining the time after which is considered recent.
	 * @return a chronologically ordered list of QuizLink of the recently created quizzes
	 * by a specific person.
	 */
	public List<QuizLink> getRecentQuizzesCreated( String username, int n, Timestamp t ) {
		try {
			PreparedStatement pstmt = 
					db.getPreparedStatement("SELECT * FROM quizzes "
							+ "WHERE modifiedDate > ? AND creatorUsername = ? "
							+ "ORDER BY modifiedDate DESC LIMIT ?");
			pstmt.setTimestamp(1, t);
			pstmt.setString(2, username);
			pstmt.setInt(3, n);
			ResultSet rs = pstmt.executeQuery(); // Query

			List<QuizLink> quizLinks = new ArrayList<QuizLink>();
			while( rs.next() ) {
				QuizLink quizLink = new QuizLink( rs.getInt("quizid"), rs.getString("name"),
						rs.getString("creatorUsername"), rs.getTimestamp("modifiedDate"), null,
						rs.getInt("numOfTimesTaken"), null, 0);
				quizLinks.add(quizLink);
			}
			return quizLinks;
		} catch( SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Returns a QuizSummaryInfo including all information about a quiz to 
	 * be shown in its summary page visited by a [username]
	 * @param quizID
	 * @param username user name of the user who is visiting this quiz
	 * @param recentTime Timestamp of a time after which is considered recent 
	 * @param lastDayTime Timestamp of a time exactly one day before now
	 * @param n maximum number of results for performance lists
	 * @return
	 */
	public QuizSummaryInfo getQuizSummaryInfo( int quizID, String username,
			Timestamp recentTime, Timestamp lastDayTime, int n) {
		try {
			////////////////////////////
			// get user's performance //
			////////////////////////////
			PreparedStatement pstmt = db.getPreparedStatement("SELECT * FROM results "
					+ "WHERE quizid = ? AND username = ? "
					+ "ORDER BY date DESC LIMIT ?");
			pstmt.setInt(1, quizID);
			pstmt.setString(2, username);
			pstmt.setInt(3, n);			
			ResultSet rs = pstmt.executeQuery();

			List<Performance> myPerformances = new ArrayList<Performance>();

			while ( rs.next() ) {
				Performance userPerformance = new Performance(username, rs.getFloat("score"), 
						rs.getLong("time"), rs.getTimestamp("date"));
				myPerformances.add( userPerformance );		
			}

			/////////////////////////////////////
			// get all time highest Performers //
			/////////////////////////////////////
			pstmt = db.getPreparedStatement("SELECT * FROM results "
					+ "WHERE quizid = ? "
					+ "ORDER BY score DESC, time ASC LIMIT ?");
			pstmt.setInt(1, quizID);
			pstmt.setInt(2, n);			
			rs = pstmt.executeQuery();

			List<Performance> highestPerformers = new ArrayList<Performance>();

			while ( rs.next() ) {
				Performance highPerformance = new Performance( rs.getString("username"), rs.getFloat("score"), 
						rs.getLong("time"), rs.getTimestamp("date"));
				highestPerformers.add( highPerformance );
			}
			
			//////////////////////
			// get top last day //
			//////////////////////
			pstmt = db.getPreparedStatement("SELECT * FROM results "
					+ "WHERE quizid = ? AND date > ? "
					+ "ORDER BY score DESC, time ASC LIMIT ?");
			pstmt.setInt(1, quizID);
			pstmt.setTimestamp(2, lastDayTime);
			pstmt.setInt(3, n);			
			rs = pstmt.executeQuery();

			List<Performance> topLastDayPerformers = new ArrayList<Performance>();

			while ( rs.next() ) {
				Performance highPerformance = new Performance( rs.getString("username"), rs.getFloat("score"), 
						rs.getLong("time"), rs.getTimestamp("date"));
				topLastDayPerformers.add( highPerformance );
			}

			///////////////////////
			// recent performers //
			///////////////////////
			pstmt = db.getPreparedStatement("SELECT * FROM results "
					+ "WHERE quizid = ? AND date > ? "
					+ "ORDER BY date DESC LIMIT ?");
			pstmt.setInt(1, quizID);
			pstmt.setTimestamp(2, recentTime);
			pstmt.setInt(3, n);			
			rs = pstmt.executeQuery();

			List<Performance> recentPerformers = new ArrayList<Performance>();

			while ( rs.next() ) {
				Performance performance = new Performance( rs.getString("username"), rs.getFloat("score"), 
						rs.getLong("time"), rs.getTimestamp("date"));
				recentPerformers.add( performance );
			}

			////////////////////////////
			// get user summary stats //
			////////////////////////////
			SummaryStat mySummaryStat = new SummaryStat();
			// first get count
			pstmt = db.getPreparedStatement("SELECT COUNT(username) AS num_taken FROM results "
					+ "WHERE quizid = ? AND username = ?");
			pstmt.setInt(1, quizID);
			pstmt.setString(2, username);
			rs = pstmt.executeQuery();
			if ( rs.next() ) {
				mySummaryStat.numberTaken = rs.getInt("num_taken");
			}
			// get other stats
			pstmt = db.getPreparedStatement("SELECT MIN(score) AS minScore, MAX(score) AS maxScore, "
					+ "AVG(score) AS meanScore, MIN(time) as minTime, MAX(time) AS maxTime, "
					+ "AVG(time) AS meanTime FROM results WHERE quizid = ? AND username = ?");
			pstmt.setInt(1, quizID);
			pstmt.setString(2, username);		
			rs = pstmt.executeQuery();
			if ( rs.next() ) {
				mySummaryStat.minScore = rs.getFloat("minScore");
				mySummaryStat.maxScore = rs.getFloat("maxScore");
				mySummaryStat.meanScore = rs.getFloat("meanScore");
				mySummaryStat.minTime = rs.getLong("minTime");
				mySummaryStat.maxTime = rs.getLong("maxTime");
				mySummaryStat.meanTime = rs.getDouble("meanTime");
			}
			
			///////////////////////////
			// get all summary stats //
			///////////////////////////
			SummaryStat allSummaryStat = new SummaryStat();
			// first get count
			pstmt = db.getPreparedStatement("SELECT COUNT(quizid) AS num_taken FROM results "
					+ "WHERE quizid = ?");
			pstmt.setInt(1, quizID);
			rs = pstmt.executeQuery();
			if ( rs.next() ) {
				allSummaryStat.numberTaken = rs.getInt("num_taken");
			}
			// get other stats
			pstmt = db.getPreparedStatement("SELECT MIN(score) AS minScore, MAX(score) AS maxScore, "
					+ "AVG(score) AS meanScore, MIN(time) as minTime, MAX(time) AS maxTime, "
					+ "AVG(time) AS meanTime FROM results WHERE quizid = ?");
			pstmt.setInt(1, quizID);
			rs = pstmt.executeQuery();
			if ( rs.next() ) {
				allSummaryStat.minScore = rs.getFloat("minScore");
				allSummaryStat.maxScore = rs.getFloat("maxScore");
				allSummaryStat.meanScore = rs.getFloat("meanScore");
				allSummaryStat.minTime = rs.getLong("minTime");
				allSummaryStat.maxTime = rs.getLong("maxTime");
				allSummaryStat.meanTime = rs.getDouble("meanTime");
			}
			
			///////////////////////////
			// get general quiz info //
			///////////////////////////			
			pstmt = db.getPreparedStatement("SELECT * FROM quizzes WHERE quizid = ?");
			pstmt.setInt(1, quizID);
			rs = pstmt.executeQuery();
			
			QuizSummaryInfo quizSummaryInfo = null;			
			
			if ( rs.next() ) {
				quizSummaryInfo = new QuizSummaryInfo(quizID, rs.getString("name"), rs.getString("description"),
						rs.getString("creatorUsername"), rs.getTimestamp("modifiedDate"), 
						rs.getBoolean("practiceMode"), rs.getInt("numOfQuestions"), 
						rs.getBoolean("randomOrder"), rs.getBoolean("onePage"), 
						rs.getBoolean("immediateCorrection"), mySummaryStat, allSummaryStat, myPerformances, 
						highestPerformers, topLastDayPerformers, recentPerformers);
			}
			return quizSummaryInfo;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	/* helper functions */

	private void setString(int quizid, String field, String value) {
		try {
			PreparedStatement pstmt = db.getPreparedStatement("UPDATE quizzes SET " + field + " = ?  WHERE quizid = ?");
			pstmt.setString(1, value);
			pstmt.setInt(2, quizid);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private String getString(int quizid, String field) {
		try {
			PreparedStatement pstmt = db.getPreparedStatement("SELECT " + field + " FROM quizzes WHERE quizid = ?");
			pstmt.setInt(1, quizid);
			ResultSet rs = pstmt.executeQuery();
			rs.first();
			return rs.getString(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null; /* indicates database error */
	}

	private void setInt(int quizid, String field, int value) {
		try {
			PreparedStatement pstmt = db.getPreparedStatement("UPDATE quizzes SET " + field + " = ? WHERE quizid = ?");
			pstmt.setInt(1, value);
			pstmt.setInt(2, quizid);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private int getInt(int quizid, String field) {
		try {
			PreparedStatement pstmt = db.getPreparedStatement("SELECT " + field + " FROM quizzes WHERE quizid = ?");
			pstmt.setInt(1, quizid);
			ResultSet rs = pstmt.executeQuery();
			rs.first();
			return rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1; /* indicates database error */
	}

	private void setDate(int quizid, String field, Timestamp value) {
		try {
			PreparedStatement pstmt = db.getPreparedStatement("UPDATE quizzes SET " + field + " = ? WHERE quizid = ?");
			pstmt.setTimestamp(1, value);
			pstmt.setInt(2, quizid);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private Timestamp getDate(int quizid, String field) {
		try {
			PreparedStatement pstmt = db.getPreparedStatement("SELECT " + field + " FROM quizzes WHERE quizid = ?");
			pstmt.setInt(1, quizid);
			ResultSet rs = pstmt.executeQuery();
			rs.first();
			return rs.getTimestamp(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null; /* indicates database error */
	}

}

