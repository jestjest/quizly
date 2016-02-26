package quizme.tests;

import static org.junit.Assert.*;

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
	}
	
	@AfterClass
	public static void oneTimeTearDown() {
		db.closeConnection();
	}
	
	@Test
	public void basictest() {
		//LB to fill in
	}
}
