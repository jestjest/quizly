package quizme;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import quizme.database.*;
import quizme.links.*;

/**
 * Servlet implementation class FindFriendServlet
 */
@WebServlet("/FindFriendServlet")
public class FindFriendServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public FindFriendServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String searchTerm = (String) request.getParameter("searchTerm");
		User user = (User) request.getSession().getAttribute("user");
		String username = user.getName();
		UsersTable userTable = (UsersTable) request.getServletContext().getAttribute("usersTable");
		FriendTable friendTable = (FriendTable) request.getServletContext().getAttribute("friendTable");
		FriendRequestTable requestTable = (FriendRequestTable) request.getServletContext().getAttribute("requestTable");
		
		List<String> matches = userTable.getUsernameMatches(searchTerm);
		List<String> friendMatches = new ArrayList<String>();
		List<String> nonFriendMatches = new ArrayList<String>();
		List<String> sentPendingMatches = new ArrayList<String>();
		List<String> receivedPendingMatches = new ArrayList<String>();
		
		for (String match : matches) {
			
			if (username.equals(match)) continue;		// don't display own name
			
			if (friendTable.areFriends(username, match)) {
				friendMatches.add(match);
			} else if (requestTable.pendingFriendRequest(match, username)) {
				receivedPendingMatches.add(match);
			} else if (requestTable.pendingFriendRequest(username, match)) {
				sentPendingMatches.add(match);
			} else {
				nonFriendMatches.add(match);
			}
		}
		
		request.setAttribute("friendMatches", friendMatches);
		request.setAttribute("nonFriendMatches", nonFriendMatches);
		request.setAttribute("sentPendingMatches", sentPendingMatches);
		request.setAttribute("receivedPendingMatches", receivedPendingMatches);
		
		request.getRequestDispatcher("friend-search.jsp").forward(request, response);
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}