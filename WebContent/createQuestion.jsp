<%@page import="com.sun.xml.internal.ws.api.ha.StickyFeature"%>
<%@page import="quizme.QuestionResponse"%>
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
			QuestionResponse Q = (QuestionResponse) request.getSession().getAttribute("Q");
			Q = new QuestionResponse(1);
			request.getSession().setAttribute("Q", Q);
			StringBuilder S = new StringBuilder();
			Q.create(S);
			out.println(S);
		%>
		<input type="hidden" name="next" value="show">
		<button>Done</button>
	</form>
</body>
</html>