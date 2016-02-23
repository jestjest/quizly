package project;

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

	public QuestionResponse( int n ) {
		super(n);
		type = 0; // This is a Question-Response type.
		questionText = "question";
		responseText = "response";
		correctResponseText = "correct response";
		maxPoints = 1;	
	}

	@Override
	public void show( StringBuilder out ) {
		out.append("<b>");
		out.append(questionText);
		out.append("</b>");
		out.append("<br>");
		out.append("<input type=\"text\" name=\"responseText_" + Integer.toString(number) 
		+ "\" value=\""	+ responseText + "\">");
	}

	@Override
	public void create( StringBuilder out ) {
		out.append("<input type=\"text\" name=\"questionText_" + Integer.toString(number) 
		+ "\" value=\""	+ questionText + "\">");
		out.append("<br>");
		out.append("<input type=\"text\" name=\"correctResponseText_" + Integer.toString(number) 
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
	public void update( String key, Object value) {
		switch( key ) {
		case "questionText":
			questionText = (String) value;
		break;
		case "responseText":
			responseText = (String) value;
		break;
		case "correctResponseText":
			correctResponseText = (String) value;
		break;
		}
	}
}
