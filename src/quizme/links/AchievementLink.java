package quizme.links;

import java.util.Date;


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
	public Date date;
	
	public AchievementLink ( String name, String username, Date date) {
		this.name = name;
		this.username = username;
		this.date = date;
	}
}
