// Quiz.java
package quizme;

import java.util.ArrayList;

/**
 * Quiz class encapsulating a single quiz and its information.
 */
public class Quiz {
	private ArrayList<Question> questions;
	boolean singlePage;
	boolean randomOrder;
	boolean immediateFeedback;
	String description;
	String purpose;
	long timeTaken;
	int numQuestions;
}
