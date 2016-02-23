package quizme;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class QuestionResponseDBTest {
	static DBConnection db;
	static QuestionResponseDB questionResponseDB;
	
	@BeforeClass
	public static void oneTimeSetUp() {
		db = new DBConnection();
		questionResponseDB = new QuestionResponseDB(db);
		questionResponseDB.removeQuestion("QR0000700300"); /* keep replacing question QR2707 in basic test */
	}
	
	@AfterClass
	public static void oneTimeTearDown() {
		db.closeDatabase();
	}
	
	@Test
	public void basictest() {
		String questionid = questionResponseDB.addQuestion(7, 300, "When was The War of 1812?", "1812");
		assertTrue(questionid.equals("QR0000700300"));
		
		int quizid = questionResponseDB.getQuizID(questionid);
		assertEquals(quizid, 7);
		
		int order = questionResponseDB.getQuestionOrder(questionid);
		assertEquals(order, 300);
		
		String question = questionResponseDB.getQuestion(questionid);
		assertTrue(question.equals("When was The War of 1812?"));
		
		String correctAnswer = questionResponseDB.getCorrectAnswer(questionid);
		assertTrue(correctAnswer.equals("1812"));
	}
}
