package quizme.quizzes;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.servlet.jsp.JspWriter;

public class QuestionResponse extends Question {

	/**
	 * A string showing the question text.
	 */
	private String questionText;

	/**
	 * A string showing the response text.
	 */
	private String responseText;

	/**
	 * A list that stores all correct text answers.
	 */
	private List<String> correctAnswers;
	
	/**
	 * Lower-case correct answers
	 */
	private List<String> correctAnswersLowerCase;
	
	/**
	 * A string that stores the preferred correct response text.
	 */
	private String correctResponseText;
	
	/**
	 * List of column names in the corresponding data base.
	 */
	private static final String[] columnNames = {
			"quizid",
			"questionOrder",
			"question",
			"correctAnswers",
			"preferredAnswer"
	};
	
	/**
	 * List of column types in the corresponding data base.
	 */
	private static final String[] columnTypes = {
			"INT",
			"INT",
			"TEXT",
			"TEXT",
			"INT"
	};
	
	/**
	 * The type of this question.
	 */
	private static final TYPE type = TYPE.QR;
	
	/**
	 * The maximum achievable points of this question.
	 */
	private static final int maxPoints = 1;
	
	/**
	 * Constructor: create an instance of a question using one row
	 * of the corresponding data base.
	 * @param rs a ResultSet object pointing to a row in the table
	 * @throws SQLException
	 */
	public QuestionResponse( ResultSet rs ) throws SQLException {
		super( rs );
		quizID = rs.getInt( columnNames[0] );
		order = rs.getInt( columnNames[1] );
		questionText = rs.getString( columnNames[2] );
		String answers = rs.getString(columnNames[3]);
		correctAnswers = Arrays.asList(answers.split("\\s*~~~\\s*"));
		correctAnswersLowerCase = new ArrayList<String>();
		for ( String str: correctAnswers ) {
			correctAnswersLowerCase.add(str.toLowerCase());
		}
		int preferredResponse = rs.getInt( columnNames[4] );
		correctResponseText = correctAnswers.get(preferredResponse);
		responseText = "";
	}
	
	/**
	 * Constructor for debugging.
	 * @param QID
	 * @param ord
	 * @param QT
	 * @param CRT
	 */
	public QuestionResponse( int QID, int ORD, String QT, List<String> answers, String CRT) {
		quizID = QID;
		order = ORD;
		questionText = QT;
		correctAnswers = answers;
		correctResponseText = CRT;
		responseText = "";
	}

	@Override
	public void show( JspWriter out, int questionIndex ) throws IOException {
		out.append("<b>");
		out.append(questionText);
		out.append("</b>");
		out.append("<br>");
		out.append("Please enter your response here:<br>");
		out.append("<input type='text' name='response_" + questionIndex + "_0'>");
	}

	@Override
	public void answerSummary( JspWriter out, int questionIndex) throws IOException  {
		if (points == maxPoints) {
			out.append("<p>Good job! You got it right!</p>");
		} else {
			out.append("<p>Looks like you didn't get question " + questionIndex + " completely right.</p>");
		}
		
		out.append("<b>Question " + questionIndex + ": </b>");
		out.append(questionText);
		out.append("<br>");
		out.append("<b>Your answer: </b>");
		out.append(responseText);
		out.append("<br>");
		out.append("<b>Preferred answer: </b>");
		out.append(correctResponseText);
		out.append("<br>");
		out.append("<b>Points: </b>");
		out.append( Integer.toString(points) + " out of " + maxPoints);
		out.append("<br>");
	}
	
	@Override
	public String[] columnNames() {
		return columnNames;
	}
	
	@Override
	public String[] columnTypes() {
		return columnTypes;
	}

	@Override
	public TYPE type() {
		return type;
	}

	@Override
	public int maxPoints() {
		return maxPoints;
	}

	@Override
	public void setResponse(String response) {
		responseText = response;
		if ( correctAnswersLowerCase.contains(responseText.toLowerCase()) ) {
			points = 1;
		}
		else {
			points = 0;
		}
		
	}
}
