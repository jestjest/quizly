package quizme.quizzes;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MultipleAnswers extends Question {

	/**
	 * A string showing the question text.
	 */
	private String questionText;
	
	/**
	 * List of responses.
	 */
	private List<String> responses;

	/**
	 * A list that stores all correct text answers.
	 */
	private List<List<String>> correctAnswers;
	
	/**
	 * String containing correctAnswers
	 */
	private String correctAnswersText;
	
	/**
	 * For each list of answer tell which one is preferred.
	 */
	private List<Integer> prefferedAnswers;
	
	/**
	 * A String containing all preferred answers indices.
	 */
	private String prefferedAnswersText;
	
	/**
	 * Show if the order matters or not
	 */
	private boolean ordered;
	
	/**
	 * For unordered questions, show how many answers is requested
	 */
	private int answerSlotsNum;
	
	/**
	 * List of column names in the corresponding data base.
	 */
	private static final String[] columnNames = {
			"quizid",
			"questionOrder",
			"question",
			"correctAnswers",
			"preferredAnswers",
			"ordered",
			"numAns"
	};
	
	/**
	 * List of column types in the corresponding data base.
	 */
	private static final String[] columnTypes = {
			"INT",
			"INT",
			"TEXT",
			"TEXT",
			"TEXT",
			"BOOL",
			"INT"
	};
	
	/**
	 * The type of this question.
	 */
	private static final TYPE type = TYPE.MA;
	
	/**
	 * The maximum achievable points of this question.
	 */
	private int maxPoints;
	
	private List<List<String>> textToCorrectAnswers( String txt ) {;
		List<String> answers = Arrays.asList( txt.split("\\s*|||\\s*") );
		List<List<String>> correctAnswers = new ArrayList<List<String>>();
		for ( String s: answers ) {
			correctAnswers.add(Arrays.asList( s.split("\\s*~~~\\s*") ) );
		}
		return correctAnswers;
	}
	
	/**
	 * Parse String to an array of ints
	 */
	private  List<Integer> textToInts( String text ) {
		String[] S = text.split("\\s*~~~\\s*");
		List<Integer> out = new ArrayList<Integer>();
		for ( int i = 0; i < S.length; i++ ) {
			out.add( Integer.parseInt( S[i] ) );
		}
		return out;
	}
	
	/**
	 * Constructor: create an instance of a question using one row
	 * of the corresponding data base.
	 * @param rs a ResultSet object pointing to a row in the table
	 * @throws SQLException
	 */
	public MultipleAnswers( ResultSet rs ) throws SQLException {
		super( rs );
		quizID = rs.getInt( columnNames[0] );
		order = rs.getInt( columnNames[1] );
		questionText = rs.getString( columnNames[2] );
		correctAnswersText = rs.getString(columnNames[3]);
		correctAnswers = textToCorrectAnswers( correctAnswersText );
		prefferedAnswersText = rs.getString( columnNames[4] );
		prefferedAnswers = textToInts( prefferedAnswersText );
		ordered = rs.getBoolean(columnNames[5]);
		answerSlotsNum = rs.getInt(columnNames[5]);
		maxPoints = answerSlotsNum;
	}


	@Override
	public void answer( StringBuilder out ) {
		out.append("<b>Question: </b>");
		out.append(questionText);
		out.append("<br>");
		if ( ordered ) {
			out.append("The correct list of answers:<br><ol>");
			for ( int i = 0; i < answerSlotsNum; i++ ) {
				out.append("<li>"+correctAnswers.get(i).get( prefferedAnswers.get(i) ) );
				out.append(" , your response = "+responses.get(i) + "</li>");
			}
			out.append("</ol><br>");
		}
		else {
			out.append("The correct list of answers:<br><ul>");
			for ( int i = 0; i < correctAnswers.size(); i++ ) {
				out.append("<li>"+ correctAnswers.get(i).get( prefferedAnswers.get(i) ) + "</li>" );
			}
			out.append("<br>Your responses:<br><ul>");
			for ( int i = 0; i < responses.size(); i++ ) {
				out.append("<li>"+responses.get(i)+"</li>");
			}
			out.append("</ul><br>");
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

	@Override
	public void setReponse(String responseText) {
		responses = Arrays.asList( responseText.split("\\s*|||\\s*") );
		points = 0;
		if ( ordered ) {
			for ( int i = 0; i < answerSlotsNum; i++ ) {
				if ( correctAnswers.get(i).contains( responses.get(i) ) ) {
					points++;
				}
			}
		}
		else {
			List<Integer> gotPoint = new ArrayList<Integer>();
			for ( int j = 0; j < correctAnswers.size(); j++ ) {
				gotPoint.set(j, 0);
			}
			for ( int i = 0; i < answerSlotsNum; i++ ) {
				for ( int j = 0; j < correctAnswers.size(); j++ ) {
					if ( correctAnswers.get(j).contains(responses.get(i)) ) {
						gotPoint.set(j, 1);
					}
				}
			}
			for ( int j = 0; j < correctAnswers.size(); j++ ) {
				points += gotPoint.get(j);
			}
		}
	}
}
