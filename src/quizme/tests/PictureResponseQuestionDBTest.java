package quizme.tests;

import static org.junit.Assert.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import quizme.DBConnection;
import quizme.database.PictureResponseQuestionTable;

public class PictureResponseQuestionDBTest {
	static DBConnection db;
	static PictureResponseQuestionTable pictureResponseDB;
	
	@BeforeClass
	public static void oneTimeSetUp() {
		db = new DBConnection();
		pictureResponseDB = new PictureResponseQuestionTable(db);
		pictureResponseDB.removeQuestion(7, 35);
		pictureResponseDB.removeQuestion(7, 500);
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
		answers.add("1812!");
		answers.add("19th Century");
		pictureResponseDB.addQuestion(quizid, questionOrder, "When did The War of 1812 begin?", answers, 1, "www.google.com/warphoto.jpeg");
		
		int quizidFromDB = pictureResponseDB.getQuizID(quizid, questionOrder);
		assertEquals(quizidFromDB, 7);
		
		int questionOrderFromDB = pictureResponseDB.getQuestionOrder(quizid, questionOrder);
		assertEquals(questionOrderFromDB, 300);
		
		String question = pictureResponseDB.getQuestion(quizid, questionOrder);
		assertTrue(question.equals("When did The War of 1812 begin?"));
		
		List<String> correctAnswers = pictureResponseDB.getCorrectAnswers(quizid, questionOrder);
		assertTrue(correctAnswers.get(0).equals("1812!"));
		assertTrue(correctAnswers.get(1).equals("19th Century"));
		
		int preferredAnswer = pictureResponseDB.getPreferredAnswer(quizid, questionOrder);
		assertEquals(preferredAnswer, 1);
		
		pictureResponseDB.setQuestion(quizid, questionOrder, "When was the Civil War?");
		question = pictureResponseDB.getQuestion(quizid, questionOrder);
		assertTrue(question.equals("When was the Civil War?"));
		
		List<String> answers2 = new LinkedList<String>();
		answers2.add("19th Century");
		answers2.add("1861-1865");
		answers2.add("Before the new millenium!");
		pictureResponseDB.setCorrectAnswers(quizid, questionOrder, answers2);
		correctAnswers = pictureResponseDB.getCorrectAnswers(quizid, questionOrder);
		assertTrue(correctAnswers.get(1).equals("1861-1865"));
		assertTrue(correctAnswers.get(2).equals("Before the new millenium!"));
		
		pictureResponseDB.setPreferredAnswer(quizid, questionOrder, 0);
		preferredAnswer = pictureResponseDB.getPreferredAnswer(quizid, questionOrder);
		assertEquals(preferredAnswer, 0); 
		
		pictureResponseDB.setQuestionOrder(quizid, questionOrder, 35);
		questionOrder = 35;
		questionOrderFromDB = pictureResponseDB.getQuestionOrder(quizid, questionOrder);
		assertEquals(questionOrderFromDB, 35);
		
		List<String> answers3 = new LinkedList<String>();
		answers3.add("1775");
		pictureResponseDB.addQuestion(quizid, 500, "When was the American Revolutionary War?", answers3, 0, "www.google.com/warphoto2.jpeg");
		
		String picURL = pictureResponseDB.getPictureURL(quizid, 500);
		assertTrue(picURL.equals("www.google.com/warphoto2.jpeg"));
		pictureResponseDB.setPictureURL(quizid, 500, "www.google.com/lincolnpic.jpeg");
		picURL = pictureResponseDB.getPictureURL(quizid, 500);
		assertTrue(picURL.equals("www.google.com/lincolnpic.jpeg"));
		
		List<String> answers4 = new LinkedList<String>();
		answers4.add("No");
		answers4.add("I don't know");
		answers4.add("Yes!");
		pictureResponseDB.addQuestion(500, 500, "Is 2016 a leap year?", answers4, 2, "www.google.com/calendarphoto.jpeg");
		
		ResultSet rs = pictureResponseDB.getAllQuizEntries(quizid);
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
