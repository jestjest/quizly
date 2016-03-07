package quizme;

import java.io.IOException;
import java.sql.Timestamp;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import quizme.database.*;

/**
 * Servlet implementation class FriendRequestServlet
 */
@WebServlet("/FriendRequestServlet")
public class FriendRequestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FriendRequestServlet() {
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
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String user = ((User) request.getSession().getAttribute("user")).getName();
		String targetUser = request.getParameter("target-user");
		
		FriendRequestTable requestTable = (FriendRequestTable) request.getServletContext().getAttribute("requestTable");
		MessagesTable messagesTable = (MessagesTable) request.getServletContext().getAttribute("messagesTable");
		FriendTable friendTable = (FriendTable) request.getServletContext().getAttribute("friendTable");
		
		if (request.getParameter("send-request") != null) {
			requestTable.addFriendRequest(user, targetUser);
			messagesTable.addMessage(targetUser, user, new Timestamp(System.currentTimeMillis()), "", "Friend request", MessagesTable.REQUEST);

		} else if (request.getParameter("accept-request") != null) {
			requestTable.removeFriendRequest(targetUser, user);
			messagesTable.removeRequestMessage(user, targetUser);
			friendTable.addFriends(user, targetUser);
			messagesTable.addMessage(targetUser, user, new Timestamp(System.currentTimeMillis()), "", "I accepted your friend request.", MessagesTable.NOTE);
			
		} else if (request.getParameter("reject-request") != null) {
			requestTable.removeFriendRequest(targetUser, user);
			messagesTable.removeRequestMessage(user, targetUser);
			
		} else if (request.getParameter("remove-friend") != null) {
			friendTable.removeFriends(user, targetUser);
		}
		
		request.getRequestDispatcher("UserPageServlet?username=" + targetUser).forward(request, response);
	}

}
