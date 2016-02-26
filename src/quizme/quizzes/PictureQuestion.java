package quizme.quizzes;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PictureQuestion extends Question {

	/**
	 * A string showing the question text.
	 */
	private String questionText;
	
	/**
	 * A String that stores a URL pointing to a picture.
	 */
	private String pictureURL;
	
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
			"quizID",
			"order",
			"questionText",
			"pictureURL",
			"correctResponseText"
	};
	
	/**
	 * List of column types in the corresponding data base.
	 */
	private static final String[] columnTypes = {
			"INT",
			"INT",
			"TEXT",
			"TEXT",
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
		questionText = rs.getString( columnNames[2] );
		pictureURL = rs.getString( columnNames[3] );
		correctResponseText = rs.getString( columnNames[4] );
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
	public PictureQuestion( int QID, int ORD, String QT, String PURL, String CRT) {
		quizID = QID;
		order = ORD;
		questionText = QT;
		pictureURL = PURL;
		correctResponseText = CRT;
		responseText = "";
	}

	@Override
	public void show( StringBuilder out ) {
		out.append("<b>");
		out.append(questionText);
		out.append("</b>");
		out.append("<br>");
		out.append("<img src=\""+ pictureURL+"\" alt=\"Sorry! Image not found.\"><br>");
		out.append("Please eneter your response here:<br>");
		out.append("<input type=\"text\" name=\"responseText_" + Integer.toString(order) 
		+ "\" value=\""	+ responseText + "\"><br>");
	}

	@Override
	public void answer( StringBuilder out ) {
		out.append("<b>Question: </b>");
		out.append(questionText);
		out.append("<br>");
		out.append("<img src=\""+ pictureURL+"\" alt=\"Sorry! Image not found.\"><br>");
		out.append("<b>Your answer: </b>");
		out.append(responseText);
		out.append("<br>");
		out.append("<b>Correct answer: </b>");
		out.append(correctResponseText);
		out.append("<br>");
		if ( correctResponseText.equals( responseText ) ) {
			points = 1;
		}
		out.append("<b>Your points: </b>");
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
