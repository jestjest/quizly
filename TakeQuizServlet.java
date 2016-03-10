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
import java.sql.*;

/**
 * Servlet implementation class TakeQuizServlet
 */
@WebServlet("/TakeQuizServlet")
public class TakeQuizServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public TakeQuizServlet() {
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
		QuizSummaryInfo quizSummaryInfo = (QuizSummaryInfo) request.getSession().getAttribute("quizSummaryInfo");
		int quizid = quizSummaryInfo.getQuizID();
		int numOfQuestions = quizSummaryInfo.numOfQuestions();
		boolean randomOrder = quizSummaryInfo.randomOrder();
		boolean practiceMode = (request.getAttribute("practice-mode") != null);
		
		Quiz quiz = new Quiz(numOfQuestions);
		addQuestionResponseQuestions(request, quizid, quiz);
		addFillBlankQuestions(request, quizid, quiz);
		addMultipleChoiceQuestions(request, quizid, quiz);
		addPictureResponseQuestions(request, quizid, quiz);
		addMultipleAnswersQuestions(request, quizid, quiz);
		addTrueFalseQuestions(request, quizid, quiz);
			
		if (randomOrder) quiz.randomizeQuestionOrder();
		if (practiceMode) { 
		/* set up an array to keep track of the number of times each question is answered correctly */
			int[] correctAnswerCounts = new int[numOfQuestions];
			request.getSession().setAttribute("correctAnswerCounts", correctAnswerCounts);
		}
		quiz.beginTiming();
		request.getSession().setAttribute("quiz", quiz);
		request.getSession().setAttribute("practiceMode", practiceMode);
		if (quizSummaryInfo.onePage()) {
			request.getRequestDispatcher("take-single-page-quiz.jsp").forward(request, response); 
		} else {
			request.getRequestDispatcher("take-multi-page-quiz.jsp?questionIndex=1").forward(request, response); 
		}
	}

	private void addQuestionResponseQuestions(HttpServletRequest request, int quizid, Quiz quiz) {
		QuestionResponseTable qrTable = (QuestionResponseTable) request.getServletContext().getAttribute("qrTable");
		ResultSet rs = qrTable.getAllQuizEntries(quizid);
		try {
			while(rs.next()) {
				QuestionResponse qr = new QuestionResponse(rs);
				quiz.setQuestion(qr.order, qr);
			}
		} catch (SQLException e) { e.printStackTrace(); }
	}
	
	private void addFillBlankQuestions(HttpServletRequest request, int quizid, Quiz quiz) {
		FillInTheBlankTable blankTable = (FillInTheBlankTable) request.getServletContext().getAttribute("blankTable");
		ResultSet rs = blankTable.getAllQuizEntries(quizid);
		try {
			while(rs.next()) {
				FillBlank fb = new FillBlank(rs);
				quiz.setQuestion(fb.order, fb);
			}
		} catch (SQLException e) { e.printStackTrace(); }
	}
	
	private void addMultipleChoiceQuestions(HttpServletRequest request, int quizid, Quiz quiz) {
		MultipleChoiceQuestionTable mcTable = (MultipleChoiceQuestionTable) request.getServletContext().getAttribute("mcTable");
		ResultSet rs = mcTable.getAllQuizEntries(quizid);
		try {
			while(rs.next()) {
				MultipleChoice mc = new MultipleChoice(rs);
				quiz.setQuestion(mc.order, mc);
			}
		} catch (SQLException e) { e.printStackTrace(); }
	}

	private void addPictureResponseQuestions(HttpServletRequest request, int quizid, Quiz quiz) {
		PictureResponseQuestionTable prTable = (PictureResponseQuestionTable) request.getServletContext().getAttribute("pictureTable");
		ResultSet rs = prTable.getAllQuizEntries(quizid);
		try {
			while(rs.next()) {
				PictureQuestion pr = new PictureQuestion(rs);
				quiz.setQuestion(pr.order, pr);
			}
		} catch (SQLException e) { e.printStackTrace(); }
	}

	private void addMultipleAnswersQuestions(HttpServletRequest request, int quizid, Quiz quiz) {
		MultipleAnswersQuestionTable multipleAnswersTable = (MultipleAnswersQuestionTable) request.getServletContext().getAttribute("multipleAnswersTable");
		ResultSet rs = multipleAnswersTable.getAllQuizEntries(quizid);
		try {
			while(rs.next()) {
				MultipleAnswers ma = new MultipleAnswers(rs);
				quiz.setQuestion(ma.order, ma);
			}
		} catch (SQLException e) { e.printStackTrace(); }
	}
	
	private void addTrueFalseQuestions(HttpServletRequest request, int quizid, Quiz quiz) {
		TrueFalseQuestionTable trueFalseTable = (TrueFalseQuestionTable) request.getServletContext().getAttribute("trueFalseTable");
		ResultSet rs = trueFalseTable.getAllQuizEntries(quizid);
		try {
			while(rs.next()) {
				TrueFalse tf = new TrueFalse(rs);
				quiz.setQuestion(tf.order, tf);
			}
		} catch (SQLException e) { e.printStackTrace(); }
	}
	
}