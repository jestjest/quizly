package quizme.links;

import java.sql.Timestamp;

public class QuizLink {

	/**
	 * The quiz ID.
	 */
	private int quizID;

	/**
	 * The name of quiz.
	 */
	private String name;

	/**
	 * The user name of person who created the quiz
	 */
	private String creatorUsername;

	/**
	 * The date quiz is created.
	 */
	private Timestamp dateCreated;

	/**
	 * The date quiz is taken.
	 */
	private Timestamp dateTaken;

	/**
	 * How many times people have taken this quiz.
	 */
	private int numberTaken;

	/**
	 *user name of a person that has taken this quiz
	 */
	private String takenUsername;

	/**
	 * The score of the person who has taken the quiz ( in percentage ).
	 */
	private float score;

	public QuizLink( int quizID, String name, String creatorUsername, Timestamp dateCreated,
			Timestamp dateTaken, int numberTaken, String takenUsername, float score) {
		this.quizID = quizID;
		this.name = name;
		this.creatorUsername = creatorUsername;
		this.dateCreated = dateCreated;
		this.dateTaken = dateTaken;
		this.numberTaken = numberTaken;
		this.takenUsername = takenUsername;
		this.score = score;
	}

	/**
	 * Returns quiz ID
	 * @return
	 */
	public int getQuizID() {
		return quizID;
	}
	
	/**
	 * Returns the name of this quiz
	 * @return
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Returns the user name of the person who has created this quiz
	 * @return
	 */
	public String getCreatorUsername() {
		return creatorUsername;
	}
	
	/**
	 * Returns the time that quiz is created
	 * @return a Timestamp object
	 */
	public Timestamp getDateCreated() {
		return dateCreated;
	}

	/**
	 * Returns the time that this quiz is taken
	 * @return a Timestamp object
	 */
	public Timestamp getDateTaken() {
		return dateTaken;
	}
	
	/**
	 * Returns number of times this quiz has been taken
	 * @return
	 */
	public int getNumberTaken() {
		return numberTaken;
	}
	
	/**
	 * Returns the user name of the person who has taken this quiz
	 * @return
	 */
	public String getTakenUsername() {
		return takenUsername;
	}
	
	/**
	 * Returns the score (in percentage) of the person who has taken the quiz
	 * @return
	 */
	public float getScore() {
		return score;
	}
}
