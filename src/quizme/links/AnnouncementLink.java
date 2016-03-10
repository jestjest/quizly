package quizme.links;

import java.sql.Timestamp;

public class AnnouncementLink {
	/**
	 * The id of the announcement.
	 */
	private int announcementID;
	
	/**
	 * The subject of the announcement.
	 */
	private String subject;
	
	/**
	 * The text of the announcement.
	 */
	private String content;
	
	/**
	 * The date announcement has been made.
	 */
	private Timestamp date;
	
	public AnnouncementLink( int id, String subject, String content, Timestamp date) {
		this.announcementID = id;
		this.subject = subject;
		this.content = content;
		this.date = date;
	}
	
	/**
	 * Returns the id of announcement.
	 * @return
	 */
	public int announcementID() {
		return announcementID;
	}
	
	/**
	 * Returns the subject of announcement.
	 * @return
	 */
	public String subject() {
		return subject;
	}
	
	/**
	 * Returns the content of announcement.
	 * @return
	 */
	public String content() {
		return content;
	}
	
	/**
	 * Returns a Timestamp object consists of the date
	 * announcement has been made.
	 * @return
	 */
	public Timestamp date() {
		return date;
	}
}
