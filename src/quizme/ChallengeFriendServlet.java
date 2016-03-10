package quizme;

import java.io.IOException;
import java.sql.Timestamp;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import quizme.database.FriendTable;
import quizme.database.MessagesTable;
import quizme.links.QuizSummaryInfo;

/**
 * Servlet implementation class ChallengeFriendServlet
 */
@WebServlet("/ChallengeFriendServlet")
public class ChallengeFriendServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ChallengeFriendServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User user = (User) request.getSession().getAttribute("user");
		QuizSummaryInfo quizSummary = (QuizSummaryInfo) request.getSession().getAttribute("quizSummaryInfo");
		String targetFriend = request.getParameter("friend");
		MessagesTable messagesTable = (MessagesTable) request.getServletContext().getAttribute("messagesTable");
		FriendTable friendTable = (FriendTable) request.getServletContext().getAttribute("friendTable");
		
		// if not friends or user does not exist
		if (!(friendTable.areFriends(user.getName(), targetFriend))) {
			displayError("You cannot challenge '" + targetFriend + "'. Check if you are friends with the user or if the user exists.", request, response);
			return;
		}
		
		String quizLink = "<a href='QuizSummaryServlet?quizID=" + quizSummary.getQuizID() + "' class='btn'>" + quizSummary.getName() + "</a>";
		messagesTable.removeChallenge(targetFriend, user.getName(), quizLink);
		messagesTable.addMessage(targetFriend, user.getName(), new Timestamp(System.currentTimeMillis()), 
				Float.toString(quizSummary.mySummaryStat().maxScore), quizLink, MessagesTable.CHALLENGE);
		request.getRequestDispatcher("quiz-summary.jsp").forward(request, response);
	}


	/**
	 * Helper method which displays any error found in the error div on quiz-summary.jsp
	 */
	private void displayError(String error, HttpServletRequest request, 
			HttpServletResponse response) throws ServletException, IOException {
		request.setAttribute("error", error);
		request.getRequestDispatcher("quiz-summary.jsp").forward(request, response);
	}
	
	
}
