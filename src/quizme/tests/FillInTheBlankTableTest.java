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
import quizme.database.FillInTheBlankTable;
import quizme.database.PictureResponseQuestionTable;

public class FillInTheBlankTableTest {
	static DBConnection db;
	static FillInTheBlankTable fillInTheBlankDB;
	
	@BeforeClass
	public static void oneTimeSetUp() {
		db = new DBConnection();
		fillInTheBlankDB = new FillInTheBlankTable(db);
		fillInTheBlankDB.removeQuestion(35, 200);
		fillInTheBlankDB.removeQuestion(500, 5000);
	}
	
	@AfterClass
	public static void oneTimeTearDown() {
		db.closeConnection();
	}
	
	@Test
	public void basictest() {
		int quizid = 35;
		int questionOrder = 200;
		List<String> answers = new LinkedList<String>();
		answers.add("blue");
		answers.add("turquoise");
		fillInTheBlankDB.addQuestion(quizid, questionOrder, "The sky is a beautiful shade of", "on a calm day.",  answers, 1);
		
		int quizidFromDB = fillInTheBlankDB.getQuizID(quizid, questionOrder);
		assertEquals(quizidFromDB, 35);
		
		int questionOrderFromDB = fillInTheBlankDB.getQuestionOrder(quizid, questionOrder);
		assertEquals(questionOrderFromDB, 200);
		
		String preQuestion = fillInTheBlankDB.getPreQuestion(quizid, questionOrder);
		assertTrue(preQuestion.equals("The sky is a beautiful shade of"));
		
		String postQuestion = fillInTheBlankDB.getPostQuestion(quizid, questionOrder);
		assertTrue(postQuestion.equals("on a calm day."));
		
		List<String> correctAnswers = fillInTheBlankDB.getCorrectAnswers(quizid, questionOrder);
		assertTrue(correctAnswers.get(0).equals("blue"));
		assertTrue(correctAnswers.get(1).equals("turquoise"));
		
		int preferredAnswer = fillInTheBlankDB.getPreferredAnswer(quizid, questionOrder);
		assertEquals(preferredAnswer, 1);
		
		fillInTheBlankDB.setPreQuestion(quizid, questionOrder, "A special sunset called a green splash transforms the sky to be");
		preQuestion = fillInTheBlankDB.getPreQuestion(quizid, questionOrder);
		assertTrue(preQuestion.equals("A special sunset called a green splash transforms the sky to be"));
		
		fillInTheBlankDB.setPostQuestion(quizid, questionOrder, ".");
		postQuestion = fillInTheBlankDB.getPostQuestion(quizid, questionOrder);
		assertTrue(postQuestion.equals("."));
		
		List<String> answers2 = new LinkedList<String>();
		answers2.add("green");
		answers2.add("bright green");
		answers2.add("very very green");
		fillInTheBlankDB.setCorrectAnswers(quizid, questionOrder, answers2);
		correctAnswers = fillInTheBlankDB.getCorrectAnswers(quizid, questionOrder);
		assertTrue(correctAnswers.get(1).equals("bright green"));
		assertTrue(correctAnswers.get(2).equals("very very green"));
		
		fillInTheBlankDB.setPreferredAnswer(quizid, questionOrder, 0);
		preferredAnswer = fillInTheBlankDB.getPreferredAnswer(quizid, questionOrder);
		assertEquals(preferredAnswer, 0); 
		
		fillInTheBlankDB.setQuestionOrder(quizid, questionOrder, 35);
		questionOrder = 35;
		questionOrderFromDB = fillInTheBlankDB.getQuestionOrder(quizid, questionOrder);
		assertEquals(questionOrderFromDB, 35);
		
		List<String> answers3 = new LinkedList<String>();
		answers3.add("Dipper");
		answers3.add("Big Dipper");
		answers3.add("Biggest Dipper!");
		fillInTheBlankDB.addQuestion(500, 5000, "The constellation", "contains the north star.", answers3, 2);
		
		ResultSet rs = fillInTheBlankDB.getAllQuizEntries(500);
		int count = 0;
		try {
			while(rs.next()) {
				assertEquals(rs.getInt("quizid"), 500);
				count++;
			}
			assertEquals(count, 1);
		} catch (SQLException e) {
			e.printStackTrace();
		}		
	}

}
