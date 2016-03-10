package quizme.quizzes;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

import quizme.database.AchievementGuidelinesTable;
import quizme.database.AchievementsTable;
import quizme.database.QuizResultsTable;
import quizme.database.QuizTable;

public class Achievement {
	public static final int CREATE = 1;
	public static final int TAKE = 2;
	public static final int HIGH_SCORE = 3;
	public static final int PRACTICE = 4;
	
	private static final int MAX_NUM_OF_QUIZZES_CREATED = 10;
	private static final int MAX_NUM_OF_QUIZZES_TAKEN = 20;
	
	/* Check for new achievements after a user has created a quiz and the quiz table has been updated */
	public static List<AchievementGuidelinesData> checkForCreateAchievements(String username, QuizTable quizTable, 
													AchievementsTable achievementsTable, 
													AchievementGuidelinesTable guidelinesTable) {
		List<AchievementGuidelinesData> list = new ArrayList<AchievementGuidelinesData>();
		Timestamp allTime = new Timestamp(0);
		int numOfQuizzesCreated = quizTable.getRecentQuizzesCreated(username, MAX_NUM_OF_QUIZZES_CREATED, allTime).size();
		
		ResultSet rs = guidelinesTable.getAchievementGuidelinesByType(CREATE);
		try {
			while(rs.next()) {
				if (numOfQuizzesCreated >= rs.getInt("minimumValue") && 
					!achievementsTable.hasAchievement(username, rs.getString("name"))) {
					list.add(new AchievementGuidelinesData(rs.getString("name"), rs.getString("pictureURL"), 
							rs.getString("description")));
					Timestamp now = new Timestamp(System.currentTimeMillis());
					achievementsTable.addAchievement(username, rs.getString("name"), now);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/* Check for new achievements after a user has finished the quiz and the quiz results table has been updated */
	public static List<AchievementGuidelinesData> checkForTakeAchievements(String username, QuizResultsTable resultsTable,
									AchievementsTable achievementsTable, AchievementGuidelinesTable guidelinesTable) {
		List<AchievementGuidelinesData> list = new ArrayList<AchievementGuidelinesData>();
		Timestamp allTime = new Timestamp(0);
		int numOfQuizzesTaken = resultsTable.getRecentQuizzesTaken(username, MAX_NUM_OF_QUIZZES_TAKEN, allTime).size();
		ResultSet rs = guidelinesTable.getAchievementGuidelinesByType(TAKE);
		try {
			while(rs.next()) {
				if (numOfQuizzesTaken >= rs.getInt("minimumValue") && 
					!achievementsTable.hasAchievement(username, rs.getString("name"))) {
					list.add(new AchievementGuidelinesData(rs.getString("name"), rs.getString("pictureURL"), 
							rs.getString("description")));
					Timestamp now = new Timestamp(System.currentTimeMillis());
					achievementsTable.addAchievement(username, rs.getString("name"), now);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	private static final String HIGH_SCORE_ACHIEVEMENT_NAME = "I Am The Greatest!";
	public static AchievementGuidelinesData checkForHighScoreAchievement(String username, double score, int quizid, QuizResultsTable resultsTable,
													   AchievementsTable achievementsTable, AchievementGuidelinesTable guidelinesTable) {
		if (!achievementsTable.hasAchievement(username, HIGH_SCORE_ACHIEVEMENT_NAME) && 
			score >= resultsTable.getHighScore(quizid)) {
			Timestamp now = new Timestamp(System.currentTimeMillis());
			achievementsTable.addAchievement(username, HIGH_SCORE_ACHIEVEMENT_NAME, now);
			return getAchievementGuidelinesData(HIGH_SCORE_ACHIEVEMENT_NAME, guidelinesTable);
		} else {
			return null;
		}
	}
	
	private static final String PRACTICE_ACHIEVEMENT_NAME = "Practice Makes Perfect";
	public static AchievementGuidelinesData checkForPracticeModeAchievement(String username, AchievementsTable achievementsTable,
														  AchievementGuidelinesTable guidelinesTable) {
		if (!achievementsTable.hasAchievement(username, PRACTICE_ACHIEVEMENT_NAME)) {
			Timestamp now = new Timestamp(System.currentTimeMillis());
			achievementsTable.addAchievement(username, PRACTICE_ACHIEVEMENT_NAME, now);
			return getAchievementGuidelinesData(PRACTICE_ACHIEVEMENT_NAME, guidelinesTable);
		} else {
			return null;
		}
	}
	
	public static AchievementGuidelinesData getAchievementGuidelinesData(String name, AchievementGuidelinesTable guidelinesTable) {
		ResultSet rs = guidelinesTable.getAchievementGuidelineByName(name);
		try {
			AchievementGuidelinesData data = new AchievementGuidelinesData(name, rs.getString("pictureURL"), rs.getString("description"));
			return data;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static class AchievementGuidelinesData {
		public final String name;
		public final String pictureURL;
		public final String description;
		
		public AchievementGuidelinesData (String name, String pictureURL, String description) {
			this.name = name;
			this.pictureURL = pictureURL;
			this.description = description;
		}
	}
}
