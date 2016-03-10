<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<title>QuizMe Quiz Creation</title>
	<link rel="stylesheet" href="css/bootstrap.min.css">
	<link rel="stylesheet" href="css/create-quiz.css">
</head>

<body>
	<jsp:include page="header.jsp"/>
	
    <div class="jumbotron vertical-center">
      <div class="container text-center">
        <h1>Create a quiz!</h1>
        If you leave this page, your quiz will not be created or saved. 
        <br><br>
        
        <form class="quiz-submission" role="form">
        	<div class="form-group">
        		<label for="name">Quiz name</label> 
        		<input class="form-control" type="text" name="title" placeholder="Title">
	        </div>
		    
	        <div class="form-group">
			        <label for="description">Description</label> 
			        <input class="form-control" name="description" type="text" placeholder="Description">
		    </div>
		    
		    <div class="form-group">
			      <div class="checkbox"><input type="checkbox" name="random" value="random">Random order<br></div>
			      <div class="checkbox"><input type="checkbox" name="one-page" value="one-age">One page<br></div>
			      <div class="checkbox"><input type="checkbox" name="immediate-correction" value="immediate-correction">Immediate correction (only for multi-page)<br></div>
			      <div class="checkbox"><input type="checkbox" name="practice-mode" value="practice-mode">Practice mode<br></div>
	        </div>
	        
        	<input type="submit" name="submit" value="Create quiz">
        </form>
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
				  	<li><a href="#" id="question-response">Question-Response</a></li>
				    <li><a href="#" id="fill-in-blank">Fill in the blank</a></li>
				    <li><a href="#" id="multiple-choice">Multiple choice</a></li>
				    <li><a href="#" id="picture-response">Picture-Response</a></li>
				  </ul>
				</div>
			</div>
			
			<div id="left-pane"></div>
		</div>    	
		
		<div id="right-pane"></div>
    </div>
    
    <script type="text/javascript" src="js/templates.js"></script>
    <script type="text/javascript" src="js/xml-builder.js"></script>
    <script type="text/javascript" src="js/create-quiz.js"></script>
</body>

</html>