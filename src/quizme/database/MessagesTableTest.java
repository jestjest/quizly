package quizme.database;

import static org.junit.Assert.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import quizme.DBConnection;

public class MessagesTableTest {
	static DBConnection db;
	static MessagesTable messagesDB;
	
	@BeforeClass
	public static void oneTimeSetUp() {
		db = new DBConnection();
		messagesDB = new MessagesTable(db);
		messagesDB.clearAllMessages();
	}
	
	@AfterClass
	public static void oneTimeTearDown() {
		db.closeConnection();
	}
	
	@Test
	public void basictest() {
		Timestamp date1 = new Timestamp(2016 - 1900, 02, 28, 0, 0, 0, 0);
		Timestamp date2 = new Timestamp(2016 - 1900, 02, 1, 0, 0, 0, 0);
		Timestamp date3 = new Timestamp(2005 - 1900, 02, 27, 0, 0, 0, 0);
		Timestamp date4 = new Timestamp(1990 - 1900, 02, 27, 0, 0, 0, 0);
		
		int id1 = messagesDB.addMessage("receiver", "sender", date1, "quizid", "Game on!!", MessagesTable.CHALLENGE);
		int id2 = messagesDB.addMessage("receiver", "another sender", date2, "Hi Receiver! Hope all is well", "Just saying hi", MessagesTable.NOTE);
		int id3 = messagesDB.addMessage("another receiver", "another sender", date3, "Howdy partner!", "Thinking of you!", MessagesTable.NOTE);
		int id4 = messagesDB.addMessage("receiver", "quiz master", date4, "Please add Jacob as a friend", "You have a friend request", MessagesTable.REQUEST);
		
		assertFalse(messagesDB.getSeen(id1));
		messagesDB.setSeen(id1, true);
		assertTrue(messagesDB.getSeen(id1));
		
		ResultSet rs = messagesDB.getAllUserReceivedMessages("receiver");
		try {
			int count = 0;
			//rs.first();
			//assertTrue(rs.getString("fromUsername").equals("sender"));
			//assertTrue(rs.getString("subject").equals("Game on!!"));
			//assertTrue(rs.getString("content").equals("quizid"));
			//assertEquals(rs.getInt("type"), MessagesTable.CHALLENGE);
			//assertTrue(rs.getTimestamp("date").equals(date1));
			//count++;
			while(rs.next()) {
				System.out.println(rs.getString("subject"));
				assertFalse(rs.getString("fromUsername").equals("receiver"));
				count++;
			}
			assertEquals(count, 3);
		} catch (SQLException e) {
				e.printStackTrace();
		}
		
		rs = messagesDB.getRecentUserReceivedMessages("receiver", 2);
		try {
			int count = 0;
			while(rs.next()) {
				count++;
			}
			assertEquals(count, 2);
		} catch (SQLException e) {
				e.printStackTrace();
		}
		
		rs = messagesDB.getAllUserSentMessages("another sender");
		try {
			int count = 0;
			while(rs.next()) {
				count++;
			}
			assertEquals(count, 2);
		} catch (SQLException e) {
				e.printStackTrace();
		}
	}
}
