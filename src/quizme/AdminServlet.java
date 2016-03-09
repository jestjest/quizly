package quizme;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import quizme.database.*;
import quizme.links.WebsiteStats;

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
		UsersTable usersTable = (UsersTable) request.getServletContext().getAttribute("usersTable");
		int numOfUsers = usersTable.numOfUsers();
		
		FriendTable friendTable = (FriendTable) request.getServletContext().getAttribute("friendTable");
		int numOfFriendRelationships = friendTable.numOfFriendRelationships();
		
		QuizTable quizTable = (QuizTable) request.getServletContext().getAttribute("quizTable");
		int[] numOfQuizzesCreated = quizTable.numOfQuizzesCreated();
		
		QuizResultsTable resultsTable = (QuizResultsTable) request.getServletContext().getAttribute("resultsTable");
		int[] numOfQuizzesTaken = resultsTable.numOfQuizzesTaken();
		
		AchievementsTable achievementsTable = (AchievementsTable) request.getServletContext().getAttribute("achievementsTable");
		int[] numOfAchievements = achievementsTable.numOfAchievements();
		
		MessagesTable messagesTable = (MessagesTable) request.getServletContext().getAttribute("messagesTable");
		int[] numOfMessages = messagesTable.numOfMessages();
		
		WebsiteStats websiteStats = new WebsiteStats(numOfUsers, numOfFriendRelationships, numOfQuizzesCreated, 
													numOfQuizzesTaken, numOfAchievements, numOfMessages);
		request.setAttribute("WebsiteStats", websiteStats);
		request.getRequestDispatcher("admin.jsp").forward(request, response);
	}
}
