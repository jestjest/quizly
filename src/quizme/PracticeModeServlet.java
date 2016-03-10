package quizme;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import quizme.database.*;
import quizme.links.*;
import quizme.quizzes.*;

/**
 * Servlet implementation class PracticeModeServlet
 */
@WebServlet("/PracticeModeServlet")
public class PracticeModeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public PracticeModeServlet() {
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
		Quiz quiz = (Quiz) request.getSession().getAttribute("quiz");
		QuizSummaryInfo quizSummaryInfo = (QuizSummaryInfo) request.getSession().getAttribute("quizSummaryInfo");
		int[] correctAnswerCounts = (int[]) request.getSession().getAttribute("correctAnswerCounts");
		
		updateCorrectAnswerCounts(quiz, correctAnswerCounts);	
		
		if (quiz.numOfQuestionsRemaining() == 0) { /* done with practice mode */
			User user = (User) request.getSession().getAttribute("user");
			AchievementsTable achievementsTable = (AchievementsTable) request.getServletContext().getAttribute("achievementsTable");
			AchievementGuidelinesTable guidelinesTable = (AchievementGuidelinesTable) request.getServletContext().getAttribute("achievementGuidelinesTable");
			Achievement.checkForPracticeModeAchievement(user.getName(), achievementsTable, guidelinesTable);
			request.getRequestDispatcher("HomePageServlet").forward(request, response);
		} else { /* take the quiz again */
			quiz.randomizeQuestionOrder();
			if (quizSummaryInfo.onePage()) {
				request.getRequestDispatcher("take-single-page-quiz.jsp").forward(request, response); 
			} else {
				request.getRequestDispatcher("take-multi-page-quiz.jsp?questionIndex=1").forward(request, response); 
			}
		}
	}
	
	private static final int NUM_OF_CORRECT_ANSWERS_REQUIRED = 3;
	/* once a question is answered correctly the required number of times, the question is removed from the quiz */
	private void updateCorrectAnswerCounts(Quiz quiz, int[] correctAnswerCounts) {
		int numOfQuestionsRemaining = quiz.numOfQuestionsRemaining();
		for (int i = numOfQuestionsRemaining - 1; i >= 0; i++) {
			Question q = quiz.getQuestion(i);
			if (q.maxPoints() == q.points()) {
				/* must win all points for the answer 
				to be considered correct */
				correctAnswerCounts[q.order]++;
				if(correctAnswerCounts[q.order] == NUM_OF_CORRECT_ANSWERS_REQUIRED) {
					quiz.removeQuestion(q); 
				}
			}
		} 
	}
}
