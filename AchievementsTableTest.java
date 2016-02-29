package quizme.tests;

import static org.junit.Assert.*;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import quizme.DBConnection;
import quizme.database.AchievementsTable;

public class AchievementsTableTest {
	static DBConnection db;
	static AchievementsTable achievementsDB;
	
	@BeforeClass
	public static void oneTimeSetUp() {
		db = new DBConnection();
		achievementsDB = new AchievementsTable(db);
		achievementsDB.clearAllAchievements();
	}
	
	@AfterClass
	public static void oneTimeTearDown() {
		db.closeConnection();
	}
	
	@Test
	public void basictest() {
		Timestamp date1 = new Timestamp(2016 - 1900, 2, 14, 5, 59, 0, 0);
		Timestamp date2 = new Timestamp(2014 - 1900, 4, 15, 7, 59, 0, 0);
		Timestamp date3 = new Timestamp(2014 - 1900, 4, 14, 11, 59, 0, 0);
		Timestamp date4 = new Timestamp(1990 - 1900, 2, 27, 15, 59, 0, 0);
		String achievements[] = {"Amateur Author", "Prolific Author", "Prodigious Author", "Quiz Machine", "I am the Greatest", "Most Active User"};
		String usernames[] = {"LB", "Jestin", "Hadi"};
		for (int i = 0; i < achievements.length; i++) {
			achievementsDB.addAchievement(usernames[i % 3], achievements[i], date1);
		}
		achievementsDB.addAchievement(usernames[1], "Fastest Time", date2);
		achievementsDB.addAchievement(usernames[1], "Biggest Brain", date3);
		achievementsDB.addAchievement(usernames[1], "Best CS Quiz Taker", date2);
		achievementsDB.addAchievement(usernames[1], "Top CS Scorer", date4);
		
		ResultSet rs = achievementsDB.getAllUserAchievements("LB");
		int count = 0;
		try {
			while(rs.next()) {
				assertTrue(rs.getString(2).equals(achievements[0]) || rs.getString(2).equals(achievements[3]));
				count++;
			}
			assertEquals(count, 2);
		
			assertTrue(achievementsDB.hasAchievement("Hadi", "Prodigious Author"));
			assertTrue(achievementsDB.hasAchievement("Hadi", "Most Active User"));
		
			rs = achievementsDB.getRecentUserAchievements("Jestin", 5);
			count = 0;
			while(rs.next()) {
				System.out.println(rs.getTimestamp("date"));
				assertFalse(rs.getString(2).equals("Top CS Scorer"));
				count++;
			}
			assertEquals(count, 5);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
