package quizme.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import quizme.DBConnection;

public class TrueFalseQuestionTable {
	private DBConnection db;
	
	public TrueFalseQuestionTable(DBConnection db) {
		this.db = db;
		createTrueFalseQuestionTable();
	}
	
	private void createTrueFalseQuestionTable() {
		try {
			PreparedStatement pstmt = db.getPreparedStatement("CREATE TABLE IF NOT EXISTS "
					+ "truefalse (quizid INT, questionOrder INT, "
					+ "question TEXT, answerChoices TEXT, correctAnswers TEXT)");
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
	
	private String intsToString(List<Integer> answers) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < answers.size(); i++) {
			sb.append(answers.get(i).toString() + "~~~ ");
		}
		return sb.toString();
	}
	
	public void addQuestion(int quizid, int questionOrder, String question, 
			List<String> answerChoices, List<Integer> correctAnswers) {
		try {
			PreparedStatement pstmt = db.getPreparedStatement("INSERT INTO truefalse "
					+ "VALUES (?, ?, ?, ?, ?)");
			pstmt.setInt(1, quizid);
			pstmt.setInt(2, questionOrder);
			pstmt.setString(3, question);
			pstmt.setString(4, answersToString(answerChoices));
			pstmt.setString(5, intsToString(correctAnswers));
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void removeQuestion(int quizid, int questionOrder) {
		try {
			PreparedStatement pstmt = db.getPreparedStatement("DELETE FROM truefalse WHERE "
					+ "quizid = ? AND questionOrder = ?");
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
	
	public void setAnswerChoices(int quizid, int questionOrder, List<String> answerChoices) {
		setString(quizid, questionOrder, "answerChoices", answersToString(answerChoices));
	}
	
	public List<String> getAnswerChoices(int quizid, int questionOrder) {
		String answers = getString(quizid, questionOrder, "answerChoices");
		return Arrays.asList(answers.split("\\s*~~~\\s*"));
	}
	
	public void setCorrectAnswers(int quizid, int questionOrder, List<Integer> correctAnswers) {
		setString(quizid, questionOrder, "correctAnswers", intsToString(correctAnswers));
	}
	
	public List<Integer> getCorrectAnswers(int quizid, int questionOrder) {
		String[] answers = getString(quizid, questionOrder, "correctAnswers").split("\\s*~~~\\s*");
		List<Integer> correctAnswers = new ArrayList<Integer>();
		for ( int i = 0; i < answers.length; i++ ) {
			correctAnswers.add( Integer.parseInt(answers[i]) );
		}
		return correctAnswers;
	}
	
	public ResultSet getAllQuizEntries(int quizid) {
		try {
			PreparedStatement pstmt1 = db.getPreparedStatement("SELECT * FROM truefalse WHERE quizid = ?");
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
			PreparedStatement pstmt = db.getPreparedStatement("UPDATE truefalse SET " + field + " = ? "
					+ "WHERE quizid = ? AND questionOrder = ?");
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
			PreparedStatement pstmt = db.getPreparedStatement("SELECT " +  field + " FROM truefalse "
					+ "WHERE quizid = ? AND questionOrder = ?");
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
			PreparedStatement pstmt = db.getPreparedStatement("UPDATE truefalse SET " + field + " = ? "
					+ "WHERE quizid = ? AND questionOrder = ?");
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
			PreparedStatement pstmt = db.getPreparedStatement("SELECT " +  field + " FROM truefalse "
					+ "WHERE quizid = ? AND questionOrder = ?");
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