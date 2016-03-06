package quizme;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import quizme.database.QuizResultsTable;
import quizme.database.QuizTable;
import quizme.links.QuizSummaryInfo;
import quizme.quizzes.Quiz;

/**
 * Servlet implementation class QuizResultsServlet
 */
@WebServlet("/QuizResultsServlet")
public class QuizResultsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public QuizResultsServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost( request, response );
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Get quiz object that is stored on the session.
		Quiz quiz = (Quiz) request.getSession().getAttribute("quiz");
		
		// Stop the timer.
		quiz.endTiming();
		
		/* Should it be here ? */
//		List<String> responses = (List<String>) request.getSession().getAttribute("responses");
//		// corrects questions
//		for ( int i = 0; i < quiz.numOfQuestions(); i++ ) {
//			quiz.getQuestion(i).setReponse(responses.get(i));
//		}
		
		// Compute score;
		quiz.computeScore();
		
		// Update tables
		QuizSummaryInfo quizInfoSummary = (QuizSummaryInfo) request.getSession().getAttribute("quizSummaryInfo");
		int quizID = quizInfoSummary.getQuizID();
		User user = (User) request.getSession().getAttribute("user");
		Calendar calendar = Calendar.getInstance();
		
		// Quiz table
		QuizTable quizTable = (QuizTable) getServletContext().getAttribute("quizTable");
		quizTable.incNumOfTimesTaken(quizID);
		
		// Quiz result table
		QuizResultsTable quizResultTable = (QuizResultsTable) 
				getServletContext().getAttribute("quizResultTable");
		
		quizResultTable.addResult(quizID, user.getName(), 
				quiz.getScore(), quiz.getTime(), 
				new Timestamp( calendar.getTime().getTime() ) );
		
		// Achievement table
		
	}

}
