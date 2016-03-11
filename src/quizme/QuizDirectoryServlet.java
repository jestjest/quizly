package quizme;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import quizme.database.QuizTable;
import quizme.links.QuizLink;

/**
 * Servlet implementation class QuizDirectoryServlet
 */
@WebServlet("/QuizDirectoryServlet")
public class QuizDirectoryServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private static final Timestamp fromTime = new Timestamp(0);
	private static final int limit = Integer.MAX_VALUE;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public QuizDirectoryServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		QuizTable quizTable = (QuizTable) getServletContext().getAttribute("quizTable");
		List<QuizLink> allQuizzes = quizTable.getRecentQuizzesCreated(limit, fromTime);
		request.setAttribute("allQuizzes", allQuizzes ); 

		RequestDispatcher dispatch = request.getRequestDispatcher("quiz-directory.jsp");
		dispatch.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
