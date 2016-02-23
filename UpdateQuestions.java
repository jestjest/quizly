package project;

import java.io.IOException;
import java.util.Iterator;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class UpdateQuestions
 */
@WebServlet("/UpdateQuestions")
public class UpdateQuestions extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UpdateQuestions() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Iterator<String> it = request.getParameterMap().keySet().iterator();
		String next = "show";
		while ( it.hasNext() ) {
			String param = (String) it.next();
			System.out.println(param);
			System.out.println( "isValid = " + ParamKey.isValid(param));
			if ( ParamKey.isValid(param) ) {
				ParamKey paramKey = new ParamKey( param );
				System.out.println(paramKey.fieldName+" = "+request.getParameter(param) );
				( (QuestionResponse) request.getSession().getAttribute("Q") ).update( paramKey.fieldName, request.getParameter(param) );
			}
			else {
				next = request.getParameter(param);
			}
		}
		RequestDispatcher dispatch;
		switch (next) {
		case "show": 
			dispatch = request.getRequestDispatcher("showQuestion.jsp");
			dispatch.forward(request, response);
			break;
		case "answer":
			dispatch = request.getRequestDispatcher("answerQuestion.jsp");
			dispatch.forward(request, response);
			break;
		case "create":
			dispatch = request.getRequestDispatcher("createQuestion.jsp");
			dispatch.forward(request, response);
			break;
		}

	}

}
