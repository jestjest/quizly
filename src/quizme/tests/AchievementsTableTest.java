package quizme.tests;

import static org.junit.Assert.*;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

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
		Date date1 = new Date(2016, 2, 14);
		Date date2 = new Date(2014, 4, 15);
		Date date3 = new Date(2014, 4, 14);
		Date date4 = new Date(1990, 2, 27);
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
				assertFalse(rs.getString(2).equals("Top CS Scorer"));
				count++;
			}
			assertEquals(count, 5);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
