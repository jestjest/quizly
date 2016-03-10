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

import quizme.database.QuizResultsTable;
import quizme.database.QuizTable;
import quizme.links.QuizLink;
import quizme.links.SummaryStat;

/**
 * Servlet implementation class HistoryServlet
 */
@WebServlet("/HistoryServlet")
public class HistoryServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private static final Timestamp fromTime = new Timestamp(1970, 1, 1, 0, 0, 0, 0);
	private static final int limit = 10000; 
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public HistoryServlet() {
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
		User user = (User) request.getSession().getAttribute("user");
		
		QuizTable quizTable = (QuizTable) getServletContext().getAttribute("quizTable");
		
		List<QuizLink> myAllQuizzesCreated  = quizTable.getRecentQuizzesCreated(
				user.getName(), limit, fromTime);
		// LIST OF ALL CREATED QUIZZES
		request.setAttribute("myAllQuizzesCreated", myAllQuizzesCreated ); 
		
		QuizResultsTable quizResultTable = (QuizResultsTable) 
				getServletContext().getAttribute("quizResultTable");
		List<QuizLink> myAllQuizzesTaken  = quizResultTable.getRecentQuizzesTaken(
				user.getName(), limit, fromTime);
		// LIST OF ALL TAKEN QUIZZED
		request.setAttribute("myAllQuizzesTaken", myAllQuizzesTaken ); 
		
		SummaryStat myAllSummaryStat = quizResultTable.getUserSummaryStat(user.getName());
		// A SUMMARY STAT OBJECT OF ALL TIME
		request.setAttribute("myAllSummaryStat", myAllSummaryStat ); 


		RequestDispatcher dispatch = request.getRequestDispatcher("history.jsp");
		dispatch.forward(request, response);
	
	}

}
