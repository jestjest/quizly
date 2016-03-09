package quizme;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import quizme.links.QuizSummaryInfo;
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
		Quiz quiz = (Quiz) request.getSession().getAttribute("quiz");
		Question question = quiz.getQuestion(currentIndex - 1); 
		/* adjust from 1-based indexing for questions in the jsp and
		   zero-based indexing for questions in the quiz object */
		
		setQuestionAnswer(request, question, currentIndex);
		

		QuizSummaryInfo quizSummaryInfo = (QuizSummaryInfo) request.getSession().getAttribute("quizSummaryInfo");
		boolean immediateFeedback = quizSummaryInfo.immediateCorrection();
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
	
	private void setQuestionAnswer(HttpServletRequest request, Question question, int currentIndex) {
		Iterator<String> it = request.getParameterMap().keySet().iterator();
		ParamKey pk = new ParamKey();
		// list of responses to the current question
		List<String> responses = new ArrayList<String>();
		// list of integers for the order of received responses.
		List<Integer> orders = new ArrayList<Integer>();
		while ( it.hasNext() ) {
			String key = it.next();
			if ( pk.parseKey(key) && pk.questionOrder == currentIndex ) { // i.e., has "response_n_m" format 
				String value = request.getParameterMap().get(key)[0];
				responses.add(value);
				orders.add(pk.contentOrder);
			}
		}

		String[] orderedResponses = new String[ responses.size() ];
		for ( int j = 0; j < responses.size(); j++ ) {
			orderedResponses[ orders.get(j) ] = responses.get(j);
		}
		question.setResponse( concatStrings(orderedResponses) );
	}
	
	private String concatStrings( String[] S ) {
		if ( S.length == 0 ) {
			return "";
		}
		if ( S.length == 1 ) {
			return S[0];
		}
		else {
			StringBuilder out = new StringBuilder("");
			for ( int i = 0 ; i < S.length; i++ ) {
				out.append( S[i] + "~~~ ");
			}
			return out.toString();
		}
	}
}
