// User.java
package quizme;

/**
 * Class representing a user in the quiz system.
 */
public class User {
	private String username;
	private boolean isAdmin;
	
	public User(String username, boolean isAdmin) {
		this.username = username;
		this.isAdmin = isAdmin;
	}
	
	public String getName() { return username; }
	public boolean isAdmin() { return isAdmin; }
}
