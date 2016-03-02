package quizme.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import quizme.DBConnection;
import quizme.links.QuizLink;
import quizme.links.QuizSummaryInfo;

public class QuizTable {
	private DBConnection db;
	
	public QuizTable(DBConnection db) {
		this.db = db;
		createQuizTable();
	}
	
	private void createQuizTable() {
		try {
			PreparedStatement pstmt = db.getPreparedStatement("CREATE TABLE IF NOT EXISTS quizes (quizid INT, name VARCHAR(128), description VARCHAR(128), numOfQuestions INT, randomOrder BOOL, "
					+ "onePage BOOL, immediateCorrection BOOL, practiceMode BOOL, creatorUsername VARCHAR(128), modifiedDate DATETIME, numOfTimesTaken INT)");
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public int addQuiz(String name, String description, int numOfQuestions, String creatorUsername, Timestamp modifiedDate, boolean randomOrder, 
			boolean immediateCorrection, boolean onePage, boolean practiceMode, int numOfTimesTaken) {
		try{
			PreparedStatement pstmt1 = db.getPreparedStatement("SELECT quizid FROM quizes");
			ResultSet rs = pstmt1.executeQuery();
			rs.last();
			int quizid = rs.getRow() + 1;

			
			PreparedStatement pstmt2 = db.getPreparedStatement("INSERT INTO quizes VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt2.setInt(1, quizid);
			pstmt2.setString(2, name);
			pstmt2.setString(3, description);
			pstmt2.setInt(4, numOfQuestions);
			pstmt2.setInt(5, (randomOrder) ? 1 : 0);
			pstmt2.setInt(6, (onePage) ? 1 : 0);
			pstmt2.setInt(7, (immediateCorrection) ? 1 : 0);
			pstmt2.setInt(8, (practiceMode) ? 1 : 0); 
			pstmt2.setString(9, creatorUsername);
			pstmt2.setTimestamp(10, modifiedDate);
			pstmt2.setInt(11, numOfTimesTaken);
			pstmt2.executeUpdate();
			
			return quizid;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1; /* indicates database error */
	}
	
	public void removeQuiz(int quizid) {
		try {
			PreparedStatement pstmt = db.getPreparedStatement("DELETE FROM quizes WHERE quizid = ?");
			pstmt.setInt(1, quizid);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public ResultSet getEntry(int quizid) {
		try {
			PreparedStatement pstmt = db.getPreparedStatement("SELECT * FROM quizes where quizid = ?");
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
	
	/* HomePage related functions */
	
	public ResultSet getRecentQuizzesModifiedHelper(int n, Timestamp t) {
		try {
			PreparedStatement pstmt = db.getPreparedStatement("SELECT * FROM quizes "
							+ "WHERE modifiedDate > ? ORDER BY modifiedDate DESC LIMIT ?");
			pstmt.setTimestamp(1, t);
			pstmt.setInt(2, n);
			return pstmt.executeQuery(); 
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public ResultSet getRecentQuizzesModifiedHelper(String username, int n, Timestamp t) {
		try {
			PreparedStatement pstmt = db.getPreparedStatement("SELECT * FROM quizes "
							+ "WHERE modifiedDate > ? AND creatorUsername = ? ORDER BY modifiedDate DESC LIMIT ?");
			pstmt.setTimestamp(1, t);
			pstmt.setString(2, username);
			pstmt.setInt(3, n);
			return pstmt.executeQuery(); 
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Query list of recently created quizzes
	 * @param n an integer determining maximum number of quizzes to be returned
	 * @param t a Timestamp object determining the time after which is considered recent.
	 * @return a chronologically ordered list of QuizLink of the recently created quizzes.
	 */
	public List<QuizLink> getRecentQuizzesCreated( int n, Timestamp t ) {
		try {
			PreparedStatement pstmt = 
					db.getPreparedStatement("SELECT * FROM quizes "
							+ "WHERE createdDate > ? ORDER BY createdDate DESC LIMIT ?");
			pstmt.setTimestamp(1, t);
			pstmt.setInt(2, n);
			ResultSet rs = pstmt.executeQuery(); // Query

			List<QuizLink> quizLinks = new ArrayList<QuizLink>();
			while( rs.next() ) {
				QuizLink quizLink = new QuizLink( rs.getInt("quizid"), rs.getString("name"),
						rs.getString("creatorUsername"), rs.getTimestamp("createdDate"), null, 
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
					db.getPreparedStatement("SELECT * FROM quizes "
							+ "WHERE createdDate > ? AND creatorUsername = ? "
							+ "ORDER BY createdDate DESC LIMIT ?");
			pstmt.setTimestamp(1, t);
			pstmt.setString(2, username);
			pstmt.setInt(3, n);
			ResultSet rs = pstmt.executeQuery(); // Query

			List<QuizLink> quizLinks = new ArrayList<QuizLink>();
			while( rs.next() ) {
				QuizLink quizLink = new QuizLink( rs.getInt("quizid"), rs.getString("name"),
						rs.getString("creatorUsername"), rs.getTimestamp("createdDate"), null,
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
	 * Returns a QuizSummaryInfo object for the given quizID
	 * @param quizID
	 * @return QuizSummaryInfo, null is exception occurs
	 */
	public QuizSummaryInfo getQuizSummaryInfo( int quizID ) {
		try {
			PreparedStatement pstmt = db.getPreparedStatement("SELECT * FROM quizes WHERE quizid = ?");
			pstmt.setInt(1, quizID);
			ResultSet rs = pstmt.executeQuery();
			while( rs.next() ) {
				QuizSummaryInfo quiz = new QuizSummaryInfo( quizID, rs.getString("name"),
						rs.getString("description"), rs.getString("creatorUsername"), 
						rs.getTimestamp("createdDate"), rs.getInt("numOfTimesTaken") );
				return quiz;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	/* helper functions */
	
	private void setString(int quizid, String field, String value) {
		try {
			PreparedStatement pstmt = db.getPreparedStatement("UPDATE quizes SET " + field + " = ?  WHERE quizid = ?");
			pstmt.setString(1, value);
			pstmt.setInt(2, quizid);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private String getString(int quizid, String field) {
		try {
			PreparedStatement pstmt = db.getPreparedStatement("SELECT " + field + " FROM quizes WHERE quizid = ?");
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
			PreparedStatement pstmt = db.getPreparedStatement("UPDATE quizes SET " + field + " = ? WHERE quizid = ?");
			pstmt.setInt(1, value);
			pstmt.setInt(2, quizid);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private int getInt(int quizid, String field) {
		try {
			PreparedStatement pstmt = db.getPreparedStatement("SELECT " + field + " FROM quizes WHERE quizid = ?");
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
			PreparedStatement pstmt = db.getPreparedStatement("UPDATE quizes SET " + field + " = ? WHERE quizid = ?");
			pstmt.setTimestamp(1, value);
			pstmt.setInt(2, quizid);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private Timestamp getDate(int quizid, String field) {
		try {
			PreparedStatement pstmt = db.getPreparedStatement("SELECT " + field + " FROM quizes WHERE quizid = ?");
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

