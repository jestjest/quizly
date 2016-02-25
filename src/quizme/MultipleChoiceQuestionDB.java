package quizme;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MultipleChoiceQuestionDB {
	private DBConnection db;
	private final static int numOfOptions = 5;
	
	public MultipleChoiceQuestionDB(DBConnection db) {
		this.db = db;
		createMultipleChoiceQuestionTable();
	}
	
	
	private void createMultipleChoiceQuestionTable() {
		try {
			PreparedStatement pstmt = db.getPreparedStatement("CREATE TABLE IF NOT EXISTS multiplechoice (quizid INT, questionOrder INT, question TEXT, optionA TEXT, optionB TEXT, optionC TEXT, optionD TEXT, optionE TEXT, correctAnswer CHAR(64))");
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void addQuestion(int quizid, int questionOrder, String question, String[] options, String correctAnswer) {
		try {
			PreparedStatement pstmt = db.getPreparedStatement("INSERT INTO multiplechoice VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, quizid);
			pstmt.setInt(2, questionOrder);
			pstmt.setString(3, question);
			int optionIndexOffset = 4;
			for (int i = 0; i < numOfOptions; i++) 
				pstmt.setString(i + optionIndexOffset, options[i]);
			pstmt.setString(9, correctAnswer);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void removeQuestion(int quizid, int questionOrder) {
		try {
			PreparedStatement pstmt = db.getPreparedStatement("DELETE FROM multiplechoice WHERE quizid = ? AND questionOrder = ?");
			pstmt.setInt(1, quizid);
			pstmt.setInt(2, questionOrder);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public int getQuizID(int quizid, int questionOrder) {
		return getInt(quizid, questionOrder, "quizid");
	}
	
	public int getQuestionOrder(int quizid, int questionOrder) {
		return getInt(quizid, questionOrder, "questionOrder");
	}
	
	public void setQuestionOrder(int quizid, int questionOrder, int newQuestionOrder) {
		setInt(quizid, questionOrder, "questionOrder", newQuestionOrder);
	}
	
	public void setQuestion(int quizid, int questionOrder, String question) {
		setString(quizid, questionOrder, "question", question);
	}
	
	public String getQuestion(int quizid, int questionOrder) {
		return getString(quizid, questionOrder, "question");
	}
	
	public void setOption(int quizid, int questionOrder, char letter, String option) {
		char uppercaseLetter = Character.toUpperCase(letter);
		if (uppercaseLetter < 'A' || uppercaseLetter > 'E') return; 
		setString(quizid, questionOrder, "option" + uppercaseLetter, option);
	}
	
	public String getOption(int quizid, int questionOrder, char letter) {
		char uppercaseLetter = Character.toUpperCase(letter);
		if (uppercaseLetter < 'A' || uppercaseLetter > 'E') return null; 
		return getString(quizid, questionOrder, "option" + uppercaseLetter);
	}
	
	public void setCorrectAnswer(int quizid, int questionOrder, String correctAnswer) {
		setString(quizid, questionOrder, "correctAnswer", correctAnswer);
	}
	
	public String getCorrectAnswer(int quizid, int questionOrder) {
		return getString(quizid, questionOrder, "correctAnswer");
	}
	
	public ResultSet getAllQuizEntries(int quizid) {
		try {
			PreparedStatement pstmt1 = db.getPreparedStatement("SELECT * FROM multiplechoice WHERE quizid = ?");
			pstmt1.setInt(1, quizid);
			return pstmt1.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null; /* indicates database error */
	}
	
	/* helper functions */
	
	private void setString(int quizid, int questionOrder, String field, String value) {
		try {
			PreparedStatement pstmt = db.getPreparedStatement("UPDATE multiplechoice SET " + field + " = ? WHERE quizid = ? AND questionOrder = ?");
			pstmt.setString(1, value);
			pstmt.setInt(2, quizid);
			pstmt.setInt(3, questionOrder);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private String getString(int quizid, int questionOrder, String field) {
		try {
			PreparedStatement pstmt = db.getPreparedStatement("SELECT " +  field + " FROM multiplechoice WHERE quizid = ? AND questionOrder = ?");
			pstmt.setInt(1, quizid);
			pstmt.setInt(2, questionOrder);
			ResultSet rs = pstmt.executeQuery();
			rs.first();
			return rs.getString(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null; /* indicates database error */
	}
	
	private void setInt(int quizid, int questionOrder, String field, int value) {
		try {
			PreparedStatement pstmt = db.getPreparedStatement("UPDATE multiplechoice SET " + field + " = ? WHERE quizid = ? AND questionOrder = ?");
			pstmt.setInt(1, value);
			pstmt.setInt(2, quizid);
			pstmt.setInt(3, questionOrder);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private int getInt(int quizid, int questionOrder, String field) {
		try {
			PreparedStatement pstmt = db.getPreparedStatement("SELECT " +  field + " FROM multiplechoice WHERE quizid = ? AND questionOrder = ?");
			pstmt.setInt(1, quizid);
			pstmt.setInt(2, questionOrder);
			ResultSet rs = pstmt.executeQuery();
			rs.first();
			return rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1; /* indicates database error */
	}
}
