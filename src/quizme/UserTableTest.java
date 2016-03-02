package quizme.tests;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import quizme.DBConnection;
import quizme.database.UsersTable;

public class UserTableTest {
	static DBConnection db;
	static UsersTable userDB;
	
	@BeforeClass
	public static void oneTimeSetUp() {
		db = new DBConnection();
		userDB = new UsersTable(db);
		userDB.removeUser("Lauren");
		userDB.removeUser("Carol");
		userDB.removeUser("David");
		userDB.removeUser("Jonathan");
		userDB.removeUser("Laura");
		userDB.removeUser("Laurel");
	}
	
	@AfterClass
	public static void oneTimeTearDown() {
		db.closeConnection();
	}
	
	@Test
	public void basictest() {
		userDB.addUser("Lauren", "hashedpassword1");
		userDB.addUser("Carol", "hashedpassword2");
		userDB.addUser("David", "hashedpassword3");
		userDB.addUser("Jonathan", "hashedpassword4");
		userDB.addUser("Laura", "hashedpassword5");
		userDB.addUser("Laurel", "hashedpassword6");
		userDB.setAdmin("Carol", true);
		userDB.setAdmin("David", true);
		
		assertTrue(userDB.usernameAlreadyExists("Carol"));
		assertTrue(userDB.usernameAlreadyExists("David"));
		assertFalse(userDB.usernameAlreadyExists("Joseph"));
		
		assertTrue(userDB.correctPassword("Jonathan", "hashedpassword4"));
		assertTrue(userDB.correctPassword("Laura", "hashedpassword5"));
		assertFalse(userDB.correctPassword("Laurel", "hashedpassword1"));
		assertFalse(userDB.correctPassword("Goofy", "fakepassword"));
		
		assertTrue(userDB.getPassword("Lauren").equals("hashedpassword1"));
		userDB.setPassword("Lauren", "newPassword");
		assertTrue(userDB.getPassword("Lauren").equals("newPassword"));
		
		assertTrue(userDB.getAdmin("Carol"));
		assertFalse(userDB.getAdmin("Jonathan"));
		userDB.setAdmin("Jonathan", true);
		assertTrue(userDB.getAdmin("Jonathan"));
		
		List<String> matches = userDB.getUsernameMatches("laur"); 
		assertEquals(matches.size(), 3);
		
		matches = userDB.getUsernameMatches("a"); 
		assertEquals(matches.size(), 6);
	}
}
