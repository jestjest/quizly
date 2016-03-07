package quizme.links;

import java.sql.Timestamp;
import java.util.List;

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
	private Timestamp modifiedDate;
		
	/**
	 * A boolean that shows if this quiz has a practice mode.
	 */
	private boolean hasPractice;
	
	/**
	 * number of questions
	 */
	private int numOfQuestions;
	
	/**
	 * Is the ordering random?
	 */
	private boolean randomOrder;
	
	/**
	 * Is this a one-page quiz?
	 */
	private boolean onePage;
	
	/**
	 * Does user immediately see the correct asnwers?
	 */
	private boolean immediateCorrection;
	
	/**
	 * Summary statistics of the visiting user.
	 */
	private SummaryStat mySummaryStat;
	
	/**
	 * Summary statistics of all users.
	 */
	private SummaryStat allSummaryStat;

	public QuizSummaryInfo( int quizID, String name, String description, 
			String creatorUsername, Timestamp modifiedDate, boolean hasPractice, int numOfQuestions,
			boolean randomOrder, boolean onePage, boolean immediateCorrection,
			SummaryStat mySummaryStat, SummaryStat allSummaryStat,
			List<Performance> myPerformance, List<Performance> highestPerformers, 
			List<Performance> topLastDayPerformers, List<Performance> recentPerformers) {
		this.quizID = quizID;
		this.name = name;
		this.description = description;
		this.creatorUsername = creatorUsername;
		this.modifiedDate = modifiedDate;
		this.hasPractice = hasPractice;
		this.numOfQuestions = numOfQuestions;
		this.randomOrder = randomOrder;
		this.onePage = onePage;
		this.immediateCorrection = immediateCorrection;
		this.mySummaryStat = mySummaryStat;
		this.allSummaryStat = allSummaryStat;
		this.myPerformances = myPerformance;
		this.highestPerformers = highestPerformers;
		this.topLastDayPerformers = topLastDayPerformers;
		this.recentPerformers = recentPerformers;
	}
	
	private List<Performance> myPerformances;
	private List<Performance> highestPerformers;
	private List<Performance> topLastDayPerformers;
	private List<Performance> recentPerformers;
	
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
	public Timestamp getModifiedDate() {
		return modifiedDate;
	}
	
	/**
	 * Returns true if this quiz has a practice mode
	 * @return
	 */
	public boolean hasPractice() {
		return hasPractice;
	}
	
	
	/**
	 * Returns a SummaryStat object containing users' minimal stat.
	 * @return
	 */
	public SummaryStat mySummaryStat() {
		return mySummaryStat;
	}
	
	/**
	 * Returns a SummaryStat object containing all users' minimal stat.
	 * @return
	 */
	public SummaryStat allSummaryStat() {
		return allSummaryStat;
	}
	
	/**
	 * Returns number of questions of this quiz.
	 * @return
	 */
	public int numOfQuestions() {
		return numOfQuestions;
	}
	
	/**
	 * Returns true if the order of questions is random.
	 * @return
	 */
	public boolean randomOrder() {
		return randomOrder;
	}
	
	/**
	 * Returns true if this is a single-page quiz.
	 * @return
	 */
	public boolean onePage() {
		return onePage;
	}
	
	/**
	 * Returns true if user sees immediately the correct responses.
	 * @return
	 */
	public boolean immediateCorrection() {
		return immediateCorrection;
	}
	
	public List<Performance> getMyPerformances() {
		return myPerformances;
	}
	
	public List<Performance> getBestPerformances() {
		return highestPerformers;
	}
	
	public List<Performance> getTopDayPerformances() {
		return topLastDayPerformers;
	}
	
	public List<Performance> getRecentPerformances() {
		return recentPerformers;
	}
}
