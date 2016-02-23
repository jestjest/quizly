package quizme;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class QuizDB {
	private Statement stmt;
	
	public QuizDB(DBConnection db) {
		stmt = db.getStatement();
		createQuizTable();
	}
	
	private void createQuizTable() {
		try {
			stmt.executeUpdate("CREATE TABLE IF NOT EXISTS quizes (quizid INT, name CHAR(64), description CHAR(64), numOfQuestions INT, randomOrder BOOL, "
					+ "multiplePages BOOL, immediateCorrection BOOL)");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public int addQuiz(String name, String description) {
		try{
			ResultSet tableRS = stmt.executeQuery("SELECT quizid FROM quizes");
			tableRS.last();
			int quizid = tableRS.getRow() + 1;
			
			stmt.executeUpdate("INSERT INTO quizes VALUES (" + quizid + ", \"" + name + "\", \"" + description + "\", 0, 0, 0, 0)" );
			
			return quizid;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1; /* indicates database error */
	}
	
	public void removeQuiz(int quizid) {
		try {
			stmt.executeUpdate("DELETE FROM quizes WHERE quizid = " + quizid);
		} catch (SQLException e) {
			e.printStackTrace();
		}
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
	
	//Add incNumOfQuestions and decNumOfQuestions?
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
			stmt.executeUpdate("UPDATE quizes SET " +  field + " = \"" + value + "\" where quizid = " + quizid);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private String getString(int quizid, String field) {
		try {
			ResultSet rs = stmt.executeQuery("SELECT " +  field + " FROM quizes where quizid = " + quizid);
			rs.first();
			return rs.getString(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null; /* indicates database error */
	}
	
	private void setInt(int quizid, String field, int value) {
		try {
			stmt.executeUpdate("UPDATE quizes SET " + field + " = " + value + " WHERE quizid = " + quizid);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private int getInt(int quizid, String field) {
		try {
			ResultSet rs = stmt.executeQuery("SELECT " + field + " FROM quizes WHERE quizid = " + quizid);
			rs.first();
			return rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1; /* indicates database error */
	}
	
}

//Functionality to add:
//More getters?
//Ability to clear the table?
