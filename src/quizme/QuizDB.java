package quizme;

import java.sql.*;

public class QuizDB {
	private DBConnection db;
	
	public QuizDB(DBConnection db) {
		this.db = db;
		createQuizTable();
	}
	
	private void createQuizTable() {
		try {
<<<<<<< HEAD
			PreparedStatement pstmt = db.getPreparedStatement("CREATE TABLE IF NOT EXISTS quizes ( quizid INT, name VARCHAR(128), description VARCHAR(128), purpose VARCHAR(128), numOfQuestions INT, randomOrder BOOL, "
=======
			stmt.executeUpdate("CREATE TABLE IF NOT EXISTS quizzes (quizid INT, name CHAR(64), description CHAR(64), numOfQuestions INT, randomOrder BOOL, "
>>>>>>> origin/master
					+ "multiplePages BOOL, immediateCorrection BOOL)");
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public int addQuiz(String name, String description, String purpose) {
		try{
			PreparedStatement pstmt1 = db.getPreparedStatement("SELECT quizid FROM quizes");
			ResultSet rs = pstmt1.executeQuery();
			rs.last();
			int quizid = rs.getRow() + 1;

			
			PreparedStatement pstmt2 = db.getPreparedStatement("INSERT INTO quizes VALUES ( ?, ?, ?, ?, 0, 0, 0, 0)");
			pstmt2.setInt(1, quizid);
			pstmt2.setString(2, name);
			pstmt2.setString(3, description);
			pstmt2.setString(4, purpose);
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
	
	public void setPurpose(int quizid, String purpose) {
		setString(quizid, "purpose", purpose);
	}
	
	public String getPurpose(int quizid) {
		return getString(quizid, "purpose");
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
	
	public void setMultiplePages(int quizid, boolean multiplePages) {
		int multiplePagesNum = (multiplePages) ? 1 : 0;
		setInt(quizid, "multiplePages", multiplePagesNum);
	}
	
	public boolean getMultiplePages(int quizid) {
		return (getInt(quizid, "multiplePages") > 0) ? true : false;
	}
	
	public void setImmediateCorrection(int quizid, boolean immediateCorrection) {
		int immediateCorrectionNum = (immediateCorrection) ? 1 : 0;
		setInt(quizid, "immediateCorrection", immediateCorrectionNum);
	}
	
	public boolean getImmediateCorrection(int quizid) {
		return (getInt(quizid, "immediateCorrection") > 0) ? true : false;
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
	
}


