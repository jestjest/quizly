package quizme.links;

import java.sql.Timestamp;


public class AchievementLink {
	/**
	 * Name of the achievement.
	 */
	private String name;
	
	/**
	 * User name of the person who accomplished this achievement.
	 */
	private String username;
	
	/**
	 * The date in which this achievement is accomplished by the user.
	 */
	public Timestamp date;
	
	/**
	 * Constructor
	 * @param name
	 * @param username
	 * @param date
	 */
	public AchievementLink ( String name, String username, Timestamp date) {
		this.name = name;
		this.username = username;
		this.date = date;
	}
	
	/**
	 * Returns name of the achievement.
	 * @return name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Returns the user name of the person who accomplished this achievement.
	 * @return username
	 */
	public String getUsername() {
		return username;
	}
}
