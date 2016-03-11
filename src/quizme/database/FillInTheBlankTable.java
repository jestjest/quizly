package quizme.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import quizme.DBConnection;

public class FillInTheBlankTable {
private DBConnection db;
	
	public FillInTheBlankTable(DBConnection db) {
		this.db = db;
		createFillInTheBlankTable();
	}
	
	
	private void createFillInTheBlankTable() {
		try {	
			PreparedStatement pstmt = db.getPreparedStatement("CREATE TABLE IF NOT EXISTS fillintheblank (quizid INT, questionOrder INT, preQuestion TEXT, postQuestion TEXT, correctAnswers TEXT, preferredAnswer INT)");
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
	
	public void addQuestion(int quizid, int questionOrder, String preQuestion, String postQuestion, List<String> correctAnswers, int preferredAnswer) {
		try {
			PreparedStatement pstmt = db.getPreparedStatement("INSERT INTO fillintheblank VALUES (?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, quizid);
			pstmt.setInt(2, questionOrder);
			pstmt.setString(3, preQuestion);
			pstmt.setString(4, postQuestion);
			pstmt.setString(5, answersToString(correctAnswers));
			pstmt.setInt(6, preferredAnswer);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void removeQuestion(int quizid, int questionOrder) {
		try {
			PreparedStatement pstmt = db.getPreparedStatement("DELETE FROM fillintheblank WHERE quizid = ? AND questionOrder = ?");
			pstmt.setInt(1, quizid);
			pstmt.setInt(2, questionOrder);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void removeQuizQuestions(int quizid) {
		try {
			PreparedStatement pstmt = db.getPreparedStatement("DELETE FROM fillintheblank WHERE quizid = ?");
			pstmt.setInt(1, quizid);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public ResultSet getAllQuizEntries(int quizid) {
		try {
			PreparedStatement pstmt1 = db.getPreparedStatement("SELECT * FROM fillintheblank WHERE quizid = ?");
			pstmt1.setInt(1, quizid);
			return pstmt1.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null; /* indicates database error */
	}
}