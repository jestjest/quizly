package quizme;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import quizme.database.*;
import quizme.links.*;

/**
 * Servlet implementation class UserPageServlet
 */
@WebServlet("/UserPageServlet")
public class UserPageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final int MAX_RESULT_NUM = Integer.MAX_VALUE;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UserPageServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * We assume the user of this page is passed by the request as a String field "username".
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// get the user (who is visiting)
		User user = (User) request.getSession().getAttribute("user");

		// get the user (who is visited)
		String pageUsername = (String) request.getParameter("username");

		// created quizzes by this user
		QuizTable quizTable = (QuizTable) getServletContext().getAttribute("quizTable");
		List<QuizLink> pageUserQuizzesCreated  = quizTable.getRecentQuizzesCreated(
				pageUsername, MAX_RESULT_NUM, new Timestamp(0));
		request.setAttribute("pageUserQuizzesCreated", pageUserQuizzesCreated ); 

		// taken quizzes by this user
		QuizResultsTable quizResultTable = (QuizResultsTable) 
				getServletContext().getAttribute("quizResultTable");
		List<QuizLink> pageUserQuizzesTaken  = quizResultTable.getRecentQuizzesTaken(
				pageUsername, MAX_RESULT_NUM, new Timestamp(0));
		request.setAttribute("pageUserQuizzesTaken", pageUserQuizzesTaken );

		// achievements
		AchievementsTable achievementsTable = (AchievementsTable) 
				getServletContext().getAttribute("achievementsTable");
		List<AchievementLink> pageUserAchievements = achievementsTable.getAllUserAchievementsLinkList( 
				pageUsername );
		request.setAttribute("pageUserAchievements", pageUserAchievements );

		// The status of add friend button
		/**
		 * -1: same person
		 * 0: Send friend request
		 * 1: Friend request pending
		 * 2: Confirm friend request
		 * 3: You are friends
		 */
		int friendStatus;
		FriendTable friendTable = (FriendTable) 
				getServletContext().getAttribute("friendTable");
		FriendRequestTable requestTable = (FriendRequestTable)
				getServletContext().getAttribute("requestTable");

		if (user.getName().equals(pageUsername)){
			friendStatus = -1;
		} else if ( friendTable.areFriends( user.getName(), pageUsername ) ) { // they are friends
			friendStatus = 3;
		}
		else {
			if ( requestTable.pendingFriendRequest(pageUsername, user.getName()) ) 
			{ // pageUser want to be friend with user
				friendStatus = 2;
			}
			else if ( requestTable.pendingFriendRequest( user.getName(), pageUsername ) ) 
			{ // user has already requested to be friend with pageUser (pending)
				friendStatus = 1;
			} else { // no request has been sent, and they are not friends
				friendStatus = 0;
			}
		}
		request.setAttribute("friendStatus", friendStatus );
		request.getRequestDispatcher("user-page.jsp").forward(request, response);
	}
}