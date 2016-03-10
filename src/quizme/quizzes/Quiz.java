// Quiz.java
package quizme.quizzes;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Quiz class encapsulating a single quiz and its information.
 */
public class Quiz {
	private int numOfQuestions;
	private ArrayList<Question> questions;

	private long startTime; 
	private long endTime;
	
	private float score; // the total score

	public Quiz(int numOfQuestions) {
		this.numOfQuestions = numOfQuestions;
		this.questions = new ArrayList<Question>();
		for (int i = 0; i < numOfQuestions; i++)
			questions.add(null);
	}

	public void setQuestion(int index, Question question) {
		questions.set(index, question);
	}

	public Question getQuestion(int index) {
		return questions.get(index);
	}

	public void removeQuestion(Question question) {
		questions.remove(question);	
	}
	
	public void randomizeQuestionOrder() {
		Collections.shuffle(questions);
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
		if ( sTot!=0 ) {
			score = ((float) s)/((float) sTot) * 100;
		}
		else {
			score = 0;
		}
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
	
	/* accounts for questions that have been removed from the quiz
	  in practice mode */
	public int numOfQuestionsRemaining() {
		return questions.size();
	}
	
}
