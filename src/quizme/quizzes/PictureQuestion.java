package quizme.quizzes;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import javax.servlet.jsp.JspWriter;

public class PictureQuestion extends Question {
	
	/**
	 * A String that stores a URL pointing to a picture.
	 */
	private String pictureURL;
	
	/**
	 * A list that stores all correct text answers.
	 */
	private List<String> correctAnswers;
	
	/**
	 * A String that stores the correct response text.
	 */
	private String correctResponseText;

	/**
	 * A string showing the response text.
	 */
	private String responseText;
	
	/**
	 * List of column names in the corresponding data base.
	 */
	private static final String[] columnNames = {
			"quizid",
			"questionOrder",
			"correctAnswers",
			"preferredAnswer",
			"pictureURL",
	};
	
	/**
	 * List of column types in the corresponding data base.
	 */
	private static final String[] columnTypes = {
			"INT",
			"INT",
			"TEXT",
			"INT",
			"TEXT"
	};
	
	/**
	 * The type of this question.
	 */
	private static final TYPE type = TYPE.PICTURE;
	
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
	public PictureQuestion( ResultSet rs ) throws SQLException {
		super( rs );
		quizID = rs.getInt( columnNames[0] );
		order = rs.getInt( columnNames[1] );
		String answers = rs.getString(columnNames[2]);
		correctAnswers = Arrays.asList(answers.split("\\s*~~~\\s*"));
		int preferredAnswer = rs.getInt( columnNames[3] );
		correctResponseText = correctAnswers.get(preferredAnswer);
		pictureURL = rs.getString( columnNames[4] );
		responseText = "";
	}
	
	/**
	 * Constructor for debugging.
	 * @param QID
	 * @param ord
	 * @param QT
	 * @param PURL
	 * @param CRT
	 */
	public PictureQuestion( int QID, int ORD, String PURL, List<String> answers, String CRT) {
		quizID = QID;
		order = ORD;
		pictureURL = PURL;
		correctAnswers = answers;
		correctResponseText = CRT;
		responseText = "";
	}

	@Override
	public void show( JspWriter out, int questionIndex) throws IOException {
		out.append("<img src='"+ pictureURL+"' alt='Error. Image not found.'><br>");
		out.append("<br>");
		out.append("Please enter your response here:<br>");
		out.append("<input type='text' name='response_" + questionIndex + "_0'>");
	}

	@Override
	public void answerSummary ( JspWriter out, int questionIndex ) throws IOException {
		if (points == maxPoints) {
			out.append("<p>Good job! You got it right!</p>");
		}
		
		out.append("<b>Question" + questionIndex + ": </b>");
		out.append("<img src='"+ pictureURL + "' alt='Sorry! Image not found.'><br>");
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
		if ( correctAnswers.contains( responseText ) ) {
			points = 1;
		}
		else {
			points = 0;
		}
		
	}
}
