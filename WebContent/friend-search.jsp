<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ page import='java.util.List' %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<title>QuizMe Quiz Directory</title>
	<link rel="stylesheet" href="css/bootstrap.min.css">
</head>

<body>
	<jsp:include page="header.jsp"/>
	
	<div class="jumbotron">
      <div class="container text-center">
      	<h2>QuizMe Friend Search</h2>
		<form action="FindFriendServlet" method='GET'>
			<input style="width: 300px; text-align: center;" type='text' name='searchTerm' placeholder="Enter in a username query">
			<br><br>
			<input class='btn btn-primary' type='submit' name='search-friends' value='Search for users'>
		</form>
	  </div>
	</div>
	
	<%
	List<String> friendMatches = (List<String>) request.getAttribute("friendMatches");
	List<String> nonFriendMatches = (List<String>) request.getAttribute("nonFriendMatches");
	List<String> sentPendingMatches = (List<String>) request.getAttribute("sentPendingMatches");
	List<String> receivedPendingMatches = (List<String>) request.getAttribute("receivedPendingMatches");
	%>
	
	<div class='container text-center'>
	<h3>Friends</h3>
	<%
	if (friendMatches != null) {
		if (friendMatches.size() == 0) {
			out.println("<p>No friends found.</p>");	
		} else {
			for (String username : friendMatches) {
				out.println("<a class='btn btn-default' href='UserPageServlet?username=" + username + "' class='btn'>" + username + "</a>");
				out.println("<br>");
			}
		}
	}
	%>
	</div>
	<hr>
	
	<div class='container text-center'>
	<h3>Not friends</h3>
	<%
	if (nonFriendMatches != null) {
		if (nonFriendMatches.size() == 0) {
			out.println("<p>No users who are not friends with you were found.</p>");	
		} else {
			for (String username : nonFriendMatches) {
				out.println("<a class='btn btn-default' href='UserPageServlet?username=" + username + "' class='btn'>" + username + "</a>");
				out.println("<br>");
			}
		}
	}
	%>
	</div>
	<hr>
	
	<div class='container text-center'>
	<h3>Users you have sent pending requests to</h3>
	<%
	if (sentPendingMatches != null) {
		if (sentPendingMatches.size() == 0) {
			out.println("<p>No users with outstanding pending friend requests from you found.</p>");	
		} else {
			for (String username : sentPendingMatches) {
				out.println("<a class='btn btn-default' href='UserPageServlet?username=" + username + "' class='btn'>" + username + "</a>");
				out.println("<br>");
			}
		}
	}
	%>
	</div>
	<hr>
	
	<div class='container text-center'>
	<h3>Users you have received pending friend requests from</h3>
	<%
	if (receivedPendingMatches != null) {
		if (receivedPendingMatches.size() == 0) {
			out.println("<p>No users who have sent you outstanding pending friend requests found.</p>");	
		} else {
			for (String username : receivedPendingMatches) {
				out.println("<a class='btn btn-default' href='UserPageServlet?username=" + username + "' class='btn'>" + username + "</a>");
				out.println("<br>");
			}
		}
	}
	%>
	</div>
	<hr>
</body>
</html>