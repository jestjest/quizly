<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<title>QuizMe</title>
	<link rel="stylesheet" href="css/bootstrap.min.css">
	<link rel="stylesheet" href="css/create-quiz.css">
</head>

<body>
	<jsp:include page="header.jsp"/>
	
    <div class="jumbotron">
      <div class="container">
        <h1>Create a quiz!</h1>
        If you leave this page, your quiz will not be created or saved. 
        <br><br>
        <a href="CreateQuizServlet" class="btn btn-success">Submit my quiz</a>
      </div>
    </div>
    
    <div id="wrapper">
		<div id="navigation">
			<div id="interactors">
				<a id="new-question" href="#" class="btn">New question</a>

				<!-- Question type Button -->
				<div class="btn-group">
				  <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
					Pick a question type
				  </button>
				  <ul class="dropdown-menu">
				  	<li><a href="#" id="qr">Question-Response</a></li>
				    <li><a href="#" id="fill">Fill in the blank</a></li>
				    <li><a href="#" id="mc">Multiple choice</a></li>
				    <li><a href="#" id="picture">Picture-Response</a></li>
				  </ul>
				</div>
			</div>
			
			<div id="left-pane"></div>
		</div>    	
		
		<div id="right-pane"></div>
    </div>
    
    <script type="text/javascript" src="js/templates.js"></script>
    <script type="text/javascript" src="js/script.js"></script>
</body>

</html>