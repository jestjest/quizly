package quizme.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import quizme.DBConnection;

public class MultipleAnswersQuestionTable {
	private DBConnection db;
	
	public MultipleAnswersQuestionTable(DBConnection db) {
		this.db = db;
		createMultipleAnswersQuestionTable();
	}
	
	
	private void createMultipleAnswersQuestionTable() {
		try {
			PreparedStatement pstmt = db.getPreparedStatement("CREATE TABLE IF NOT EXISTS multipleanswers "
					+ "(quizid INT, questionOrder INT, question TEXT, correctAnswers TEXT, "
					+ "preferredAnswers TEXT, ordered BOOL, numanswers INT)");
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private String answersToString(List<List<String>> answers) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < answers.size(); i++) {
			StringBuilder sbin = new StringBuilder();
			for (int j = 0; j < answers.get(i).size(); j++ ) {
				sbin.append(answers.get(i).get(j) + "~~~ ");
			}
			sb.append(sbin.toString()+"||| ");
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
	
	public void addQuestion(int quizid, int questionOrder, String question, List<List<String>> correctAnswers, 
			List<Integer> preferredAnswers, boolean ordered, int numAnswerSlots) {
		try {
			PreparedStatement pstmt = db.getPreparedStatement("INSERT INTO multipleanswers VALUES "
					+ "(?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, quizid);
			pstmt.setInt(2, questionOrder);
			pstmt.setString(3, question);
			pstmt.setString(4, answersToString(correctAnswers));
			pstmt.setString(5, intsToString(preferredAnswers) );
			pstmt.setBoolean(6, ordered);
			pstmt.setInt(7, numAnswerSlots);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void removeQuestion(int quizid, int questionOrder) {
		try {
			PreparedStatement pstmt = db.getPreparedStatement("DELETE FROM multipleanswers WHERE "
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
	
	public void setCorrectAnswers(int quizid, int questionOrder, List<List<String>> correctAnswers) {
		setString(quizid, questionOrder, "correctAnswers", answersToString(correctAnswers));
	}
	
	public List<String> getCorrectAnswers(int quizid, int questionOrder) {
		String answers = getString(quizid, questionOrder, "correctAnswers");
		String[] answersArray = answers.split("\\s*|||\\s*");
		List<List<String>> answersListList = new ArrayList<List<String>>();
		for ( int i = 0; i< answersArray.length; i++ ) {
			answersListList.add( Arrays.asList( answersArray[i].split("\\s*~~~\\s*") ) );
		}		
		return Arrays.asList();
	}
	
	public void setPreferredAnswers(int quizid, int questionOrder, List<Integer> preferredAnswers) {
		setString(quizid, questionOrder, "preferredAnswers", intsToString(preferredAnswers) );
	}
	
	public List<Integer> getPreferredAnswers(int quizid, int questionOrder) {
		String[] preferredAnswers = getString(quizid, questionOrder, "preferredAnswers").split("\\s*~~~\\s*");
		List<Integer> preferredAnswersList = new ArrayList<Integer>();
		for ( int i = 0; i < preferredAnswers.length; i++ ) {
			preferredAnswersList.add( Integer.parseInt(preferredAnswers[i]) );
		}
		return preferredAnswersList;		
	}
	
	public ResultSet getAllQuizEntries(int quizid) {
		try {
			PreparedStatement pstmt1 = db.getPreparedStatement("SELECT * FROM multipleanswers WHERE quizid = ?");
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
			PreparedStatement pstmt = db.getPreparedStatement("UPDATE multipleanswers SET " + field + " = ? "
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
			PreparedStatement pstmt = db.getPreparedStatement("SELECT " +  field + " FROM multipleanswers "
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
			PreparedStatement pstmt = db.getPreparedStatement("UPDATE multipleanswers SET " + 
		field + " = ? WHERE quizid = ? AND questionOrder = ?");
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
			PreparedStatement pstmt = db.getPreparedStatement("SELECT " +  field + " FROM multipleanswers "
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