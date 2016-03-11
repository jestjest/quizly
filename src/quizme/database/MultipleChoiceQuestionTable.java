package quizme.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import quizme.DBConnection;

public class MultipleChoiceQuestionTable {
	private DBConnection db;
	
	public MultipleChoiceQuestionTable(DBConnection db) {
		this.db = db;
		createMultipleChoiceQuestionTable();
	}
	
	private void createMultipleChoiceQuestionTable() {
		try {
			PreparedStatement pstmt = db.getPreparedStatement("CREATE TABLE IF NOT EXISTS "
					+ "multiplechoice (quizid INT, questionOrder INT, "
					+ "question TEXT, answerChoices TEXT, correctAnswer INT)");
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
	
	public void addQuestion(int quizid, int questionOrder, String question, List<String> answerChoices, int correctAnswer) {
		try {
			PreparedStatement pstmt = db.getPreparedStatement("INSERT INTO multiplechoice VALUES (?, ?, ?, ?, ?)");
			pstmt.setInt(1, quizid);
			pstmt.setInt(2, questionOrder);
			pstmt.setString(3, question);
			pstmt.setString(4, answersToString(answerChoices));
			pstmt.setInt(5, correctAnswer);
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
	
	public void removeQuizQuestions(int quizid) {
		try {
			PreparedStatement pstmt = db.getPreparedStatement("DELETE FROM multiplechoice WHERE quizid = ?");
			pstmt.setInt(1, quizid);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
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
}