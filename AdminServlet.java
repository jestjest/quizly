package src.quizme;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.sql.*;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;

import src.quizme.database.*;
import src.quizme.quizzes.*;

/**
 * Servlet implementation class AdminServlet
 */
@WebServlet("/AdminServlet")
public class AdminServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AdminServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (request.getParameter("add") != null) { /* other capabilities to incorporate? */
			addAnnouncement(request);
		} else if (request.getParameter("remove") != null) {
			removeAnnouncements(request);
		}
		request.getRequestDispatcher("admin.jsp").forward(request, response); /* confirm jsp name */
	}

	private void addAnnouncement(HttpServletRequest request) {
		AnnouncementsTable announcementsTable = (AnnouncementsTable) request.getServletContext().getAttribute("announcementsTable"); 
		String subject = (String) request.getParameter("subject");
		String message = (String) request.getParameter("message");
		Timestamp date = new Timestamp(System.currentTimeMillis());
		announcementsTable.addAnnouncement(message, subject, date);
	}
	
	private void removeAnnouncements(HttpServletRequest request) {
		AnnouncementsTable announcementsTable = (AnnouncementsTable) request.getServletContext().getAttribute("announcementsTable"); 
		
		ResultSet rs = announcementsTable.getAllAnnouncements();
		List<Integer> idsToRemove = new LinkedList<Integer>();
		try {
			while(rs.next()) {
				Integer id = rs.getInt("announcementid");
				if (request.getParameter(id.toString()) != null) idsToRemove.add(id); /* confirm these parameters are available */
			}
		} catch (SQLException e) { e.printStackTrace(); }
		
		for (Integer id : idsToRemove) {
			announcementsTable.removeAnnouncement(id);
		}
	}
}
