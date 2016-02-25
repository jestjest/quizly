package quizme;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class PictureResponseQuestionDBTest {
	static DBConnection db;
	static PictureResponseQuestionDB pictureResponseDB;
	
	@BeforeClass
	public static void oneTimeSetUp() {
		db = new DBConnection();
		pictureResponseDB = new PictureResponseQuestionDB(db);
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
