package quizme;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

		QuizSummaryInfo quizInfoSummary = (QuizSummaryInfo) request.getSession().getAttribute("quizSummaryInfo");

		// assume in request.getParameterMap we have key,value
		// For each question we have (response_order_responseOrder, String );
		if ( quizInfoSummary.onePage() ) { // only need to to this if it is one page.
			Iterator<String> it = request.getParameterMap().keySet().iterator();
			ParamKey pk = new ParamKey();
			// map question order to the list of responses of that question
			Map<Integer, List<String>> responseTextMap = new HashMap<Integer, List<String>>();
			// map question order to the list of integers showing order of received responses.
			Map<Integer, List<Integer>> responseOrderMap = new HashMap<Integer, List<Integer>>();
			while ( it.hasNext() ) {
				String key = it.next();
				if ( pk.parseKey(key) ) { // i.e., has "response_n_m" format 
					String value = request.getParameterMap().get(key)[0];
					if ( responseTextMap.containsKey( pk.questionOrder ) ) {
						responseTextMap.get( pk.questionOrder ).add( value );
						responseOrderMap.get( pk.questionOrder ).add( pk.contentOrder );
					}
					else {
						List<String> responseText = new ArrayList<String>();
						responseText.add(value);
						List<Integer> responseOrder = new ArrayList<Integer>();
						responseOrder.add( pk.contentOrder );
						responseTextMap.put( pk.questionOrder, responseText );
						responseOrderMap.put( pk.questionOrder, responseOrder );
					}
				}
			}
			
			// convert list of Strings to one String for each question and send it to the question
			for ( int i = 0; i < quiz.numOfQuestions(); i++ ) {
				List<String> responses = responseTextMap.get(i);
				List<Integer> orders = responseOrderMap.get(i);
				String[] orderedResponses = new String[ responses.size() ];
				for ( int j = 0; j < responses.size(); j++ ) {
					orderedResponses[ orders.get(j) ] = responses.get(j);
				}
				quiz.getQuestion(i).setResponse( concatStrings(orderedResponses) );
			}
		}

		// Compute score;
		quiz.computeScore();

		// Update tables
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
