package quizme.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import quizme.DBConnection;
import quizme.links.AnnouncementLink;

public class AnnouncementsTable {
	private DBConnection db;
	
	public AnnouncementsTable(DBConnection db) {
		this.db = db;
		createAnnouncementTable();
	}
	
	private void createAnnouncementTable() {
		try {
			PreparedStatement pstmt = db.getPreparedStatement("CREATE TABLE IF NOT EXISTS "
					+ "announcements (announcementid INT AUTO_INCREMENT primary key NOT NULL, message TEXT, "
					+ "subject VARCHAR(128), date DATETIME)");
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public int addAnnouncement(String message, String subject, Timestamp date) {
		try {
			PreparedStatement pstmt1 = db.getPreparedStatement("INSERT INTO announcements (message, subject, date) VALUES (?, ?, ?)");
			pstmt1.setString(1, message);
			pstmt1.setString(2, subject);
			pstmt1.setTimestamp(3, date);
			pstmt1.executeUpdate();
			
			PreparedStatement pstmt2 = db.getPreparedStatement("SELECT announcementid FROM announcements ORDER BY announcementid ASC");
			ResultSet rs = pstmt2.executeQuery();
			rs.last();
			return rs.getInt(1);
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
                AnnouncementLink announcementLink = new AnnouncementLink( rs.getString("subject"),
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
