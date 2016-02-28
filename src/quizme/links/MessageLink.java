package quizme.links;

import java.sql.Timestamp;

public class MessageLink {
	
	/**
	 * User name of the person sent the message.
	 */
	private String senderUsername;
	
	/**
	 * The subject of the message.
	 */
	private String subject;
	
	/**
	 * The date message sent.
	 */
	private Timestamp date;
	
	/**
	 * Different message types.
	 */
	public static enum MType {
		TEXT, CHALLENGE, FRIENDSHIP
	}
	
	/**
	 * Type of this message
	 */
	private MType type;
	
	/**
	 * Constructor
	 * @param senderUsername
	 * @param subject
	 * @param date
	 * @param type
	 */
	public MessageLink( String senderUsername, String subject, Timestamp date, MType type) {
		this.senderUsername = senderUsername;
		this.subject = subject;
		this.date = date;
		this.type = type;
	}
	
	/**
	 * Return user name of the person sent the message.
	 * @return
	 */
	public String getSenderUsername() {
		return senderUsername;
	}
	
	/**
	 * Returns the subject of the message.
	 * @return
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * Returns the date message sent.
	 * @return
	 */
	public Timestamp getDate() {
		return date;
	}
	
	/**
	 * Returns the message type.
	 * @return
	 */
	public MType getType() {
		return type;
	}
}
