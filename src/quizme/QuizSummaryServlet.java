package quizme;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import quizme.database.QuizTable;
import quizme.links.QuizSummaryInfo;

/**
 * Servlet implementation class QuizSummaryServlet
 */
@WebServlet("/QuizSummaryServlet")
public class QuizSummaryServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final long recentDuration = 30 * 60 * 1000; // recent mean last 30mins
	private static final long dayDuration = 24 * 60 * 60 * 1000; // last 24 hours
	private static final int resultNumLimit = 5;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public QuizSummaryServlet() {
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
	 * Assume quizID is sent through request as an Integer field "quizID"
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// get the user
		User user = (User) request.getSession().getAttribute("user");
		
		// get the quiz ID from request
		int quizID = (int) request.getAttribute("quizID");
		
		// Get quiz summary info
		QuizTable quizTable = (QuizTable) request.getSession().getAttribute("quizTable");
		QuizSummaryInfo quizSummaryInfo = quizTable.getQuizSummaryInfo(quizID);
		request.setAttribute("quizSummaryInfo", quizSummaryInfo);

		// determine the time after which is considered "recent" or "next day"
		Calendar calendar = Calendar.getInstance();		
		Timestamp recentTime = new Timestamp( calendar.getTime().getTime() + recentDuration );
		Timestamp lastDayTime = new Timestamp( calendar.getTime().getTime() + dayDuration );
		
	}

}
