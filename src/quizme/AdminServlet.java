package quizme;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import quizme.database.*;
import quizme.links.*;

/**
 * Servlet implementation class AdminServlet
 */
@WebServlet("/AdminServlet")
public class AdminServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Timestamp fromTime = new Timestamp(0);
	private static final int limit = Integer.MAX_VALUE;  
	
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

		/* Data to show directly on the admin page */
		AnnouncementsTable announcementsTable = (AnnouncementsTable) request.getServletContext().getAttribute("announcementsTable");
		List<AnnouncementLink> announcements = announcementsTable.getAllAnnouncementsList();
		request.setAttribute("announcements", announcements);
		
		UsersTable usersTable = (UsersTable) request.getServletContext().getAttribute("usersTable");
		List<User> users = usersTable.usersList();
		request.setAttribute("users", users);
		
		QuizTable quizTable = (QuizTable) request.getServletContext().getAttribute("quizTable");
		List<QuizLink> quizzes = quizTable.getRecentQuizzesCreated(limit, fromTime);
		request.setAttribute("quizzes", quizzes);
		
		/* Data for the website statistics */
		int numOfUsers = usersTable.numOfUsers();
		
		FriendTable friendTable = (FriendTable) request.getServletContext().getAttribute("friendTable");
		int numOfFriendRelationships = friendTable.numOfFriendRelationships();
		
		
		int[] numOfQuizzesCreated = quizTable.numOfQuizzesCreated();
		
		QuizResultsTable resultsTable = (QuizResultsTable) request.getServletContext().getAttribute("quizResultTable");
		int[] numOfQuizzesTaken = resultsTable.numOfQuizzesTaken();
		
		AchievementsTable achievementsTable = (AchievementsTable) request.getServletContext().getAttribute("achievementsTable");
		int[] numOfAchievements = achievementsTable.numOfAchievements();
		
		WebsiteStats websiteStats = new WebsiteStats(numOfUsers, numOfFriendRelationships, numOfQuizzesCreated, 
													numOfQuizzesTaken, numOfAchievements);
		request.setAttribute("websiteStats", websiteStats);
		request.getRequestDispatcher("admin.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request,response);
	}
}
