package quizme.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import quizme.DBConnection;
import quizme.database.QuestionResponseTable;

public class QuestionResponseDBTest {
	static DBConnection db;
	static QuestionResponseTable questionResponseDB;
	
	@BeforeClass
	public static void oneTimeSetUp() {
		db = new DBConnection();
		questionResponseDB = new QuestionResponseTable(db);
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
		List<String> answers = new LinkedList<String>();
		answers.add("1812");
		answers.add("19th Century");
		questionResponseDB.addQuestion(7, 300, "When was The War of 1812?", answers, 0);
		
		int quizidFromDB = questionResponseDB.getQuizID(quizid, questionOrder);
		assertEquals(quizidFromDB, 7);
		
		int questionOrderFromDB = questionResponseDB.getQuestionOrder(quizid, questionOrder);
		assertEquals(questionOrderFromDB, 300);
		
		String question = questionResponseDB.getQuestion(quizid, questionOrder);
		assertTrue(question.equals("When was The War of 1812?"));
		
		List<String> correctAnswer = questionResponseDB.getCorrectAnswers(quizid, questionOrder);
		assertTrue(correctAnswer.get(0).equals("1812"));
		assertTrue(correctAnswer.get(1).equals("19th Century"));
		
		int preferredAnswer = questionResponseDB.getPreferredAnswer(quizid, questionOrder);
		assertEquals(preferredAnswer, 0);
		
		questionResponseDB.setQuestion(quizid, questionOrder, "When was the American Civil War?");
		question = questionResponseDB.getQuestion(quizid, questionOrder);
		assertTrue(question.equals("When was the American Civil War?"));
		
		List<String> answers2 = new LinkedList<String>();
		answers2.add("19th Century");
		answers2.add("1861");
		answers2.add("Before the new millenium");
		questionResponseDB.setCorrectAnswers(quizid, questionOrder, answers2);
		correctAnswer = questionResponseDB.getCorrectAnswers(quizid, questionOrder);
		assertTrue(correctAnswer.get(1).equals("1861"));
		assertTrue(correctAnswer.get(2).equals("Before the new millenium"));
		
		questionResponseDB.setPreferredAnswer(quizid, questionOrder, 1);
		preferredAnswer = questionResponseDB.getPreferredAnswer(quizid, questionOrder);
		assertEquals(preferredAnswer, 1); 
		
		questionResponseDB.setQuestionOrder(quizid, questionOrder, 30);
		questionOrder = 30;
		questionOrderFromDB = questionResponseDB.getQuestionOrder(quizid, questionOrder);
		assertEquals(questionOrderFromDB, 30);
		
		List<String> answers3 = new LinkedList<String>();
		answers3.add("1775");
		questionResponseDB.addQuestion(quizid, 500, "When was the American Revolutionary War?", answers3, 0);
		
		List<String> answers4 = new LinkedList<String>();
		answers4.add("Maybe");
		answers4.add("No");
		answers4.add("I don't know");
		answers4.add("Yes!");
		questionResponseDB.addQuestion(500, 500, "Is 2016 a leap year?", answers4, 3);
		
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