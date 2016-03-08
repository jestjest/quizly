package quizme.quizzes;

import java.io.IOException;
import java.sql.ResultSet;

import javax.servlet.jsp.JspWriter;

/**
 * An abstract class used as a framework to implement different types
 * of questions.
 * @author hadip
 *
 */
public abstract class Question {
	/**
	 * This is the list of all Question types extending this class.
	 */
	public static enum TYPE {
		QR, BLANK, MC, PICTURE, TF, MA
	}		

	/**
	 * Every question type should implement this function. The function 
	 * returns the column names of the corresponding table in the 
	 * data base.
	 * @return array of strings consisting column names of the
	 * corresponding table in the data base.
	 */
	public abstract String[] columnNames();

	/**
	 * Every question type should implement this function. The function 
	 * returns the column types of the corresponding table in the 
	 * data base.
	 * @return array of strings consisting column types of the
	 * corresponding table in the data base.
	 */
	public abstract String[] columnTypes();


	/**
	 * Every question type should implement this function. This 
	 * returns the question type.
	 * @return an integer shows question type.
	 * @see #TYPES
	 */
	public abstract TYPE type();

	/**
	 * [order] represents the question number.
	 * This is also can be served as a unique id for each question
	 * within a quiz.
	 */
	public int order;

	/**
	 * An integer representing the ID of the quiz encapsulating the 
	 * question.
	 */
	public int quizID;

	/**
	 * Every question type should implement this function. This function
	 * returns the maximum achievable points of the question.
	 * @return an integer representing the maximum achievable points.
	 */
	public abstract int maxPoints();
	
	/**
	 * Every question class should implement this function. This function
	 * gets the user response as a string, and updates the ivar (including points)
	 * accordingly.
	 * @param response
	 */
	public abstract void setResponse( String response );

	/**
	 * show the points user earned on this question.
	 */
	protected int points;


	/**
	 * Constructor: create an instance of a question using one row
	 * of the corresponding data base.
	 * @param rs a ResultSet object pointing to a row in the table 
	 * storing the question data.
	 */
	public Question( ResultSet rs ) {
		points = 0;
	}
	
	/**
	 * Default constructor.
	 */
	public Question() {
		points = 0;
	}

	/**
	 * Each question type should override this function.
	 * Call this function to print the content of the question. 
	 * @param out a StringBuilder used to write contents
	 * question contents on. We assume header info, etc. are already
	 * written to the PrintWriter object, and it is in the <body> section.
	 */
	public void show( JspWriter out, int questionIndex ) throws IOException {
		out.append("<h1>Super question</h1>");
	}

	/**
	 * Show the necessary fields for this question type, allowing the
	 * user (creator) to create a new question. If the question is 
	 * created previously, this function can be used to edit the contents.
	 * @param out a StringBuilder used to write contents
	 * @see #show(java.io.PrintWriter)
	 */
	public void create( StringBuilder out ) {
		out.append("<h1>Create a super question</h1>");
	}

	/**
	 * Present a question and the answer of user.
	 * @param out a StringBuilder used to write contents.
	 * @see #show(java.io.PrintWriter)
	 */
	public void answer( StringBuilder out ) {
		out.append("<h1>Super answer</h1>");
	}

	/**
	 * Present a summary of the question and user answer.
	 * @param out a StringBuilder used to write contents.
	 * @see #answer(java.io.PrintWriter)
	 */
	public void answerSummary( StringBuilder out ) {
		out.append("Super answer summary");
	}

	/**
	 * For an answered question, returns the points earned.
	 * @return the points earned from this question.
	 */
	public int points() {
		return points;
	}


}
