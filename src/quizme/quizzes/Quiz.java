// Quiz.java
package src.quizme.quizzes;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Quiz class encapsulating a single quiz and its information.
 */
public class Quiz {
	private int quizid;
	private String name;
	private String description;
	private long startTime; //start time??
	private long endTime;
	private int numOfQuestions;
	
	private boolean singlePage;
	private boolean randomOrder;
	private boolean immediateFeedback; //from quiz summary page, not database
	private boolean practiceMode; //from quiz summary page, not database
	
	private ArrayList<Question> questions;
	private ArrayList<Integer> questionIndexes;
	
	public Quiz(int quizid, String name, String description, int numOfQuestions, boolean singlePage, boolean randomOrder, 
			boolean immediateFeedback, boolean practiceMode) {
		this.quizid = quizid;
		this.name = name;
		this.description = description;
		this.numOfQuestions = numOfQuestions;
		
		this.singlePage = singlePage;
		this.randomOrder = randomOrder;
		this.immediateFeedback = immediateFeedback;
		this.practiceMode = practiceMode;
		
		this.questions = new ArrayList<Question>(numOfQuestions);
		this.questionIndexes = new ArrayList<Integer>(numOfQuestions);
		setQuestionOrder();
	}
	
	private void setQuestionOrder() {
		for (int i = 0; i < numOfQuestions; i++)
			questionIndexes.set(i, i);
		if (randomOrder) Collections.shuffle(questionIndexes);
	}
	
	public void setQuestion(Integer order, Question question) {
		int index = questionIndexes.get(order);
		questions.set(index, question);
	}
	
	public Question getQuestion(int index) {
		return questions.get(index);
	}
	
	public void beginTiming() {
		startTime = System.currentTimeMillis();
	}
	
	public void endTiming() {
		endTime = System.currentTimeMillis();
	}
}
