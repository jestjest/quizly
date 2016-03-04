package quizme;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import quizme.database.MessagesTable;
import quizme.links.MessageLink;

/**
 * Servlet implementation class MessagesServlet
 */
@WebServlet("/MessagesServlet")
public class MessagesServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private static final int MAX_MESSAGES = Integer.MAX_VALUE; // The maximum number of messages to show!
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MessagesServlet() {
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
	 * Assume user is stored in session
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// get the user (who is visiting)
		User user = (User) request.getSession().getAttribute("user");
		
		// get all messages
		MessagesTable messagesTable = (MessagesTable) request.getServletContext().getAttribute("messagesTable");
		List<MessageLink> myMessages = messagesTable.getAllReceivedMessages(user.getName(), MAX_MESSAGES);
		
		for (MessageLink message : myMessages)
			messagesTable.setSeen(message.getID(), true);
		
		request.setAttribute("myMessages", myMessages);
		request.getRequestDispatcher("messages.jsp").forward(request, response);
	}

}
