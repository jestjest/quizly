<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<title>QuizMe</title>
	<link rel="stylesheet" href="css/bootstrap.min.css">
	<style>
		.alert:empty {
	   		display: none;
		}	
	</style>
</head>

<body>
	
	<nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
      <div class="container">	
        <div class="navbar-header">
          <a class="navbar-brand" href="index.jsp">QuizMe</a>
        </div>
        <div id="navbar" class="navbar-collapse collapse">
        	<div class="navbar-form navbar-right">
        	</div>
        </div><!--/.navbar-collapse -->
      </div>
    </nav>
    
	<div class="jumbotron">
		<div class="container">
			<h2>Welcome to QuizMe! Please log-in or create an account.</h2>
			<form role="form" action="MainLoginServlet" method="POST">
			    <div class="form-group">
			        <label for="username">Username</label> 
			        <input class="form-control" name="username" type="text">
			    </div>
			
			    <div class="form-group">
			        <label for="password">Password</label> 
			        <input class="form-control" name="password" type="password">
			    </div>
			
			    <div class="form-group">
			        <input type="submit" name="login" value="Log in" class="btn btn-default">
			        <input type="submit" name="create" value="Create Account" class="btn btn-primary">
			    </div>
			    
		    	<% 
		    	if (request.getAttribute("error") != null) {
		    		out.println("<div class=\"alert alert-danger\">");
		    		out.println(request.getAttribute("error"));
		    		out.println("</div>");
		    		request.setAttribute("error", "");
		    	}
		    	%>
			</form>
	    </div>
    </div>

</body>
</html>