package quizme.links;

import java.sql.Timestamp;

/**
 * A class to store the statistics of the performance of a user.
 * @author hadip
 *
 */
public class Performance {
	public String username;
	public float score;
	public long time;
	public Timestamp dateTaken;
	public Performance( String username, float score, long time, Timestamp dateTaken ) {
		this.username = username;
		this.score = score;
		this.time = time;
		this.dateTaken = dateTaken;
	}
}