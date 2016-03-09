// Quiz.java
package quizme.quizzes;

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
	
	private float score; // the total score

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
		int index = questionIndexes.indexOf(order);
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
	
	public long getTime() {
		return endTime - startTime;
	}
	
	/**
	 * Compute and returns the total score ( in percent )
	 */
	public void computeScore() {
		int s = 0;
		int sTot = 0;
		for ( Question q: questions ) {
			s += q.points();
			sTot += q.maxPoints();
		}
		score = ((float) s)/((float) sTot) * 100;
	}
	
	/**
	 * @return score (in percent)
	 */
	public float getScore() {
		return score;
	}
	
	/**
	 * @return numOfQuestions
	 */
	public int numOfQuestions() {
		return numOfQuestions;
	}
	
}
