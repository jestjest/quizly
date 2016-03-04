<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
       
<%@ page import="java.util.List" %>
<%@ page import="quizme.links.MessageLink" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.sql.Timestamp" %>
<%@ page import="quizme.database.MessagesTable" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<title>QuizMe</title>
	<link rel="stylesheet" href="css/bootstrap.min.css">
</head>
<body>
	<jsp:include page="header.jsp"/>
	
	<div class="jumbotron">
		<div class="container">
			<h2>Messages</h2>
		</div>
	</div>

	<div id="wrapper">
		<div id="messages">
			<%
			List<MessageLink> messages = (List<MessageLink>) request.getAttribute("myMessages");
			if (messages.isEmpty()) {
				out.println("<div class='message-list'>");
				out.println("<p>You have no further messages.</p>");
				out.println("</div>");
			} else {

				out.println("<div>");
				for (MessageLink message : messages) {
					String date = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(message.getDate());
					
					out.println("div class='message-info' id='" + message.getID() + "'>");
					out.println("<p>From: " + message.getSenderLink() + " on " + date + "</p>");
					
					if (message.getType() == MessageLink.MType.TEXT) {
						out.println("<h3>" + message.getSubject() + "</h3>");
						out.println("<p>" + message.getContent() + "</p>");
					
					} else if (message.getType() == MessageLink.MType.FRIENDSHIP) {
						out.println("<h3><b>" + message.getSenderLink() + "</b> wants to be friends with you!</h3>");
						out.println("<form role='form' action='FriendRequestServlet' method='POST'>");
			        	out.println("<div class='form-group'>");
			        	out.println("<input type='hidden' name='target-user' value='" + request.getParameter("username") + "'>");
			        	out.println("<input type='submit' name='accept-request' value='Accept friend' class='btn btn-success'>");
			        	out.println("<input type='submit' name='reject-request' value='Reject friend' class='btn btn-danger'>");
			        	out.println("</div>");
			        	out.println("</form>");
					
					} else if (message.getType() == MessageLink.MType.CHALLENGE) {
						out.println("I challenge you to take this quiz: " + message.getSubject());
						out.println("My score is " + message.getContent() + "%.");
					}
					
					out.println("</div>");
				}
				out.println("</div>");
			}
			%>
		</div>    	
		
		<div id="right-pane"></div>
	</div>
	
    <script type="text/javascript" src="js/templates.js"></script>
</body>
</html>