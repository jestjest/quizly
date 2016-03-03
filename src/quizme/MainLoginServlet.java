package src.quizme;

import java.io.*;
import java.security.*;
import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;

import src.quizme.database.UsersTable;

/**
 * Servlet implementation class MainLoginServlet
 */
@WebServlet("/MainLoginServlet")
public class MainLoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MainLoginServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("index.jsp").forward(request, response);
	}

	/**
	 * Handles POST requests for logins and account creations. If the user clicked the login button, handles a login.
	 * If the user clicked the create account button, handles account creation. If the user didn't not click any button,
	 * simply redirects the user back to the page.
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		
		if (username.isEmpty() || password.isEmpty()) {
			displayError("You must provide both a username and password.", request, response);
			return;
		}
		
		String hashed = generateHash(password);
		if (request.getParameter("login") != null) {
			handleLogin(username, hashed, request, response);
		} else if (request.getParameter("create") != null ){
			handleAccountCreation(username, hashed, request, response);
		} else {
			doGet(request, response);
		}
	}
	
	/**
	 * Handles user login. If the account does not exist yet, returns an error on the page. Otherwise,
	 * redirects the user to the home page and logins the user in.
	 */
	private void handleLogin(String username, String hashed, 
			HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		UsersTable usersTable = (UsersTable) request.getServletContext().getAttribute("usersTable");
		if (usersTable.correctPassword(username, hashed)) {
			forwardLoginSuccess(username, request, response);
		} else {
			displayError("Username/password combination was wrong.", request, response);
		}
	}

	/**
	 * Handles account creation. Checks if the account already exists. If it does, then tells the user to try again.
	 * Otherwise, adds the account to the database and redirects the user to the home page.
	 */
	private void handleAccountCreation(String username, String hashed, HttpServletRequest request, 
			HttpServletResponse response) throws ServletException, IOException {
		
		UsersTable usersTable = (UsersTable) request.getServletContext().getAttribute("usersTable");
		if (usersTable.usernameAlreadyExists(username)) {
			displayError("Username already exists.", request, response);
		} else {
			createAccount(usersTable, username, hashed, request, response);
		}
	}
	
	/**
	 * Creates an account in the database and redirects the user to the home page if the insertion
	 * was successful.
	 */
	private void createAccount(UsersTable usersTable, String username, String hashed, HttpServletRequest request, 
			HttpServletResponse response) throws ServletException, IOException {
		boolean success = usersTable.addUser(username, hashed);
		if (success) {
			forwardLoginSuccess(username, request, response);
		} else {
			displayError("Something went wrong adding account.", request, response);
		}
	}
	
	/**
	 * Creates a new User object to be attached to a specific session. Forwards the request to the home
	 * page after a successful login.
	 */
	private void forwardLoginSuccess(String username, 
			HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		User user = new User(username);
		request.getSession().setAttribute("user",  user);
		request.getRequestDispatcher("HomePaggeServlet").forward(request, response);
	}
	
	/**
	 * Helper method which displays any error found in the error div on index.jsp. 
	 */
	private void displayError(String error, HttpServletRequest request, 
			HttpServletResponse response) throws ServletException, IOException {
		request.setAttribute("error", error);
		request.getRequestDispatcher("index.jsp").forward(request, response);
	}
	
	/**
	 Given a byte[] array, produces a hex String,
	 such as "234a6f". with 2 chars for each byte in the array.
	 (provided code)
	*/
	private static String hexToString(byte[] bytes) {
		StringBuffer buff = new StringBuffer();
		for (int i=0; i<bytes.length; i++) {
			int val = bytes[i];
			val = val & 0xff;  // remove higher bits, sign
			if (val<16) buff.append('0'); // leading 0
			buff.append(Integer.toString(val, 16));
		}
		return buff.toString();
	}
	
	/**
	 * Returns the hashed string of a given password.
	 */
	private static String generateHash(String password) {
		try {	
			MessageDigest md = MessageDigest.getInstance("SHA");
			md.update(password.getBytes());
			byte[] mdbytes = md.digest();
			return hexToString(mdbytes);
		} catch (NoSuchAlgorithmException e) {
			System.out.println("No such algorithm.");
			return "";
		}
	}

}
