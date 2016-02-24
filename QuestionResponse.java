package quizme;

import java.sql.ResultSet;
import java.sql.SQLException;

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
			"responseText"
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
		correctResponseText = rs.getString( columnNames[3] );
		responseText = "";
	}

	@Override
	public void show( StringBuilder out ) {
		out.append("<b>");
		out.append(questionText);
		out.append("</b>");
		out.append("<br>");
		out.append("<input type=\"text\" name=\"responseText_" + Integer.toString(order) 
		+ "\" value=\""	+ responseText + "\">");
	}

	@Override
	public void create( StringBuilder out ) {
		out.append("<input type=\"text\" name=\"questionText_" + Integer.toString(order) 
		+ "\" value=\""	+ questionText + "\">");
		out.append("<br>");
		out.append("<input type=\"text\" name=\"correctResponseText_" + Integer.toString(order) 
		+ "\" value=\""	+ correctResponseText + "\">");
	}	

	@Override
	public void answer( StringBuilder out ) {
		out.append("<b>Question: </b>");
		out.append(questionText);
		out.append("<br>");
		out.append("<b>Your answer: </b>");
		out.append(responseText);
		out.append("<br>");
		out.append("<b>Correct answer: </b>");
		out.append(correctResponseText);
		out.append("<br>");
		if ( correctResponseText.equals( responseText ) ) {
			points = 1;
		}
		out.append("<b>Your points:</b>");
		out.append( Integer.toString( points) );
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
}
