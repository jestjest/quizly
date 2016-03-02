package quizme.database;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.ArrayList;
import quizme.links.AnnouncementLink;

import quizme.DBConnection;

public class AnnouncementsTable {
	private DBConnection db;
	
	public AnnouncementsTable(DBConnection db) {
		this.db = db;
		createAnnouncementTable();
	}
	
	private void createAnnouncementTable() {
		try {
			PreparedStatement pstmt = db.getPreparedStatement("CREATE TABLE IF NOT EXISTS "
					+ "announcements (announcementid INT, message TEXT, date DATE)");
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public int addAnnouncement(String message, Date date) {
		try {
			PreparedStatement pstmt1 = db.getPreparedStatement("SELECT announcementid FROM announcements");
			ResultSet rs = pstmt1.executeQuery();
			rs.last();
			int announcementid = rs.getRow() + 1;
			
			PreparedStatement pstmt2 = db.getPreparedStatement("INSERT INTO announcements VALUES(?, ?, ?)");
			pstmt2.setInt(1, announcementid);
			pstmt2.setString(2, message);
			pstmt2.setDate(3, date);
			pstmt2.executeUpdate();
			return announcementid;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1; /* indicates database error */
	}
	
	public void removeAnnouncement(int announcementid) {
		try {
			PreparedStatement pstmt = db.getPreparedStatement("DELETE FROM announcements WHERE announcementid = ?");
			pstmt.setInt(1, announcementid);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public ResultSet getAllAnnouncements() {
		try {
			PreparedStatement pstmt = db.getPreparedStatement("SELECT * FROM announcements ORDER BY date DESC");
			return pstmt.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/* HomePage related functions */

	/**
	 * Interact with announcement data base an return all available announcements.
	 * @return Map <Date, String> of all available announcements ordered chronologically.
	 */
    public List<AnnouncementLink> getAllAnnouncementsList( ) {
        ResultSet rs = getAllAnnouncements();
        List<AnnouncementLink> announcementLinks = new ArrayList<AnnouncementLink>();
        try {
            while ( rs.next() ) {
                AnnouncementLink announcementLink = new AnnouncementLink( "New announcement",
                        rs.getString("message"), rs.getTimestamp("date") );
                announcementLinks.add( announcementLink );
            }
            return announcementLinks;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
