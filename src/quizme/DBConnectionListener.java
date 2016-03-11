package quizme;

import java.io.File;
import java.net.URL;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import quizme.database.*;

/**
 * Application Lifecycle Listener implementation class DBConnectListener
 *
 */
@WebListener
public class DBConnectionListener implements ServletContextListener {

	// DIRECTORY OF XML QUIZ FILES
	// Remove quizzes from this folder once loaded or else the quiz will be loaded twice.
	private static final String XML_DIR = "../quiz-xml/";

	/**
	 * Default constructor. 
	 */
	public DBConnectionListener() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see ServletContextListener#contextInitialized(ServletContextEvent)
	 * 
	 * Initializes a DBConnection instance to be shared by all servlets.
	 */
	public void contextInitialized(ServletContextEvent arg0)  { 
		DBConnection con = new DBConnection();
		arg0.getServletContext().setAttribute("connection", con);

		QuizTable quizTable = new QuizTable(con);
		arg0.getServletContext().setAttribute("quizTable", quizTable);

		QuestionResponseTable qrTable = new QuestionResponseTable(con);
		arg0.getServletContext().setAttribute("qrTable", qrTable);

		FillInTheBlankTable blankTable = new FillInTheBlankTable(con);
		arg0.getServletContext().setAttribute("blankTable", blankTable);

		MultipleChoiceQuestionTable mcTable = new MultipleChoiceQuestionTable(con);
		arg0.getServletContext().setAttribute("mcTable", mcTable);

		PictureResponseQuestionTable prTable = new PictureResponseQuestionTable(con);
		arg0.getServletContext().setAttribute("pictureTable", prTable);

		QuizResultsTable quizResultTable = new QuizResultsTable(con);
		arg0.getServletContext().setAttribute("quizResultTable", quizResultTable);

		AnnouncementsTable announcementsTable = new AnnouncementsTable(con);
		arg0.getServletContext().setAttribute("announcementsTable", announcementsTable);

		AchievementsTable achievementsTable = new AchievementsTable(con);
		arg0.getServletContext().setAttribute("achievementsTable", achievementsTable);

		AchievementGuidelinesTable achievementGuidelinesTable = new AchievementGuidelinesTable(con);
		arg0.getServletContext().setAttribute("achievementGuidelinesTable", achievementGuidelinesTable);

		MessagesTable messagesTable = new MessagesTable(con);
		arg0.getServletContext().setAttribute("messagesTable", messagesTable);

		FriendTable friendTable = new FriendTable(con);
		arg0.getServletContext().setAttribute("friendTable", friendTable);

		UsersTable usersTable = new UsersTable(con);
		arg0.getServletContext().setAttribute("usersTable", usersTable);
		
		BlackListTable blacklist = new BlackListTable(con);
		arg0.getServletContext().setAttribute("blacklist", blacklist);

		FriendRequestTable requestTable = new FriendRequestTable(con);
		arg0.getServletContext().setAttribute("requestTable", requestTable);
		
		try {
			readFromXML(arg0.getServletContext() );
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Loads all quizzes in XML format in the quiz-xml folder located in the same directory as the quizme folder (src folder)
	 */
	
	private void readFromXML(ServletContext context ) throws Exception {
		URL url = DBConnectionListener.class.getResource(XML_DIR);
		if (url == null) {
		     // error - missing folder
			System.out.println("Missing XML folder.");
		} else {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		    File dir = new File(url.toURI());
		    for (File file : dir.listFiles()) {
		    	if ( file.getName().indexOf(".xml") > -1 ) {
					DocumentBuilder builder = factory.newDocumentBuilder();
					Document quiz = builder.parse(file);
					NodeList questions = quiz.getElementsByTagName("question");
					QuizTable quizTable = (QuizTable) context.getAttribute("quizTable");
					int quizID = CreateQuizServlet.addQuiz(quiz, questions.getLength(), "xml_reader", quizTable);                                                                                   
					CreateQuizServlet.addQuestions(quizID, questions, context);                                                                                                                    
					System.out.println("Added XML quiz: " + file.getName());
				}
		    }
		}
		
	}

	/**
	 * @see ServletContextListener#contextDestroyed(ServletContextEvent)
	 * 
	 * Closes the connection once the context is destroyed.
	 */
	public void contextDestroyed(ServletContextEvent arg0)  { 
		DBConnection con = (DBConnection) arg0.getServletContext().getAttribute("connection");
		con.closeConnection();
	}

}
