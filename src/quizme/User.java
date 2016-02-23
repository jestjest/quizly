// User.java
package quizme;

/**
 * Class representing a user in the quiz system.
 */
public class User {
	private String username;
	
	public User(String username) {
		this.username = username;
	}
	
	public String getName() { return username; }
}
