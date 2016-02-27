package quizme.tests;

import static org.junit.Assert.*;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import quizme.DBConnection;
import quizme.database.AnnouncementsTable;

public class AnnouncementsTableTest {
	static DBConnection db;
	static AnnouncementsTable announcementDB;
	
	@BeforeClass
	public static void oneTimeSetUp() {
		db = new DBConnection();
		announcementDB = new AnnouncementsTable(db);
		announcementDB.removeAnnouncement(1);
		announcementDB.removeAnnouncement(2);
		announcementDB.removeAnnouncement(3);
	}
	
	@AfterClass
	public static void oneTimeTearDown() {
		db.closeConnection();
	}
	
	@Test
	public void basictest() {
		Date date1 = new Date(2016, 2, 14);
		Date date2 = new Date(2014, 4, 15);
		Date date3 = new Date(2014, 4, 14);
		String messages[] = {"good monring!", "good afternoon!", "good evening!"};
		announcementDB.addAnnouncement(messages[0], date1);
		announcementDB.addAnnouncement(messages[1], date2);
		announcementDB.addAnnouncement(messages[2], date3);
		
		ResultSet rs = announcementDB.getAllAnnouncements();
		int count = 0;
		try {
			while(rs.next()) {
				assertTrue(rs.getString(2).equals(messages[count]));
				count++;
			}
			assertEquals(count, 3);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
