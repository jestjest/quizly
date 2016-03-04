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
	private static final long recentDuration = 30 * 60 * 1000; // recent mean last 30mins
	private static final int resultNumLimit = 5;	

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
		String pageUsername = (String) request.getAttribute("username");

		// determine the time after which is considered "recent"
		Calendar calendar = Calendar.getInstance();		
		Timestamp recentTime = new Timestamp( calendar.getTime().getTime() + recentDuration );

		// recently created quizzes by this user
		QuizTable quizTable = (QuizTable) getServletContext().getAttribute("quizTable");
		List<QuizLink> pageUserRecentQuizzesCreated  = quizTable.getRecentQuizzesCreated(
				pageUsername, resultNumLimit, recentTime);
		request.setAttribute("pageUserRecentQuizzesCreated ", pageUserRecentQuizzesCreated ); 

		// recently taken quizzes by this user
		QuizResultsTable quizResultTable = (QuizResultsTable) 
				getServletContext().getAttribute("quizResultTable");
		List<QuizLink> pageUserRecentQuizzesTaken  = quizResultTable.getRecentQuizzesTaken(
				pageUsername, resultNumLimit, recentTime);
		request.setAttribute("pageUserRecentQuizzesTaken ", pageUserRecentQuizzesTaken );

		// achievements
		AchievementsTable achievementsTable = (AchievementsTable) 
				getServletContext().getAttribute("achievementsTable");
		List<AchievementLink> pageUserAchievements = achievementsTable.getAllUserAchievementsLinkList( 
				pageUsername );
		request.setAttribute("pageUserAchievements ", pageUserAchievements );

		// The status of add friend button
		/**
		 * 0: Send friend request
		 * 1: Friend request pending
		 * 2: Confirm friend request
		 * 3: You are friends
		 */
		int friendStatus;
		FriendTable friendTable = (FriendTable) 
				getServletContext().getAttribute("friendTable");
		FriendRequestTable friendRequestTable = (FriendRequestTable)
				getServletContext().getAttribute("friendRequestTable");

		if ( friendTable.areFriends( user.getName(), pageUsername ) ) { // they are friends
			friendStatus = 3;
		}
		else {
			if ( friendRequestTable.pendingFriendRequest(pageUsername, user.getName()) ) 
			{ // pageUser want to be friend with user
				friendStatus = 2;
			}
			else if ( friendRequestTable.pendingFriendRequest( user.getName(), pageUsername ) ) 
			{ // user has already requested to be friend with pageUser (pending)
				friendStatus = 1;
			}
			else { // no request has been sent, and they are not friends
				friendStatus = 0;
			}
		}
		request.setAttribute("friendStatus ", friendStatus );

	}

}
