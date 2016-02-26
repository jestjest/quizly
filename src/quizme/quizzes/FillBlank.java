package quizme.quizzes;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FillBlank extends Question {

	/**
	 * A string showing the question text before blank.
	 */
	private String leftText;

	/**
	 * A string showing the question text after blank.
	 */
	private String rightText;

	/**
	 * Unprocessed string question.
	 * It should include the "___" determining a blank space.
	 */
	private String questionText;

	/**
	 * A string showing the response text.
	 */
	private String responseText;

	/**
	 * A String that stores the correct response text.
	 */
	private String correctResponseText;

	/**
	 * List of column names in the corresponding data base.
	 */
	private static final String[] columnNames = {
			"quizID",
			"order",
			"questionText",
			"correctResponseText"
	};

	/**
	 * List of column types in the corresponding data base.
	 */
	private static final String[] columnTypes = {
			"INT",
			"INT",
			"TEXT",
			"TEXT"
	};

	/**
	 * The type of this question.
	 */
	private static final TYPE type = TYPE.BLANK;

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
	public FillBlank( ResultSet rs ) throws SQLException {
		super( rs );
		quizID = rs.getInt( columnNames[0] );
		order = rs.getInt( columnNames[1] );
		questionText = rs.getString( columnNames[2] );
		correctResponseText = rs.getString( columnNames[3] );
		responseText = "";
		parseText();
	}

	/**
	 * Constructor for debugging.
	 * @param QID
	 * @param ord
	 * @param QT
	 * @param CRT
	 */
	public FillBlank( int QID, int ord, String QT, String CRT) {
		quizID = QID;
		order = ord;
		questionText = QT;
		correctResponseText = CRT;
		responseText = "";
		parseText();
	}
	
	@Override
	public void show( StringBuilder out ) {
		out.append("<b>");
		out.append(leftText);
		out.append("</b>");
		out.append("<input type=\"text\" name=\"responseText_" + Integer.toString(order) 
		+ "\" value=\""	+ responseText + "\">");
		out.append("<b>");
		out.append(rightText);
		out.append("</b><br>");
	}

	@Override
	public void answer( StringBuilder out ) {
		out.append("<b>Question filled with correct answer: </b><br>");
		out.append(leftText+" <b>"+correctResponseText+"</b> " + rightText);
		out.append("<br>");
		out.append("<b>Your answer: </b>");
		out.append(responseText);
		out.append("<br>");
		if ( correctResponseText.equals( responseText ) ) {
			points = 1;
		}
		out.append("<b>Your points: </b>");
		out.append( Integer.toString( points) );
		out.append("<br>");
	}

	/**
	 * This function use [questionText] to find the 
	 * [leftText] and [rightText].
	 */
	private void parseText() {
		String[] output = questionText.split("___");
		switch ( output.length ) {
		case 0:
			leftText = "";
			rightText = "";
			break;
		case 1:
			leftText = output[0];
			rightText = "";
			break;
		default:
			leftText = output[0];
			rightText = output[1];				
		}
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
}
