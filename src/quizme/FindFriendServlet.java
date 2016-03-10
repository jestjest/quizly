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
		doPost(request, response);
	}

	/**
	 * We assume the user of this page is passed a username serach term (which can be a full or partial 
	 * username).
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String searchTerm = (String) request.getAttribute("searchTerm");
		User user = (User) request.getSession().getAttribute("user");
		String username = user.getName();
		UsersTable userTable = (UsersTable) request.getServletContext().getAttribute("usersTable");
		FriendTable friendTable = (FriendTable) request.getServletContext().getAttribute("friendTable");
		
		List<String> matches = userTable.getUsernameMatches(searchTerm);

		List<String> friendMatches = new ArrayList<String>();
		List<String> nonFriendMatches = new ArrayList<String>();
		for (String match : matches) {
			if (friendTable.areFriends(username, match)) {
				friendMatches.add(match);
			} else {
				nonFriendMatches.add(match);
			} 
		}
		
		request.setAttribute("friendMatches", friendMatches);
		request.setAttribute("nonFriendMatches", nonFriendMatches);
		request.getRequestDispatcher("friend-search.jsp").forward(request, response); /* confirm JSP name */
	}
}