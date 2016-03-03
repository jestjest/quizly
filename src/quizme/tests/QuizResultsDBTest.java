package quizme.tests;

import static org.junit.Assert.*;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import quizme.DBConnection;
import quizme.database.QuizResultsTable;

public class QuizResultsDBTest {
	static DBConnection db;
	static QuizResultsTable resultDB;
	
	@BeforeClass
	public static void oneTimeSetUp() {
		db = new DBConnection();
		resultDB = new QuizResultsTable(db);
		for (int i = 1; i <= 12; i++) 
			resultDB.removeResult(i); /* keep replacing results used in basic test */
	}
	
	@AfterClass
	public static void oneTimeTearDown() {
		db.closeConnection();
	}
	
	@Test
	public void basictest() {
		Timestamp today = new Timestamp(System.currentTimeMillis());
		int resultid = resultDB.addResult(300, "fakeuser", 90.5, 60, today);
		assertEquals(resultid, 1);
		
		int quizid = resultDB.getQuizID(resultid);
		assertEquals(quizid, 300);
		
		String username = resultDB.getUsername(resultid);
		assertTrue(username.equals("fakeuser"));
		
		double score = resultDB.getScore(resultid);
		assertEquals(score, 90.5, 0.0001);
		
		long time = resultDB.getTime(resultid);
		assertEquals(time, 60);
		
		DateFormat df = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
		Timestamp date = resultDB.getDate(resultid);
		String dateString = df.format(date);
		String todayString = df.format(today);
		assertTrue(dateString.equals(todayString));
		
		Timestamp date2 = new Timestamp(2016 - 1900, 2 - 1, 27, 11, 0, 0, 0);
		Timestamp date3 = new Timestamp(2016 - 1900, 1 - 1, 27, 11, 0, 0, 0);
		for (int i = 0; i < 5; i++) {
			resultDB.addResult(1, "LB", i * 10, 60, today);
			resultDB.addResult(2, "JB", i * 20, 900, date2);
		}
		resultDB.addResult(1, "LB", 55, 40, date2);
	}

}