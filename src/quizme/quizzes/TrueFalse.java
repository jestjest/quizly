package quizzes;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.jsp.JspWriter;


public class TrueFalse extends Question {
	
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
	 * A string consists of all choices separated by '~~~'.
	 */
	private String correctChoicesText;
	
	/**
	 * A list of ints showing correct choices
	 */
	private List<Integer> correctChoices;

	/**
	 * A list of integer show the user responses.
	 * 0 <= response[i] < choices.size()
	 * The user, however, put a number in [1, choices.size] range.
	 */
	private  List<Integer> responses;
	
	
	/**
	 * List of column names in the corresponding data base.
	 */
	private static final String[] columnNames = {
			"quizid",
			"questionOrder",
			"question",
			"answerChoices",
			"correctAnswers"
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
	private static final TYPE type = TYPE.TF;
	
	/**
	 * The maximum achievable points of this question.
	 */
	private int maxPoints;
	
	/**
	 * Parse String to an array of ints
	 */
	private  List<Integer> textToInts( String text ) {
		String[] S = textToStrings(text);
		List<Integer> out = new ArrayList<Integer>();
		for ( int i = 0; i < S.length; i++ ) {
			out.add( Integer.parseInt( S[i] ) );
		}
		return out;
	}
	
	/**
	 * Parse String to an array of Strings
	 */
	private String[] textToStrings( String text ) {
		return text.split("\\s*~~~\\s*");
	}
	
	/**
	 * Constructor
	 * @param rs
	 */
	public TrueFalse( ResultSet rs ) throws SQLException {
		super( rs );
		quizID = rs.getInt( columnNames[0] );
		order = rs.getInt( columnNames[1] );
		questionText = rs.getString( columnNames[2] );
		choicesText = rs.getString( columnNames[3] );
		correctChoicesText = rs.getString( columnNames[4] );
		choices = textToStrings( choicesText );
		correctChoices = textToInts(correctChoicesText );
		maxPoints = choices.length; // Is this fair?
	}
	
	public void show( JspWriter out ) throws IOException {
		out.append("<b>");
		out.append(questionText);
		out.append("</b>");
		out.append("<br>");
		for ( int i = 0; i < choices.length; i++) {
			String chechBoxName = "response_" + order + "_" + i;
			out.append(makeCheckBox(chechBoxName,i));
			out.append("<br>");
		}
	}
	
	@Override
	public void answer( StringBuilder out ) {
		out.append("<b>Question: </b>");
		out.append(questionText);
		out.append("<br><ul>");
		for ( int i = 0; i < choices.length; i++ ) {
			out.append("<li>"+choices[i]+" ");
			if ( correctChoices.contains(i) ) {
				out.append("TRUE, your response = ");
			}
			else {
				out.append("TRUE, your response = ");
			}
			if ( responses.contains(i) ) {
				out.append("TRUE");
			}
			else {
				out.append("FALSE");
			}
			out.append("</li>");
		}
		out.append("</ul><br><b>Your points: </b>");
		out.append( Integer.toString( points) );
		out.append("<br>");
	}
	
	private String makeCheckBox( String name, int value) {
		String s = "<input type='checkbox' name='" + name +
				"' value='" + Integer.toString( value )+"'> " + choices[value];
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
	public void setReponse(String response) {
		responses = textToInts ( response );
		points = 0;
		for ( int i = 0; i < responses.size(); i++ ) {
			if ( correctChoices.contains( responses.get(i) ) ) {
				points++;
			}
		}
	}

}
