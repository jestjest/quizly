package quizme.quizzes;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

import javax.servlet.jsp.JspWriter;

public class MultipleChoice extends Question {

	/**
	 * A string showing the question text.
	 */
	private String questionText;
	
	/**
	 * A string consists of all choices separated by '~~~'.
	 */
	private String choicesText;

	/**
	 * An array of strings consisting different choices.
	 */
	private String[] choices;
	
	/**
	 * An integer show the correct choice.
	 */
	private int correctChoice;

	/**
	 * An integer show the user response.
	 * 0 <= response < choices.size()
	 * The user, however, put a number in [1, choices.size] range.
	 */
	private int response;
	
	/**
	 * List of column names in the corresponding data base.
	 */
	private static final String[] columnNames = {
			"quizid",
			"questionOrder",
			"question",
			"answerChoices",
			"correctAnswer"
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
	private static final TYPE type = TYPE.MC;
	
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
	public MultipleChoice( ResultSet rs ) throws SQLException {
		super( rs );
		quizID = rs.getInt( columnNames[0] );
		order = rs.getInt( columnNames[1] );
		questionText = rs.getString( columnNames[2] );
		choicesText = rs.getString( columnNames[3] );
		correctChoice = rs.getInt( columnNames[4] );
		parseText();
		response = 0;
	}
	
	public MultipleChoice( int QID, int ORD, String QT, String CT, int CC) {
		quizID = QID;
		order = ORD;
		questionText = QT;
		choicesText = CT;
		correctChoice = CC;
		parseText();
		response = 0;
	}

	@Override
	public void show( JspWriter out, int questionIndex ) throws IOException {
		
		out.append("<b>");
		out.append(questionText);
		out.append("</b>");
		out.append("<br>");
		for ( int i = 0; i < choices.length; i++ ) {
			String buttonName = "response_" + questionIndex + "_" + i;
			out.append( makeRadioButton( buttonName, choices[i], false) );
			out.append("<br>");
		}
	}
	
	@Override
	public void answer( StringBuilder out ) {
		out.append("<b>Question: </b>");
		out.append(questionText);
		out.append("<br>");
		for ( int i = 0; i < choices.length; i++ ) {
			String buttonName = "response_" + order + "_" + i;
			out.append( makeRadioButton( buttonName, choices[i], i == correctChoice ) );
			if ( (i == response) && ( i == correctChoice ) ) {
				out.append(" (answered correctly) ");
			}
			else if (i == response) {
				out.append(" (your choice) ");
			}
			else if (i == correctChoice) {
				out.append(" (correct choice) ");
			}
			out.append("<br>");
		}

		out.append("<b>Your points: </b>");
		out.append( Integer.toString( points) );
		out.append("<br>");
	}
	
	/**
	 * Use choicesText and create the array of choice texts.
	 */
	private void parseText() {
		choices = choicesText.split("\\s*~~~\\s*");
	}
	
	private String makeRadioButton( String name, String value, Boolean check) {
		String checkString = check ? "checked" : "";
		String s = "<input type='radio' name='" + name +
				"' value='" + value+"' "+ checkString + "> " + value;
		return s;
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
		this.response = Integer.parseInt(response);
		if ( this.response == correctChoice ) {
			points = 1;
		}
		else {
			points = 0;
		}
		
	}
}
