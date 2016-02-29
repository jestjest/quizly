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
		int quizid = quizDB.addQuiz("testQuiz", "fake description", 50, "master quiz creator", today, false, false, true, true, 0);
		assertEquals(quizid, 1);
		
		String name = quizDB.getName(quizid);
		assertTrue(name.equals("testQuiz"));
		
		String description = quizDB.getDescription(quizid);
		assertTrue(description.equals("fake description"));
		quizDB.setDescription(quizid, "another fake description");
		description = quizDB.getDescription(quizid);
		assertTrue(description.equals("another fake description"));
		
		int numOfQuestions = quizDB.getNumOfQuestions(quizid);
		assertEquals(numOfQuestions, 50);
		quizDB.setNumOfQuestions(quizid, 5);
		numOfQuestions = quizDB.getNumOfQuestions(quizid);
		assertEquals(numOfQuestions, 5);
		
		assertFalse(quizDB.getRandomOrder(quizid));
		quizDB.setRandomOrder(quizid, true);
		assertTrue(quizDB.getRandomOrder(quizid));
		
		assertTrue(quizDB.getOnePage(quizid));
		quizDB.setOnePage(quizid, false);
		assertFalse(quizDB.getOnePage(quizid));
		
		assertFalse(quizDB.getImmediateCorrection(quizid));
		quizDB.setImmediateCorrection(quizid, true);
		assertTrue(quizDB.getImmediateCorrection(quizid));
		
		assertTrue(quizDB.getPracticeMode(quizid));
		quizDB.setPracticeMode(quizid, false);
		assertFalse(quizDB.getPracticeMode(quizid));
		
		assertEquals(quizDB.getNumOfTimesTaken(quizid), 0);
		quizDB.incNumOfTimesTaken(quizid);
		assertEquals(quizDB.getNumOfTimesTaken(quizid), 1);
		
		assertTrue(quizDB.getCreatorUsername(quizid).equals("master quiz creator"));
		
		Timestamp date = quizDB.getCreatedDate(quizid);
		DateFormat df = new SimpleDateFormat("YYYY-MM-dd HH:mm");
		String dateString = df.format(date);
		String todayString = df.format(today);
		assertTrue(dateString.equals(todayString));
		
		Timestamp anotherDate = new Timestamp(2016 - 1900, 02, 27, 0, 0, 0, 0);
		int quizid2 = quizDB.addQuiz("Fake2", "fake quiz!", 5, "another quiz master", anotherDate, true, true, true, true, 45);
		assertEquals(quizid2, 2);
		
		ResultSet rs = quizDB.getEntry(quizid2);
		try {
			rs.first();
			assertTrue(rs.getString("name").equals("Fake2"));
			assertTrue(rs.getString("creatorUsername").equals("another quiz master"));
			assertTrue(rs.getTimestamp("createdDate").equals(anotherDate));
			assertEquals(rs.getInt("numOfQuestions"), 5);
			assertEquals(rs.getInt("numOfTimesTaken"), 45);
			assertTrue(rs.getBoolean("practiceMode"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}