package quizme;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import quizme.quizzes.*;


/**
 * Servlet implementation class UpdateQuizServlet
 */
@WebServlet("/UpdateQuizServlet")
public class UpdateQuizServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UpdateQuizServlet() {
		super();
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
		Integer currentIndex = Integer.getInteger(request.getParameter("questionIndex")); 
		String answer = request.getParameter("response_" + currentIndex);
		
		/* TO DO: add code from Hadi to get the answer */
		
		Quiz quiz = (Quiz) request.getSession().getAttribute("quiz");
		Question question = quiz.getQuestion(currentIndex - 1); 
		/* adjust from 1-based indexing for questions in the jsp and
		   zero-based indexing for questions in the quiz object */
		question.setReponse(answer);
		
		QuizSummaryInfo quizSummaryInfo = (QuizSummaryInfo) request.getSession().getAttribute("quizSummaryInfo");
		bool immediateFeedback = quizSummaryInfo.immediateCorrection();
		if (currentIndex < quiz.numOfQuestions()) {
			if (immediateFeedback) {
				request.getRequestDispatcher("question-feedback.jsp").forward(request, response);
			} else {
				Integer nextIndex = currentIndex + 1;
				request.getRequestDispatcher("take-quiz-multi.jsp?questionIndex=" + nextIndex).forward(request, response);
			}
		} else {
			if (immediateFeedback) {
				request.getRequestDispatcher("question-feedback.jsp").forward(request, response);
			} else {
				request.getRequestDispatcher("QuizResultsServlet").forward(request, response);
			}	
		}
	}
}
