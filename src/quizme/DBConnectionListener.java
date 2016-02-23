package quizme;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Application Lifecycle Listener implementation class DBConnectListener
 *
 */
@WebListener
public class DBConnectionListener implements ServletContextListener {

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
