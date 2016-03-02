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
	 * Returns an HTML link element for the quiz link.
	 */
	public String getQuizLink() {
		return "<a href='QuizSummaryServlet?id=" + quizID + "' class='btn'>" + name + "</a>";
	}
	
	/**
	 * Returns an HTML link element for the creator.
	 */
	public String getCreatorLink() {
		return "<a href='UserPageServlet?name=" + creatorUsername + "' class='btn'>" + creatorUsername + "</a>";
	}
	
	/**
	 * Return an HTML link element for the quiz-taker
	 */
	public String getTakerLink() {
		return "<a href='UserPageServlet?name=" + takenUsername + "' class='btn'>" + takenUsername + "</a>";
	}
	
	/**
	 * Returns the Timestamp instance variable for when the quiz was created
	 */
	public Timestamp getDateCreated() {
		return dateCreated;
	}

	/**
	 * Returns the Timestamp instance variable for when the quiz was taken
	 */
	public Timestamp getDateTaken() {
		return dateTaken;
	}
	
	/**
	 * Returns the score that a specific user got on a quiz.
	 */
	public float getScore() {
		return score;
	}
	
	/**
	 * Returns the number of times the quiz has been taken
	 */
	public int getNumTaken() {
		return numberTaken;
	}
}