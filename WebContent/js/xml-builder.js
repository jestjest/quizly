/**
 * Builds an XML document to hold a created quiz. Essentially translates
 * the page form data and question data into a XML document.
 * This document will be stringified and passed to a server, where it will
 * be parsed and inputted into the database.
 */
function buildXMLQuiz($quizSubmission, $questions) {
	var quizProperties = {
		"random": $quizSubmission.find('input[name="random"]').is(':checked'),
		"one-page": $quizSubmission.find('input[name="one-page"]').is(':checked'),
		"immediate-correction": $quizSubmission.find('input[name="immediate-correction"]').is(':checked'),
		"practice-mode": $quizSubmission.find('input[name="practice-mode"]').is(':checked'),
	};
	
	return tag("quiz", quizProperties, 
			[tag("title", {}, $quizSubmission.find('input[name="title"]').val()),
			 tag("description", {}, $quizSubmission.find('input[name="description"]').val()),
			 ].concat($questions.map(function() {
				 // create a list of XML elements to be nested inside the quiz element.
				 var question = this.getElementsByClassName('question')[0].textContent;
				 var answers = this.getElementsByTagName('li');
				 
				 if (this.classList.contains("question-response"))
					 return questionResponseXML(question, answers);
				 
				 else if (this.classList.contains("fill-in-blank"))
					 return fillInBlankXML(question, answers);
					 
				 else if (this.classList.contains("multiple-choice"))
					 return multipleChoiceXML(question, answers);
					 
				 else if (this.classList.contains("picture-response")) {
					 return pictureResponseXML(question, answers);
				 }
			 }).get())
		 );
}

/**
 * Constructs an XML element for a question-response question.
 * question - a question string
 * answers - a HTMLCollection, array of <li> answers
 */
function questionResponseXML(question, answers) {
	return tag("question", {type: "question-response"}, 
			[tag("query", {}, question),
			 constructAnswersXML(answers)]);
}

/**
 * Constructs and returns an XML element for a fill-in-the-blank question.
 * question - a question string
 * answers - a HTMLCollection, array of <li> answers
 */
function fillInBlankXML(question, answers) {
	// split up the question by the blank
	var questionParts = question.split("_");
	
	return tag("question", {type: "fill-in-blank"},
			[tag("blank-query", {}, 
					[tag("pre", {}, questionParts[0]),
					 tag("blank", {}, []),
					 tag("post", {}, questionParts[1])
					 ]),
			 constructAnswersXML(answers)
			 ]);
}

/**
 * Constructs and returns an XML element for a multiple choice question.
 * question - a question string
 * answers - an array of <li> choices
 */
function multipleChoiceXML(question, choices) {
	return tag("question", {type: "multiple-choice"},
			[tag("query", {}, question)]
			.concat($.map(choices, function(choiceObj) {
				
				var choice = choiceObj.getElementsByTagName('answer')[0].textContent;		// actual choice
				var attributes = {};
				if (choiceObj.classList.contains('correct'))
					attributes.answer = "answer";
				
				return tag("option", attributes, choice);
			}))
			);
}

/**
 * Constructs and returns an XML element for a picture response question,
 * which is similar to a simple question-response question.
 * question - a question string
 * answers - an array of <li> answers
 */
function pictureResponseXML(url, answers) {
	return tag("question", {type: "picture-response"},
			[tag("image-location", {}, url),
			 constructAnswersXML(answers)]);
}

/**
 * Constructs and returns an XML element containing all answers. If the array of answers
 * has only one element, returns a simple <answer> tag. Otherwise, returns an <answer-list>
 * tag with multiple answer tags nested inside.
 */
function constructAnswersXML(answers) {
	var answersXML;
	
	if (answers.length > 1) {
		answersXML = tag("answer-list", {}, $.map(answers, function(answerObj) {
			
			var answer = answerObj.getElementsByTagName('answer')[0].textContent;		// actual answer text
			var attributes = {};
			if (answerObj.classList.contains('preferred')) 
				attributes.preferred = "preferred";
				
			return tag("answer", attributes, answer);
		}));
		
	} else {		// single answer tag, no answer list
		answersXML = tag("answer", {}, answers[0].getElementsByTagName('answer')[0].textContent);
	}
	
	return answersXML;
}