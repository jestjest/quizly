package quizme.links;

import java.sql.Timestamp;

public class MessageLink {
	
	/**
	 * Message ID
	 */
	private int messageID;
	
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
	 * The content of the message.
	 * For TEXT and FRIENDSHIP it is the actual message.
	 * For CHALLENGE it includes the quizID.
	 */
	private String content;
		
	/**
	 * A boolean that shows if the message is read.
	 */
	private Boolean seen;
	
	/**
	 * Type of this message
	 */
	private MType type;
	
	/**
	 * Different message types.
	 */
	public static enum MType {
		TEXT, CHALLENGE, FRIENDSHIP
	}
	
	/**
	 * Constructor
	 * @param senderUsername
	 * @param subject
	 * @param date
	 * @param type
	 */
	public MessageLink( int messageID, String senderUsername, String subject, Timestamp date, String content,
			Boolean seen, MType type) {
		this.messageID = messageID;
		this.senderUsername = senderUsername;
		this.subject = subject;
		this.date = date;
		this.content = content;
		this.seen = seen;
		this.type = type;
	}
	
	/**
	 * Returns message ID
	 * @return
	 */
	public int getID() {
		return messageID;
	}
	
	/**
	 * Return user name of the person sent the message.
	 * @return
	 */
	public String getSenderUsername() {
		return senderUsername;
	}
	
	/**
	 * Returns an HTML link for a message's sender page
	 */
	public String getSenderLink() {
		return "<a href='UserPageServlet?name=" + senderUsername + "' class='btn'>" + senderUsername + "</a>";
	}
	
	/**
	 * Returns the subject of the message.
	 * 
	 * NOTE: If the message is a challenge, the subject contains an HTML link to the quiz summary page.
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
	
	/**
	 * Returns the message content.
	 * 
	 * NOTE: If the message is a challenge, the content contains the score
	 * @return
	 */
	public String getContent() {
		return content;
	}
	
	/**
	 * Returns a boolean which shows if the message
	 * has been seen (read).
	 * @return
	 */
	public Boolean seen() {
		return seen;
	}
}
