<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="quizme.User" %>
<% User user = (User) request.getSession().getAttribute("user"); %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<title>QuizMe</title>
	<link rel="stylesheet" href="css/bootstrap.min.css">
</head>

<body>

	<nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
      <div class="container">
        <div class="navbar-header">
          <a class="navbar-brand" href="#">QuizMe</a>
        </div>
        <div id="navbar" class="navbar-collapse collapse">
        	<div class="navbar-form navbar-right">
				Hi <%= user.getName() %>!
	        	<a href="create-quiz.jsp" class="btn btn-primary">Create a Quiz</a>
	        	<a href="messages.jsp" class="btn btn-primary">See Messages</a>
	        	<a href="LogoutServlet" class="btn btn-danger">Logout</a>
        	</div>
        </div><!--/.navbar-collapse -->
      </div>
    </nav>

    <!-- Main jumbotron for a primary marketing message or call to action -->
    <div class="jumbotron">
      <div class="container">
        <h1>Announcements</h1>
        
        Testing announcements
        
      </div>
    </div>
    
    <hr>

	<div class="container">
		<h2>Popular quizzes</h2>
			<div class="row">
				Testing popular quizzes
		    </div>
		</div>
	</div> 
	
    <hr>
	
	<div class="container">
		<h2>Recently created quizzes</h2>
			<div class="row">
				Testing recently created quizzes
		    </div>
		</div>
	</div> 
	
    <hr>
	
	<div class="container">
		<h2>Your quiz creation activity</h2>
			<div class="row">
				Testing creation activity
		    </div>
		</div>
	</div> 
	
    <hr>
	
	<div class="container">
		<h2>Achievements</h2>
			<div class="row">
				Testing achievements
		    </div>
		</div>
	</div> 
	
    <hr>
	
	<div class="container">
		<h2>Unread Messages</h2>
			<div class="row">
				Testing messages
		    </div>
		</div>
	</div> 
	
    <hr>
	
	<div class="container">
		<h2>Friends' Recent Activity</h2>
			<div class="row">
				Testing activity
		    </div>
		</div>
	</div> 
	
	<hr>
	
	<script src="js/vendor/jquery-1.11.2.min.js"></script>
	<script src="js/vendor/bootstrap.min.js"></script>        
        
</body>
</html>