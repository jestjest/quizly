package quizme.links;

public class WebsiteStats {
	/* all-time stats */
	public final int numOfUsers;
	public final int numOfFriendRelationships; 
	
	/* last day, last week, all-time stats */
	public final int[] numOfQuizzesCreated;
	public final int[] numOfQuizzesTaken;
	public final int[] numOfAchievements;
	public final int[] numOfMessages;
	/* note index 0 = last day, index 1 = last week, index 2 = all-time */
	
	public WebsiteStats(int numOfUsers, int numOfFriendRelationships, int[] numOfQuizzesCreated,
			            int[] numOfQuizzesTaken, int[] numOfAchievements, int[] numOfMessages) {
		this.numOfUsers = numOfUsers;
		this.numOfFriendRelationships = numOfFriendRelationships;
		this.numOfQuizzesCreated = numOfQuizzesCreated;
		this.numOfQuizzesTaken = numOfQuizzesTaken;
		this.numOfAchievements = numOfAchievements;
		this.numOfMessages = numOfMessages;
	}
}
