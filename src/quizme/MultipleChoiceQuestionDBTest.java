package quizme;

import static org.junit.Assert.*;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class MultipleChoiceQuestionDBTest {
	static DBConnection db;
	static MultipleChoiceQuestionDB multipleChoiceDB;
	
	@BeforeClass
	public static void oneTimeSetUp() {
		db = new DBConnection();
		multipleChoiceDB = new MultipleChoiceQuestionDB(db);
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
		String options[] = {"Washington", "Bush Sr.", "Clinton", "Bush Jr.", "Obama"};
		multipleChoiceDB.addQuestion(quizid, questionOrder, "Who was the first US president?", options, "A");
		
		int quizidFromDB = multipleChoiceDB.getQuizID(quizid, questionOrder);
		assertEquals(quizidFromDB, 300);
		
		int questionOrderFromDB = multipleChoiceDB.getQuestionOrder(quizid, questionOrder);
		assertEquals(questionOrderFromDB, 3);
		
		String question = multipleChoiceDB.getQuestion(quizid, questionOrder);
		assertTrue(question.equals("Who was the first US president?"));
		
		char letters[] = {'A', 'B', 'C', 'D', 'E'};
		for (int i = 0; i < 5; i++) {
			String option = multipleChoiceDB.getOption(quizid, questionOrder, letters[i]);
			assertTrue(option.equals(options[i]));
		}
		
		String correctAnswer = multipleChoiceDB.getCorrectAnswer(quizid, questionOrder);
		assertTrue(correctAnswer.equals("A"));
		
		multipleChoiceDB.setQuestion(quizid, questionOrder, "Who is not running for president right now?");
		question = multipleChoiceDB.getQuestion(quizid, questionOrder);
		assertTrue(question.equals("Who is not running for president right now?"));
		
		String newOptions[] = {"Trump", "Clinton", "Sanders", "Cruz", "Mickey Mouse"};
		for (int i = 0; i < 5; i++) 
			multipleChoiceDB.setOption(quizid, questionOrder, letters[i], newOptions[i]);
		
		for (int i = 0; i < 5; i++) { 
			String option = multipleChoiceDB.getOption(quizid, questionOrder, letters[i]);
			assertTrue(option.equals(newOptions[i]));
		}
		
		multipleChoiceDB.setCorrectAnswer(quizid, questionOrder, "E");
		correctAnswer = multipleChoiceDB.getCorrectAnswer(quizid, questionOrder);
		assertTrue(correctAnswer.equals("E"));
		
		String options2[] = {"Biden", "Gore", "Cheney", "Adams", "Washington"};
		multipleChoiceDB.addQuestion(quizid, 50, "Who is the current vice president?", options2, "A");
		
		String options3[] = { "Sparky", "Pluto", "Bo", "Skipper", "Roger"};
		multipleChoiceDB.addQuestion(27, questionOrder, "What is the name of the Obama's dog?", options3, "C");
		
		String options4[] = { "Illinois", "New York", "California", "Hawaii", "Texas"};
		multipleChoiceDB.addQuestion(quizid, 1000, "Where did Obama grow up?", options4, "D");
		
		ResultSet rs = multipleChoiceDB.getAllQuizEntries(quizid);
		try {
			int count = 0;
			while(rs.next()) {
				count++;
				assertEquals(rs.getInt("quizid"), quizid);
				assertFalse(rs.getString("correctAnswer").equals("C"));
			}
			assertEquals(count, 3);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}