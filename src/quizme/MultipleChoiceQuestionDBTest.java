package quizme;

import static org.junit.Assert.*;
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
		multipleChoiceDB.removeQuestion("MC0030000003"); /* keep replacing question MC3003 in basic test */
	}
	
	@AfterClass
	public static void oneTimeTearDown() {
		db.closeDatabase();
	}
	
	@Test
	public void basictest() {
		String options[] = {"Washington", "Bush Sr.", "Clinton", "Bush Jr.", "Obama"};
		String questionid = multipleChoiceDB.addQuestion(300, 3, "Who was the first US president?", options, "A");
		assertTrue(questionid.equals("MC0030000003"));
		
		int quizid = multipleChoiceDB.getQuizID(questionid);
		assertEquals(quizid, 300);
		
		int order = multipleChoiceDB.getQuestionOrder(questionid);
		assertEquals(order, 3);
		
		String question = multipleChoiceDB.getQuestion(questionid);
		assertTrue(question.equals("Who was the first US president?"));
		
		char letters[] = {'A', 'B', 'C', 'D', 'E'};
		for (int i = 0; i < 5; i++) {
			String option = multipleChoiceDB.getOption(questionid, letters[i]);
			assertTrue(option.equals(options[i]));
		}
		
		String correctAnswer = multipleChoiceDB.getCorrectAnswer(questionid);
		assertTrue(correctAnswer.equals("A"));
	}

}
