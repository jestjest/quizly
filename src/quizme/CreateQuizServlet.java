package quizme;

import java.io.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import quizme.database.*;
import quizme.quizzes.Achievement;

/**
 * Servlet implementation class CreateQuizServlet
 */
@WebServlet("/CreateQuizServlet")
public class CreateQuizServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreateQuizServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String xmlStr = request.getParameter("xml");
		String username = request.getParameter("user");
		
		try {
			Document quiz = loadXMLFromString(xmlStr);
			NodeList questions = quiz.getElementsByTagName("question");
			ServletContext context = request.getServletContext();

			QuizTable quizTable = (QuizTable) context.getAttribute("quizTable");
			int quizID = addQuiz(quiz, questions.getLength(), username, quizTable);
			
			addQuestions(quizID, questions, context);

			//CHECK ACHIEVEMENTS
			AchievementsTable achievementsTable = (AchievementsTable) context.getAttribute("achievementsTable");
			AchievementGuidelinesTable guidelinesTable = (AchievementGuidelinesTable) context.getAttribute("achievementGuidelinesTable");
			
			List<Achievement.AchievementGuidelinesData> newAchievements = Achievement.checkForCreateAchievements(username, quizTable, achievementsTable, guidelinesTable);	
			request.getSession().setAttribute("newAchievements", newAchievements);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void addQuestions(int quizID, NodeList questions, ServletContext context) {
		QuestionResponseTable qrTable = (QuestionResponseTable) context.getAttribute("qrTable");
		FillInTheBlankTable blankTable = (FillInTheBlankTable) context.getAttribute("blankTable");
		MultipleChoiceQuestionTable mcTable = (MultipleChoiceQuestionTable) context.getAttribute("mcTable");
		PictureResponseQuestionTable pictureTable = (PictureResponseQuestionTable) context.getAttribute("pictureTable");
		
		for (int i = 0; i < questions.getLength(); i++) {
			Element question = (Element) questions.item(i);
			String type = question.getAttribute("type");
			
			switch (type) {
			case "question-response":
				addQuestionResponse(quizID, i, question, qrTable);
				break;
				
			case "fill-in-blank":
				addFillInBlank(quizID, i, question, blankTable);
				break;
				
			case "multiple-choice":
				addMultipleChoice(quizID, i, question, mcTable);
				break;
				
			case "picture-response":
				addPictureResponse(quizID, i, question, pictureTable);
				break;
			}
				
		}
	}

	private void addPictureResponse(int quizID, int questionOrder, Element questionElem, PictureResponseQuestionTable pictureTable) {
		String imageLocation = questionElem.getElementsByTagName("image-location").item(0).getTextContent();
		NodeList answerList = questionElem.getElementsByTagName("answer");
		
		ArrayList<String> answers = new ArrayList<String>();
		int preferred = populateAnswers(answers, answerList);
		
		pictureTable.addQuestion(quizID, questionOrder, answers, preferred, imageLocation);
	}
	
	private void addMultipleChoice(int quizID, int questionOrder, Element questionElem, MultipleChoiceQuestionTable mcTable) {
		String question = questionElem.getElementsByTagName("query").item(0).getTextContent();
		NodeList choiceList = questionElem.getElementsByTagName("option");
		
		ArrayList<String> choices = new ArrayList<String>();
		int correctAnswer = populateChoices(choices, choiceList);
		
		mcTable.addQuestion(quizID, questionOrder, question, choices, correctAnswer);
	}

	private void addFillInBlank(int quizID, int questionOrder, Element questionElem, FillInTheBlankTable blankTable) {
		String preQuestion = questionElem.getElementsByTagName("pre").item(0).getTextContent();
		String postQuestion = questionElem.getElementsByTagName("post").item(0).getTextContent();
		NodeList answerList = questionElem.getElementsByTagName("answer");

		ArrayList<String> answers = new ArrayList<String>();
		int preferred = populateAnswers(answers, answerList);
		
		blankTable.addQuestion(quizID, questionOrder, preQuestion, postQuestion, answers, preferred);
	}

	private void addQuestionResponse(int quizID, int questionOrder, Element questionElem, QuestionResponseTable qrTable) {
		String question = questionElem.getElementsByTagName("query").item(0).getTextContent();
		NodeList answerList = questionElem.getElementsByTagName("answer");
		
		ArrayList<String> answers = new ArrayList<String>();
		int preferred = populateAnswers(answers, answerList);
		
		qrTable.addQuestion(quizID, questionOrder, question, answers, preferred);
	}

	private int addQuiz(Document quiz, int numQuestions, String creator, QuizTable quizTable) {
		boolean random = Boolean.parseBoolean(quiz.getDocumentElement().getAttribute("random"));
		boolean immediateCorrection = Boolean.parseBoolean(quiz.getDocumentElement().getAttribute("immediate-correction"));
		boolean onePage = Boolean.parseBoolean(quiz.getDocumentElement().getAttribute("one-page"));
		boolean practiceMode = Boolean.parseBoolean(quiz.getDocumentElement().getAttribute("practice-mode"));
		
		// immediate feedback only for multi page
		if (onePage) immediateCorrection = false;
		
		String title = quiz.getElementsByTagName("title").item(0).getTextContent();
		String desc = quiz.getElementsByTagName("description").item(0).getTextContent();

		Timestamp time = new Timestamp(new Date().getTime());
		
		return quizTable.addQuiz(title, desc, numQuestions, creator, time, 
				random, immediateCorrection, onePage, practiceMode, 0);
	}
	
	private int populateAnswers(ArrayList<String> answers, NodeList answerList) {
		int preferred = 0;
		for (int i = 0; i < answerList.getLength(); i++) {
			Element answer = (Element) answerList.item(i);
			answers.add(answer.getTextContent());
			if (answer.hasAttribute("preferred"))
				preferred = i;
		}
		return preferred;
	}
	
	private int populateChoices(ArrayList<String> choices, NodeList choiceList) {
		int correct = 0;
		for (int i = 0; i < choiceList.getLength(); i++) {
			Element choice = (Element) choiceList.item(i);
			choices.add(choice.getTextContent());
			if (choice.hasAttribute("answer"))
				correct = i;
		}
		return correct;
	}
	
	private static Document loadXMLFromString(String xml) throws Exception
	{
	    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder builder = factory.newDocumentBuilder();
	    InputSource is = new InputSource(new StringReader(xml));
	    return builder.parse(is);
	}
}
