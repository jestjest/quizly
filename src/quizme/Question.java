package quizme;

/**
 * An abstract class used as a framework to implement different types
 * of questions.
 * @author hadip
 *
 */
public abstract class Question {
	/**
	 * This is the list of all Question types extending this class.
	 * 0: Question-Response
	 * 1: Fill in the blank
	 * 2: Multiple Choice
	 * 3: Picture-Response Question
	 */
	static final int[] TYPES = {0,1,2,3};
	
	/**
	 * [type] can take a value from the array [TYPES].
	 * It represents the type of question.
	 */
	public int type;
	
	/**
	 * [number] represents the question number.
	 * This is also can be served as a unique id for each question
	 * within a quiz.
	 */
	public int number;
	
	/**
	 * Show the maximum points can be earned from the question
	 */
	public int maxPoints;
	
	/**
	 * show the points user earned on this question.
	 */
	protected int points;
	
	
	/**
	 * Constructor: create a question with its question number.
	 * @param n the question number
	 */
	public Question( int n ) {
		number = n;
		points = 0;
	}
	
	/**
	 * Each question type should override this function.
	 * Call this function to print the content of the question. 
	 * @param out a StringBuilder used to write contents
	 * question contents on. We assume header info, etc. are already
	 * written to the PrintWriter object, and it is in the <body> section.
	 */
	public void show( StringBuilder out ) {
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
	

	/**
	 * Given a parameter pair, update appropriate fields in the
	 * question class.
	 * @param key is a String consists of name of a field in one 
	 * of the forms for this question.
	 * @param value is an Object required to fill 
	 * the corresponding field.
	 */
	public void update( String key, Object value) {
	}
	
	
}
