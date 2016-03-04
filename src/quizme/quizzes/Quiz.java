// Quiz.java
package src.quizme.quizzes;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Quiz class encapsulating a single quiz and its information.
 */
public class Quiz {
	private int numOfQuestions;
	private boolean randomOrder; 
	private ArrayList<Question> questions;
	private ArrayList<Integer> questionIndexes;
	
	private long startTime; /* may not use this timing approach */
	private long endTime;
	
	public Quiz(int numOfQuestions, boolean randomOrder) {
		this.numOfQuestions = numOfQuestions;
		this.randomOrder = randomOrder;
		
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
