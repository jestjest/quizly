package quizme.tests;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import quizme.database.FriendTable;
import quizme.DBConnection;

public class FriendTableTest {
	static DBConnection db;
	static FriendTable friendDB;
	
	@BeforeClass
	public static void oneTimeSetUp() {
		db = new DBConnection();
		friendDB = new FriendTable(db);
		friendDB.clearAllFriends();
	}
	
	@AfterClass
	public static void oneTimeTearDown() {
		db.closeConnection();
	}
	
	@Test
	public void basictest() {
		String usernames[] = {"jake", "john", "jess", "jammie", "james", "jackie", "judy", "justin"};
		for(int i = 0; i < usernames.length-1; i++) {
			friendDB.addFriends(usernames[i], usernames[i+1]);
		}
		for (int i = 0; i < usernames.length/2 - 1; i++) {
			friendDB.addFriends(usernames[usernames.length - 1 - i], usernames[i]);
		}
		assertTrue(friendDB.areFriends("jake", "john"));
		assertFalse(friendDB.areFriends("jake", "jess"));
		assertTrue(friendDB.areFriends("jake", "justin"));
		assertTrue(friendDB.areFriends("jess", "john"));
		assertTrue(friendDB.areFriends("judy", "justin"));
		
		List<String> jackieFriends = new LinkedList<String>();
		jackieFriends.add("james");
		jackieFriends.add("judy");
		jackieFriends.add("jess");
		List<String> friends = friendDB.friendsList("jackie");
		for (int i = 0; i < friends.size(); i++) {
			String friend = friends.get(i);
			assertFalse(friend.equals("jackie"));
			jackieFriends.remove(friend);
		}
		assertTrue(jackieFriends.size() == 0);
		
		friendDB.removeFriends("jake", "john");
		assertFalse(friendDB.areFriends("jake", "john"));
		friendDB.removeFriends("judy", "jackie");
		assertFalse(friendDB.areFriends("judy", "jackie"));
	}
}
