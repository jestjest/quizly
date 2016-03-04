<%@ page import="quizme.User" %>
<% User user = (User) request.getSession().getAttribute("user"); %>

<script src="js/vendor/jquery-1.12.1.min.js"></script>
<script src="js/vendor/bootstrap.min.js"></script>  

<nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
	<div class="container">
	    <div class="navbar-header">
			<a class="navbar-brand" href="HomePageServlet">QuizMe</a>
	    </div>
	    <div id="navbar" class="navbar-collapse collapse">
	    	<div class="navbar-form navbar-right">
	    	<input type="hidden" name="username" value=<%=user.getName() %>>
			<a href="UserPageServlet?username=<%=user.getName()%>" class="btn btn-success" style="color: white">Hi <%=user.getName()%>!</a> &nbsp;&nbsp;&nbsp;
		 	<a href="create-quiz.jsp" class="btn btn-primary">Create a Quiz</a>
		 	<a href="MessagesServlet" class="btn btn-primary">See Messages</a>
		 	<a href="LogoutServlet" class="btn btn-danger">Logout</a>
			</div>
		</div><!--/.navbar-collapse -->
	</div>
</nav>