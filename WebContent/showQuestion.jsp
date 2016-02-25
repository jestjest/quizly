<%@page import="quizme.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Create a question</title>
</head>
<body>
	<form action="UpdateQuestions" method="post">
		<%
		StringBuilder S = new StringBuilder();

		QuestionResponse Q1 = new QuestionResponse(1, 1, "What is the largest country in the world?", "China");
		Q1.show(S);
		S.append("<br>");

		FillBlank Q2 = new FillBlank(1, 2, "The ___ is the capital of U.S.", "Washington D.C.");
		Q2.show(S);
		S.append("<br>");

		MultipleChoice Q3 = new MultipleChoice(1, 3, "Which one is correct?", "A is good\nB is good\nC is good", 2);
		Q3.show(S);
		S.append("<br>");

		PictureQuestion Q4 = new PictureQuestion(1, 4, "Where is it?",
				"http://events.stanford.edu/events/252/25201/Memchu_small.jpg", "Church");
		Q4.show(S);
		S.append("<br>");

		out.println(S);
		%>
		<input type="hidden" name="next" value="answer">
		<button>Done</button>
	</form>
</body>
</html>