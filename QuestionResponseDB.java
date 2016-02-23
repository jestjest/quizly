package quiz;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class QuestionResponseDB {
	private Statement stmt;
	
	public QuestionResponseDB(DBConnection db) {
		stmt = db.getStatement();
		createMultipleChoiceQuestionTable();
	}
	
	
	private void createMultipleChoiceQuestionTable() {
		try {
			stmt.executeUpdate("CREATE TABLE IF NOT EXISTS questionresponse (questionid CHAR(64), quizid INT, questionOrder INT, question TEXT, correctAnswer TEXT)");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public String addQuestion(int quizid, int questionOrder, String question, String correctAnswer) {
		try {
			String questionid = "QR" + String.format("%05d", quizid) + String.format("%05d",questionOrder);
			
			System.out.println("INSERT INTO questionresponse VALUES (\"" + questionid + "\", " + quizid + ", " + questionOrder + ", \"" + question + "\", \"" + correctAnswer + "\")");
			stmt.executeUpdate("INSERT INTO questionresponse VALUES (\"" + questionid + "\", " + quizid + ", " + questionOrder + ", \"" + question + "\", \"" + correctAnswer + "\")");
			
			return questionid;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null; /* indicates database error */
	}
	
	public void removeQuestion(String questionid) {
		try {
			stmt.executeUpdate("DELETE FROM questionresponse WHERE questionid = \"" + questionid + "\"");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public int getQuizID(String questionid) {
		return getInt(questionid, "quizid");
	}
	
	public int getQuestionOrder(String questionid) {
		return getInt(questionid, "questionOrder");
	}
	
	public String getQuestion(String questionid) {
		return getString(questionid, "question");
	}
	
	public String getCorrectAnswer(String questionid) {
		return getString(questionid, "correctAnswer");
	}
	
	/* helper functions */
	
	private String getString(String questionid, String field) {
		try {
			ResultSet rs = stmt.executeQuery("SELECT " +  field + " FROM questionresponse where questionid = \"" + questionid + "\"");
			rs.first();
			return rs.getString(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null; /* indicates database error */
	}
	
	private int getInt(String questionid, String field) {
		try {
			ResultSet rs = stmt.executeQuery("SELECT " + field + " FROM questionresponse WHERE questionid = \"" + questionid + "\"");
			rs.first();
			return rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1; /* indicates database error */
	}
}
