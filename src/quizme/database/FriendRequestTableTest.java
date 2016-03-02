package quizme.database;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import quizme.DBConnection;

public class FriendRequestTableTest {
	static DBConnection db;
	static FriendRequestTable friendRequestDB;
	
	@BeforeClass
	public static void oneTimeSetUp() {
		db = new DBConnection();
		friendRequestDB = new FriendRequestTable(db);
		friendRequestDB.removeFriendRequest("Romeo", "Juliet");
		friendRequestDB.removeFriendRequest("LadyMacbeth", "Macbeth");
		friendRequestDB.removeFriendRequest("Hamlet", "Ophelia");
	}
	
	@AfterClass
	public static void oneTimeTearDown() {
		db.closeConnection();
	}
	
	@Test
	public void basictest() {
		friendRequestDB.addFriendRequest("Romeo", "Juliet");
		friendRequestDB.addFriendRequest("LadyMacbeth", "Macbeth");
		friendRequestDB.addFriendRequest("Hamlet", "Ophelia");
		
		assertTrue(friendRequestDB.pendingFriendRequest("LadyMacbeth", "Macbeth"));
		assertFalse(friendRequestDB.pendingFriendRequest("Romeo", "Ophelia"));
		
		friendRequestDB.removeFriendRequest("LadyMacbeth", "Macbeth");
		assertFalse(friendRequestDB.pendingFriendRequest("LadyMacbeth", "Macbeth"));
	}
}
