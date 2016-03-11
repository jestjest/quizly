package quizme.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import quizme.DBConnection;

public class PictureResponseQuestionTable {
private DBConnection db;
	
	public PictureResponseQuestionTable(DBConnection db) {
		this.db = db;
		createPictureResponseQuestionTable();
	}
	
	private void createPictureResponseQuestionTable() {
		try {
			PreparedStatement pstmt = db.getPreparedStatement("CREATE TABLE IF NOT EXISTS pictureresponse (quizid INT, questionOrder INT, correctAnswers TEXT, preferredAnswer INT, pictureURL TEXT)");
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
	
	public void addQuestion(int quizid, int questionOrder, List<String> correctAnswers, int preferredAnswer, String pictureURL) {
		try {
			PreparedStatement pstmt = db.getPreparedStatement("INSERT INTO pictureresponse VALUES (?, ?, ?, ?, ?)");
			pstmt.setInt(1, quizid);
			pstmt.setInt(2, questionOrder);
			pstmt.setString(3, answersToString(correctAnswers));
			pstmt.setInt(4, preferredAnswer);
			pstmt.setString(5, pictureURL);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void removeQuestion(int quizid, int questionOrder) {
		try {
			PreparedStatement pstmt = db.getPreparedStatement("DELETE FROM pictureresponse WHERE quizid = ? AND questionOrder = ?");
			pstmt.setInt(1, quizid);
			pstmt.setInt(2, questionOrder);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void removeQuizQuestions(int quizid) {
		try {
			PreparedStatement pstmt = db.getPreparedStatement("DELETE FROM pictureresponse WHERE quizid = ?");
			pstmt.setInt(1, quizid);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public ResultSet getAllQuizEntries(int quizid) {
		try {
			PreparedStatement pstmt1 = db.getPreparedStatement("SELECT * FROM pictureresponse WHERE quizid = ?");
			pstmt1.setInt(1, quizid);
			return pstmt1.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null; /* indicates database error */
	}
}
