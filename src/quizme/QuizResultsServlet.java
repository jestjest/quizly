package quizme;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import quizme.database.*;
import quizme.links.Performance;
import quizme.links.QuizSummaryInfo;
import quizme.quizzes.*;

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
				//System.out.println(key);
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
			for ( int i = 0; i < quiz.numOfQuestionsRemaining(); i++ ) {
				List<String> responses = responseTextMap.get(i + 1);
				List<Integer> orders = responseOrderMap.get(i + 1);
				String[] orderedResponses = new String[ responses.size() ];
				for ( int j = 0; j < responses.size(); j++ ) {
					orderedResponses[ orders.get(j) ] = responses.get(j);
				}
				quiz.getQuestion(i).setResponse( concatStrings(orderedResponses) );
			}
		}
		
		// if in practiceMode forward to PracticeModeServlet
		if ( (boolean) request.getSession().getAttribute("practiceMode")) {
			request.getRequestDispatcher("PracticeModeServlet").forward(request, response);
			return;
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
		AchievementsTable achievementsTable = (AchievementsTable) 
				request.getServletContext().getAttribute("achievementsTable");
		AchievementGuidelinesTable guidelinesTable = (AchievementGuidelinesTable) 
				request.getServletContext().getAttribute("achievementGuidelinesTable");
		List<Achievement.AchievementGuidelinesData> achievements = Achievement.checkForTakeAchievements(
				user.getName(), quizResultTable, achievementsTable, guidelinesTable);
		Achievement.AchievementGuidelinesData highScoreAchievement = Achievement.checkForHighScoreAchievement(
				user.getName(), quiz.getScore(), quizID, quizResultTable, achievementsTable, guidelinesTable);
		if ( highScoreAchievement != null ) { 
			achievements.add(highScoreAchievement);
		}
		request.setAttribute("achievements", achievements);
		
		// recompute quizSUmmaryInfo
 		// determine the time after which is considered "recent" or "next day"
 		Timestamp recentTime = new Timestamp(System.currentTimeMillis() - QuizSummaryServlet.recentDuration );
 		Timestamp lastDayTime = new Timestamp(System.currentTimeMillis() - QuizSummaryServlet.dayDuration );
 
		QuizSummaryInfo newQuizSummary = quizTable.getQuizSummaryInfo(quizID, user.getName(),
 				recentTime, lastDayTime, QuizSummaryServlet.resultNumLimit);
 
 		Collections.sort(newQuizSummary.getMyPerformances(), new Comparator<Performance>(){
 			public int compare(Performance o1, Performance o2){
 				if(o1.dateTaken == o2.dateTaken) {
 					return 0;
 				}
 				return o1.dateTaken.getTime() < o2.dateTaken.getTime() ? -1 : 1;
 			}
 		});
 		Collections.reverse(newQuizSummary.getMyPerformances());
 		request.setAttribute("newQuizSummary", newQuizSummary);
		
		request.getRequestDispatcher("quiz-results.jsp").forward(request, response);
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
