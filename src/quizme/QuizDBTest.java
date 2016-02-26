package quizme;

import static org.junit.Assert.*;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.*;

public class QuizDBTest {
	static DBConnection db;
	static QuizDB quizDB;
	
	@BeforeClass
	public static void oneTimeSetUp() {
		db = new DBConnection();
		quizDB = new QuizDB(db);
		quizDB.removeQuiz(1); /* keep replacing quiz 1 in basic test */
		quizDB.removeQuiz(2); /* keep replacing quiz 2 in basic test */
	}
	
	@AfterClass
	public static void oneTimeTearDown() {
		db.closeConnection();
	}
	
	@Test
	public void basictest() {
		int quizid = quizDB.addQuiz("testQuiz", "fake description", "just for testing");
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
		assertEquals(numOfQuestions, 0);
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
		
		int quizid2 = quizDB.addQuiz("Fake2", "fake quiz!", "more testing");
		assertEquals(quizid2, 2);
		
		ResultSet rs = quizDB.getEntry(quizid2);
		try {
			rs.first();
			assertTrue(rs.getString("name").equals("Fake2"));
			assertEquals(rs.getInt("numOfQuestions"), 0);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}