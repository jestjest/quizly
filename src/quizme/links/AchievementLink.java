package quizme.links;

import java.sql.Timestamp;


public class AchievementLink {
	/**
	 * Name of the achievement.
	 */
	public String name;
	
	/**
	 * User name of the person who accomplished this achievement.
	 */
	public String username;
	
	/**
	 * The date in which this achievement is accomplished by the user.
	 */
	public Timestamp date;
	
	public AchievementLink ( String name, String username, Timestamp date) {
		this.name = name;
		this.username = username;
		this.date = date;
	}
}
