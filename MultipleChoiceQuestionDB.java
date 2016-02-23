package quiz;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MultipleChoiceQuestionDB {
	private Statement stmt;
	private final static int numOfOptions = 5;
	
	public MultipleChoiceQuestionDB(DBConnection db) {
		stmt = db.getStatement();
		createMultipleChoiceQuestionTable();
	}
	
	
	private void createMultipleChoiceQuestionTable() {
		try {
			stmt.executeUpdate("CREATE TABLE IF NOT EXISTS multiplechoice (questionid CHAR(64), quizid INT, questionOrder INT, question TEXT, optionA TEXT, optionB TEXT, optionC TEXT, optionD TEXT, optionE TEXT, correctAnswer CHAR(64))");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public String addQuestion(int quizid, int questionOrder, String question, String[] options, String correctAnswer) {
		try {
			String questionid = "MC" + String.format("%05d", quizid) + String.format("%05d",questionOrder);
		
			StringBuilder sb = new StringBuilder("INSERT INTO multiplechoice VALUES (\"" + questionid + "\", " + quizid + ", " + questionOrder + ", \"" + question + "\", ");
			for (int i = 0; i < numOfOptions; i++) {
				if (!options[i].isEmpty()) {
					sb.append("\"" + options[i] + "\", ");
				} else {
					sb.append(" , ");
				}
			}
			sb.append("\"" + correctAnswer + "\")");
			stmt.executeUpdate(sb.toString());
			
			return questionid;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null; /* indicates database error */
	}
	
	public void removeQuestion(String questionid) {
		try {
			stmt.executeUpdate("DELETE FROM multiplechoice WHERE questionid = \"" + questionid + "\"");
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
	
	public String getOption(String questionid, char letter) {
		char uppercaseLetter = Character.toUpperCase(letter);
		if (uppercaseLetter < 'A' || uppercaseLetter > 'E') return null; 
		return getString(questionid, "option" + uppercaseLetter);
	}
	
	public String getCorrectAnswer(String questionid) {
		return getString(questionid, "correctAnswer");
	}
	
	/* helper functions */
	
	private String getString(String questionid, String field) {
		try {
			ResultSet rs = stmt.executeQuery("SELECT " +  field + " FROM multiplechoice where questionid = \"" + questionid + "\"");
			rs.first();
			return rs.getString(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null; /* indicates database error */
	}
	
	private int getInt(String questionid, String field) {
		try {
			ResultSet rs = stmt.executeQuery("SELECT " + field + " FROM multiplechoice WHERE questionid = \"" + questionid + "\"");
			rs.first();
			return rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1; /* indicates database error */
	}
}
