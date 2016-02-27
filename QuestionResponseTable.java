package quizme.database;

import java.sql.*;
import java.util.Arrays;
import java.util.List;

import quizme.DBConnection;

public class QuestionResponseTable {
	private DBConnection db;
	
	public QuestionResponseTable(DBConnection db) {
		this.db = db;
		createQuestionResponseTable();
	}
	
	
	private void createQuestionResponseTable() {
		try {
			PreparedStatement pstmt = db.getPreparedStatement("CREATE TABLE IF NOT EXISTS questionresponse (quizid INT, questionOrder INT, question TEXT, correctAnswers TEXT, preferredAnswer INT)");
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private String answersToString(List<String> answers) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < answers.size(); i++) {
			sb.append(answers.get(i) + "~~~ ");
		}
		return sb.toString();
	}
	
	public void addQuestion(int quizid, int questionOrder, String question, List<String> correctAnswer, int preferredAnswer) {
		try {
			PreparedStatement pstmt = db.getPreparedStatement("INSERT INTO questionresponse VALUES (?, ?, ?, ?, ?)");
			pstmt.setInt(1, quizid);
			pstmt.setInt(2, questionOrder);
			pstmt.setString(3, question);
			pstmt.setString(4, answersToString(correctAnswer));
			pstmt.setInt(5, preferredAnswer);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void removeQuestion(int quizid, int questionOrder) {
		try {
			PreparedStatement pstmt = db.getPreparedStatement("DELETE FROM questionresponse WHERE quizid = ? AND questionOrder = ?");
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
	
	public void setQuestionOrder(int quizid, int questionOrder, int newQuestionOrder) {
		setInt(quizid, questionOrder, "questionOrder", newQuestionOrder);
	}
	
	public int getQuestionOrder(int quizid, int questionOrder) {
		return getInt(quizid, questionOrder, "questionOrder");
	}
	
	public void setQuestion(int quizid, int questionOrder, String question) {
		setString(quizid, questionOrder, "question", question);
	}
	
	public String getQuestion(int quizid, int questionOrder) {
		return getString(quizid, questionOrder, "question");
	}
	
	public void setCorrectAnswers(int quizid, int questionOrder, List<String> correctAnswer) {
		setString(quizid, questionOrder, "correctAnswers", answersToString(correctAnswer));
	}
	
	public List<String> getCorrectAnswers(int quizid, int questionOrder) {
		String answers = getString(quizid, questionOrder, "correctAnswers");
		return Arrays.asList(answers.split("\\s*~~~\\s*"));
	}
	
	public void setPreferredAnswer(int quizid, int questionOrder, int preferredAnswer) {
		setInt(quizid, questionOrder, "preferredAnswer", preferredAnswer);
	}
	
	public int getPreferredAnswer(int quizid, int questionOrder) {
		return getInt(quizid, questionOrder, "preferredAnswer");
	}
	
	public ResultSet getAllQuizEntries(int quizid) {
		try {
			PreparedStatement pstmt1 = db.getPreparedStatement("SELECT * FROM questionresponse WHERE quizid = ?");
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
			PreparedStatement pstmt = db.getPreparedStatement("UPDATE questionresponse SET " + field + " = ? WHERE quizid = ? AND questionOrder = ?");
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
			PreparedStatement pstmt = db.getPreparedStatement("SELECT " +  field + " FROM questionresponse WHERE quizid = ? AND questionOrder = ?");
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
			PreparedStatement pstmt = db.getPreparedStatement("UPDATE questionresponse SET " + field + " = ? WHERE quizid = ? AND questionOrder = ?");
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
			PreparedStatement pstmt = db.getPreparedStatement("SELECT " +  field + " FROM questionresponse WHERE quizid = ? AND questionOrder = ?");
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