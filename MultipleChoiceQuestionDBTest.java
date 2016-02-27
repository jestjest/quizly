package quizme.tests;

import static org.junit.Assert.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import quizme.DBConnection;
import quizme.database.MultipleChoiceQuestionTable;

public class MultipleChoiceQuestionDBTest {
	static DBConnection db;
	static MultipleChoiceQuestionTable multipleChoiceDB;
	
	@BeforeClass
	public static void oneTimeSetUp() {
		db = new DBConnection();
		multipleChoiceDB = new MultipleChoiceQuestionTable(db);
		multipleChoiceDB.removeQuestion(300, 3); /* keep replacing questions in basic test */
		multipleChoiceDB.removeQuestion(300, 50);
		multipleChoiceDB.removeQuestion(27, 3);
		multipleChoiceDB.removeQuestion(300, 1000);
	}
	
	@AfterClass
	public static void oneTimeTearDown() {
		db.closeConnection();
	}
	
	@Test
	public void basictest() {
		int quizid = 300;
		int questionOrder = 3;
		String answerChoicesArray[] = {"Washington", "Bush Sr.", "Clinton", "Bush Jr.", "Obama"};
		List<String> answerChoicesList = Arrays.asList(answerChoicesArray);
		multipleChoiceDB.addQuestion(quizid, questionOrder, "Who was the first US president?", answerChoicesList, 1);
		
		int quizidFromDB = multipleChoiceDB.getQuizID(quizid, questionOrder);
		assertEquals(quizidFromDB, 300);
		
		int questionOrderFromDB = multipleChoiceDB.getQuestionOrder(quizid, questionOrder);
		assertEquals(questionOrderFromDB, 3);
		
		String question = multipleChoiceDB.getQuestion(quizid, questionOrder);
		assertTrue(question.equals("Who was the first US president?"));
		
		List<String> answerChoices = multipleChoiceDB.getAnswerChoices(quizid, questionOrder);
		for (int i = 0; i < answerChoices.size(); i++) {
			String choice = answerChoices.get(i);
			assertTrue(choice.equals(answerChoicesArray[i]));
		}
		
		int correctAnswer = multipleChoiceDB.getCorrectAnswer(quizid, questionOrder);
		assertEquals(correctAnswer, 1);
		
		multipleChoiceDB.setQuestion(quizid, questionOrder, "Who is not running for president right now?");
		question = multipleChoiceDB.getQuestion(quizid, questionOrder);
		assertTrue(question.equals("Who is not running for president right now?"));
		
		String answerChoicesArray2[] = {"Trump", "Clinton", "Sanders", "Cruz", "Mickey Mouse", "Minnie Mouse"};
		List<String> answerChoicesList2 = Arrays.asList(answerChoicesArray2);
		multipleChoiceDB.setAnswerChoices(quizid, questionOrder, answerChoicesList2);
		
		List<String> answerChoices2 = multipleChoiceDB.getAnswerChoices(quizid, questionOrder);
		for (int i = 0; i < answerChoices2.size(); i++) { 
			String choice = answerChoices2.get(i);
			assertTrue(choice.equals(answerChoicesArray2[i]));
		}
		
		multipleChoiceDB.setCorrectAnswer(quizid, questionOrder, 6);
		correctAnswer = multipleChoiceDB.getCorrectAnswer(quizid, questionOrder);
		assertEquals(correctAnswer, 6);
		
		String answerChoicesArray3[] = {"Biden", "Gore", "Cheney", "Adams", "Washington"};
		List<String> answerChoicesList3 = Arrays.asList(answerChoicesArray3);
		multipleChoiceDB.addQuestion(quizid, 50, "Who is the current vice president?", answerChoicesList3, 1);
		
		String answerChoicesArray4[] = { "Sparky", "Pluto", "Bo"};
		List<String> answerChoicesList4 = Arrays.asList(answerChoicesArray4);
		multipleChoiceDB.addQuestion(27, questionOrder, "What is the name of the Obama's dog?", answerChoicesList4, 3);
		
		String answerChoicesArray5[] = { "Illinois", "New York", "California", "Hawaii"};
		List<String> answerChoicesList5 = Arrays.asList(answerChoicesArray5);
		multipleChoiceDB.addQuestion(quizid, 1000, "Where did Obama grow up?", answerChoicesList5, 4);
		
		ResultSet rs = multipleChoiceDB.getAllQuizEntries(quizid);
		try {
			int count = 0;
			while(rs.next()) {
				count++;
				assertEquals(rs.getInt("quizid"), quizid);
				assertFalse(rs.getString("correctAnswer").equals(3));
			}
			assertEquals(count, 3);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}