package quizme.tests;

import static org.junit.Assert.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.junit.*;

import quizme.DBConnection;
import quizme.database.QuizTable;

public class QuizDBTest {
	static DBConnection db;
	static QuizTable quizDB;
	
	@BeforeClass
	public static void oneTimeSetUp() {
		db = new DBConnection();
		quizDB = new QuizTable(db);
		quizDB.removeQuiz(1); /* keep replacing quiz 1 in basic test */
		quizDB.removeQuiz(2); /* keep replacing quiz 2 in basic test */
	}
	
	@AfterClass
	public static void oneTimeTearDown() {
		db.closeConnection();
	}
	
	@Test
	public void basictest() {
		Timestamp today = new Timestamp(System.currentTimeMillis());
		int quizid = quizDB.addQuiz("testQuiz", "fake description", "just for testing", 50, "master quiz creator", today);
		assertEquals(quizid, 1);
		
		String name = quizDB.getName(quizid);
		assertTrue(name.equals("testQuiz"));
		
		String description = quizDB.getDescription(quizid);
		assertTrue(description.equals("fake description"));
		quizDB.setDescription(quizid, "another fake description");
		description = quizDB.getDescription(quizid);
		assertTrue(description.equals("another fake description"));
		
		String purpose = quizDB.getPurpose(quizid);
		assertTrue(purpose.equals("just for testing"));
		quizDB.setPurpose(quizid, "just for junit testing");
		purpose = quizDB.getPurpose(quizid);
		assertTrue(purpose.equals("just for junit testing"));
		
		int numOfQuestions = quizDB.getNumOfQuestions(quizid);
		assertEquals(numOfQuestions, 50);
		quizDB.setNumOfQuestions(quizid, 5);
		numOfQuestions = quizDB.getNumOfQuestions(quizid);
		assertEquals(numOfQuestions, 5);
		
		assertFalse(quizDB.getRandomOrder(quizid));
		quizDB.setRandomOrder(quizid, true);
		assertTrue(quizDB.getRandomOrder(quizid));
		
		assertFalse(quizDB.getMultiplePages(quizid));
		quizDB.setMultiplePages(quizid, true);
		assertTrue(quizDB.getMultiplePages(quizid));
		
		assertFalse(quizDB.getImmediateCorrection(quizid));
		quizDB.setImmediateCorrection(quizid, true);
		assertTrue(quizDB.getImmediateCorrection(quizid));
		
		assertEquals(quizDB.getNumOfTimesTaken(quizid), 0);
		quizDB.incNumOfTimesTaken(quizid);
		assertEquals(quizDB.getNumOfTimesTaken(quizid), 1);
		
		assertTrue(quizDB.getCreatorUsername(quizid).equals("master quiz creator"));
		
		Timestamp date = quizDB.getCreatedDate(quizid);
		DateFormat df = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
		String dateString = df.format(date);
		String todayString = df.format(today);
		assertTrue(dateString.equals(todayString));
		
		Timestamp tomorrow = new Timestamp(2016 - 1900, 02, 27, 0, 0, 0, 0);
		int quizid2 = quizDB.addQuiz("Fake2", "fake quiz!", "more testing", 0, "another quiz master", tomorrow);
		assertEquals(quizid2, 2);
		
		ResultSet rs = quizDB.getEntry(quizid2);
		try {
			rs.first();
			assertTrue(rs.getString("name").equals("Fake2"));
			assertTrue(rs.getString("creatorUsername").equals("another quiz master"));
			assertTrue(rs.getTimestamp("createdDate").equals(tomorrow));
			assertEquals(rs.getInt("numOfQuestions"), 0);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}