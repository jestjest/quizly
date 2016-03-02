package quizme.links;

import java.sql.Timestamp;

public class QuizSummaryInfo {
	/**
	 * The quiz ID.
	 */
	private int quizID;

	/**
	 * The name of quiz.
	 */
	private String name;
	
	/**
	 * Quiz description text.
	 */
	private String description;

	/**
	 * The user name of person who created the quiz
	 */
	private String creatorUsername;

	/**
	 * The date quiz is created.
	 */
	private Timestamp dateCreated;
	
	/**
	 * How many times people have taken this quiz.
	 */
	private int numberTaken;
	
	public QuizSummaryInfo( int quizID, String name, String description, 
			String creatorUsername, Timestamp dateCreated, int numberTaken) {
		this.quizID = quizID;
		this.name = name;
		this.description = description;
		this.creatorUsername = creatorUsername;
		this.dateCreated = dateCreated;
		this.numberTaken = numberTaken;
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
	 * Returns the quiz description text.
	 * @return
	 */
	public String getDescription() {
		return description;
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
	 * Returns number of times this quiz has been taken
	 * @return
	 */
	public int getNumberTaken() {
		return numberTaken;
	}
	
	
}
