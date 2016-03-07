package quizme;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.Comparator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import quizme.database.QuizTable;
import quizme.links.QuizSummaryInfo;
import quizme.links.Performance;;

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
	 * Also we assume an integer is sent through a field "order" which shows the order of by
	 * which user performance should be sorted.
	 * 0/null (default) : by date descending
	 * 1 : by date ascending
	 * 2 : by score descending
	 * 3 : by score ascending
	 * 4 : by time descending
	 * 5 : by time ascending
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User user = (User) request.getSession().getAttribute("user");
		int quizID = Integer.parseInt(request.getParameter("quizID"));
		
		String ordering = request.getParameter("order");
		int order = (ordering == null) ? 0 : Integer.parseInt(ordering); // default ordering
		
		// determine the time after which is considered "recent" or "next day"
		Timestamp recentTime = new Timestamp(System.currentTimeMillis() - recentDuration );
		Timestamp lastDayTime = new Timestamp(System.currentTimeMillis() - dayDuration );


		// Get quiz summary info
		QuizTable quizTable = (QuizTable) getServletContext().getAttribute("quizTable");
		QuizSummaryInfo quizSummaryInfo = quizTable.getQuizSummaryInfo(quizID, user.getName(),
				recentTime, lastDayTime, resultNumLimit);

		// sort myPerformances based on the given order
		if ( order == 0 || order == 1) {		// sort by date
			Collections.sort(quizSummaryInfo.getMyPerformances(), new Comparator<Performance>(){
				public int compare(Performance o1, Performance o2){
					if(o1.dateTaken == o2.dateTaken) {
						return 0;
					}
					return o1.dateTaken.getTime() < o2.dateTaken.getTime() ? -1 : 1;
				}
			});
		}

		if ( order == 2 || order == 3 ) {		// sort by score
			Collections.sort(quizSummaryInfo.getMyPerformances(), new Comparator<Performance>(){
				public int compare(Performance o1, Performance o2){
					if(o1.score == o2.score) {
						if(o1.time == o2.time) {
							return 0;
						}
						else {
							return o1.time > o2.time ? -1 : 1;
						}
					}
					return o1.score < o2.score ? -1 : 1;
				}
			});
		}
		
		if ( order == 4 || order == 5 ) {		// sort by time taken
			Collections.sort(quizSummaryInfo.getMyPerformances(), new Comparator<Performance>(){
				public int compare(Performance o1, Performance o2){
					if(o1.time == o2.time) {
						return 0;
					}
					return o1.time < o2.time ? -1 : 1;
				}
			});
		}

		if (order%2 == 0) {		// support increasing/decreasing sorting.
			Collections.reverse(quizSummaryInfo.getMyPerformances());
		}

		request.getSession().setAttribute("quizSummaryInfo", quizSummaryInfo);
		request.getRequestDispatcher("quiz-summary.jsp").forward(request, response);
	}

}
