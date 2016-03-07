package quizme;

import java.io.IOException;
import java.sql.Timestamp;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import quizme.database.MessagesTable;

/**
 * Servlet implementation class SendMessageServlet
 */
@WebServlet("/SendMessageServlet")
public class SendMessageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SendMessageServlet() {
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
		String targetUser = request.getParameter("target-user");
		String message = request.getParameter("message");
		String subject = request.getParameter("subject");
		MessagesTable messagesTable = (MessagesTable) request.getServletContext().getAttribute("messagesTable");
		messagesTable.addMessage(targetUser, user.getName(), new Timestamp(System.currentTimeMillis()), message, subject, MessagesTable.NOTE);
		messagesTable.addMessage(user.getName(), targetUser, new Timestamp(System.currentTimeMillis()), "", "Auto-generated: I received your message.", MessagesTable.NOTE);
		request.getRequestDispatcher("user-page.jsp?username=" + targetUser);
	}

}
