package quizme.tests;

import static org.junit.Assert.*;

import java.sql.*;

import org.junit.*;

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
		Timestamp date1 = new Timestamp(2016 - 1900, 2, 14, 0, 0, 0, 0);
		Timestamp date2 = new Timestamp(2014 - 1900, 4, 15, 0, 0, 0 ,0);
		Timestamp date3 = new Timestamp(2015 - 1900, 4, 14, 0, 0, 0, 0);
		String messages[] = {"good monring!", "good afternoon!", "good evening!"};
		String subjects[] = {"am", "pm", "late"};
		announcementDB.addAnnouncement(messages[0], subjects[0], date1);
		announcementDB.addAnnouncement(messages[1], subjects[1], date2);
		announcementDB.addAnnouncement(messages[2], subjects[2], date3);
		
		ResultSet rs = announcementDB.getAllAnnouncements();
		int count = 0;
		try {
			while(rs.next()) {
				if (count == 0) assertTrue(rs.getString(2).equals(messages[0]));
				if (count == 0) assertTrue(rs.getString(3).equals(subjects[0]));
				if (count == 2) assertTrue(rs.getString(2).equals(messages[1]));
				if (count == 2) assertTrue(rs.getString(3).equals(subjects[1]));
				count++;
			}
			assertEquals(count, 3);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	
		announcementDB.removeAnnouncement(1);
		rs = announcementDB.getAllAnnouncements();
		count = 0;
		try {
			while(rs.next()) {
				count++;
			}
			assertEquals(count, 2);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
