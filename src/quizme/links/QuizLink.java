package quizme.links;

import java.util.Date;

public class QuizLink {
	/**
	 * The name of quiz.
	 */
	public String name;
	
	/**
	 * The user name of person who created the quiz
	 */
	public String creatorUsername;
	
	/**
	 * The date quiz is created.
	 */
	public Date dateCreated;
	
	/**
	 * How many times people have taken this quiz.
	 */
	public int numberTaken;
	
	/**
	 *user name of a person that has taken this quiz
	 */
	public String takenUsername;
	
	/**
	 * The score of the person who has taken the quiz.
	 */
	public int score;
	
	public QuizLink( String name, String creatorUsername, Date dateCreated,
					int numberTaken, String takenUsername, int score) {
		this.name = name;
		this.creatorUsername = creatorUsername;
		this.dateCreated = dateCreated;
		this.numberTaken = numberTaken;
		this.takenUsername = takenUsername;
		this.score = score;
	}
	

}
