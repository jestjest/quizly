package quizme.links;

import java.util.Date;

public class MessageLink {
	
	/**
	 * User name of the person sent the message.
	 */
	public String senderUsername;
	
	/**
	 * The subject of the message.
	 */
	public String subject;
	
	/**
	 * The date message sent.
	 */
	public Date date;
	
	/**
	 * Different message types.
	 */
	public static enum MType {
		TEXT, CHALLENGE, FRIENDSHIP
	}
	
	/**
	 * Type of this message
	 */
	public MType type;

}
