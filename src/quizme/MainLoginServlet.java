package quizme;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
		
		if (request.getParameter("login") != null) {
			String hashed = generateHash(password);
			handleLogin(username, hashed, request, response);
		} else if (request.getParameter("create") != null ){
			handleAccountCreation(username, request, response);
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
		
		DBConnection con = (DBConnection) request.getServletContext().getAttribute("connection");

		try {
			PreparedStatement stmt = con.getPreparedStatement("SELECT * FROM users WHERE username=? AND password=?");
			stmt.setString(1, username);
			stmt.setString(2, hashed);
			ResultSet rs = stmt.executeQuery();
			
			if (!rs.next()) {
				displayError("Username/password combination was wrong.", request, response);
				return;
			}
			
			forwardLoginSuccess(rs.getString("username"), request, response);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Handles account creation. Checks if the account already exists. If it does, then tells the user to try again.
	 * Otherwise, adds the account to the database and redirects the user to the home page.
	 */
	private void handleAccountCreation(String username, HttpServletRequest request, 
			HttpServletResponse response) throws ServletException, IOException {
		
		DBConnection con = (DBConnection) request.getServletContext().getAttribute("connection");
		
		try {
			PreparedStatement stmt = con.getPreparedStatement("SELECT * FROM users WHERE username=?");
			stmt.setString(1, username);
			ResultSet rs = stmt.executeQuery();
			
			if (rs.next()) {
				displayError("Username already exists.", request, response);
				return;
			}
			
			createAccount(con, username, request, response);
		} catch (SQLException e) {
			displayError("Something went wrong.", request, response);
			e.printStackTrace();
		}
	}
	
	/**
	 * Creates an account in the database and redirects the user to the home page if the insertion
	 * was successful.
	 */
	private void createAccount(DBConnection con, String username, HttpServletRequest request, 
			HttpServletResponse response) throws ServletException, IOException {
		
		PreparedStatement stmt = con.getPreparedStatement("INSERT INTO users VALUES (?)");
		try {
			stmt.setString(1, username);
			int rows = stmt.executeUpdate();
			if (rows != 1) {
				displayError("Something went wrong adding account.", request, response);
				return;
			} 
			
			forwardLoginSuccess(username, request, response);
			
		} catch (SQLException e) {
			displayError("Something went wrong.", request, response);
			e.printStackTrace();
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
		request.getRequestDispatcher("home.jsp").forward(request, response);
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
