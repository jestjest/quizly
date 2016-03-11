package quizme;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import quizme.database.*;
import java.sql.*;


/**
 * Servlet implementation class ManageAdminServlet
 */
@WebServlet("/ManageAdminServlet")
public class ManageAdminServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ManageAdminServlet() {
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
		if (request.getParameter("add-announcement") != null) { /* other capabilities to incorporate? */
			addAnnouncement(request);
		} else if (request.getParameter("remove-announcement") != null) {
			removeAnnouncements(request);
		} else if (request.getParameter("remove-user") != null) {
			removeUser(request);
		} else if (request.getParameter("add-admin-status") != null) {
			changeAdminStatus(request, true);
		} else if (request.getParameter("remove-admin-status") != null ) {
			changeAdminStatus(request, false);
		} else if (request.getParameter("remove-quiz") != null) {
			removeQuiz(request);
		} else if (request.getParameter("remove-quiz-results") != null) {
			removeQuizResults(request);
		}
		request.getRequestDispatcher("AdminServlet").forward(request, response); 
	}

	private void addAnnouncement(HttpServletRequest request) {
		AnnouncementsTable announcementsTable = (AnnouncementsTable) request.getServletContext().getAttribute("announcementsTable"); 
		String subject = request.getParameter("subject");
		String message = request.getParameter("message");
		Timestamp date = new Timestamp(System.currentTimeMillis());
		announcementsTable.addAnnouncement(message, subject, date);
	}
	
	private void removeAnnouncements(HttpServletRequest request) {
		AnnouncementsTable announcementsTable = (AnnouncementsTable) request.getServletContext().getAttribute("announcementsTable"); 
		String[] idsToRemove = request.getParameterValues("announcement");
		if (idsToRemove == null) return;
		for (int i = 0; i < idsToRemove.length; i++) {
			int id = Integer.parseInt(idsToRemove[i]);
			announcementsTable.removeAnnouncement(id);
		}
	}
	
	private void removeUser(HttpServletRequest request) {
		UsersTable usersTable = (UsersTable) request.getServletContext().getAttribute("usersTable");
		String username = request.getParameter("username");
		usersTable.removeUser(username);

		BlackListTable blacklist = (BlackListTable) request.getServletContext().getAttribute("blacklist");
		blacklist.addUser(username);
	}
	
	private void changeAdminStatus(HttpServletRequest request, boolean admin) {
		UsersTable usersTable = (UsersTable) request.getServletContext().getAttribute("usersTable");
		String username = request.getParameter("username");
		usersTable.setAdmin(username, admin);
	}
	
	private void removeQuiz(HttpServletRequest request) {
		ServletContext context = request.getServletContext();
		QuizTable quizTable = (QuizTable) context.getAttribute("quizTable");
		QuestionResponseTable qrTable = (QuestionResponseTable) context.getAttribute("qrTable");
		FillInTheBlankTable blankTable = (FillInTheBlankTable) context.getAttribute("blankTable");
		MultipleChoiceQuestionTable mcTable = (MultipleChoiceQuestionTable) context.getAttribute("mcTable");
		PictureResponseQuestionTable prTable = (PictureResponseQuestionTable) context.getAttribute("pictureTable");
		
		int quizID= Integer.parseInt(request.getParameter("quizID"));
		quizTable.setNumOfQuestions(quizID, -1);
		
		qrTable.removeQuizQuestions(quizID);
		blankTable.removeQuizQuestions(quizID);
		mcTable.removeQuizQuestions(quizID);
		prTable.removeQuizQuestions(quizID);
	}
	
	private void removeQuizResults(HttpServletRequest request) {
		QuizResultsTable resultsTable = (QuizResultsTable) request.getServletContext().getAttribute("quizResultTable");
		int quizID= Integer.parseInt(request.getParameter("quizID"));
		resultsTable.removeAllQuizResultsById(quizID);
	}
}
