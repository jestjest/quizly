package quizme;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.sql.ResultSet;
import java.sql.SQLException;

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
		questionResponseDB.removeQuestion(7, 30); /* keep replacing questions in basic test */
		questionResponseDB.removeQuestion(7, 500);
		questionResponseDB.removeQuestion(500, 500);
	}
	
	@AfterClass
	public static void oneTimeTearDown() {
		db.closeConnection();
	}
	
	@Test
	public void basictest() {
		int quizid = 7;
		int questionOrder = 300;
		questionResponseDB.addQuestion(7, 300, "When was The War of 1812?", "1812");
		
		int quizidFromDB = questionResponseDB.getQuizID(quizid, questionOrder);
		assertEquals(quizidFromDB, 7);
		
		int questionOrderFromDB = questionResponseDB.getQuestionOrder(quizid, questionOrder);
		assertEquals(questionOrderFromDB, 300);
		
		String question = questionResponseDB.getQuestion(quizid, questionOrder);
		assertTrue(question.equals("When was The War of 1812?"));
		
		String correctAnswer = questionResponseDB.getCorrectAnswer(quizid, questionOrder);
		assertTrue(correctAnswer.equals("1812"));
		
		questionResponseDB.setQuestion(quizid, questionOrder, "When was the American Civil War?");
		question = questionResponseDB.getQuestion(quizid, questionOrder);
		assertTrue(question.equals("When was the American Civil War?"));
		
		questionResponseDB.setCorrectAnswer(quizid, questionOrder, "1861");
		correctAnswer = questionResponseDB.getCorrectAnswer(quizid, questionOrder);
		assertTrue(correctAnswer.equals("1861"));
		
		questionResponseDB.setQuestionOrder(quizid, questionOrder, 30);
		questionOrder = 30;
		questionOrderFromDB = questionResponseDB.getQuestionOrder(quizid, questionOrder);
		assertEquals(questionOrderFromDB, 30);
		
		questionResponseDB.addQuestion(quizid, 500, "When was the American Revolutionary War?", "1775");
		questionResponseDB.addQuestion(500, 500, "Is 2016 a leap year?", "Yes!");
		
		ResultSet rs = questionResponseDB.getAllQuizEntries(quizid);
		int count = 0;
		try {
			while(rs.next()) {
				assertEquals(rs.getInt("quizid"), quizid);
				count++;
			}
			assertEquals(count, 2);
		} catch (SQLException e) {
			e.printStackTrace();
		}		
	}
}
