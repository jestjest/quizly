//
// Functions for rendering the question form.
//

/** 
 * Renders the question form for a new question given the type of the question.
 * Depending on which type of question was selected, displays the appropriate question form.
 * 
 * Currently supports:
 * 1. question response
 * 2. fill in the blank
 * 3. multiple choice
 * 4. picture response
 */
function renderQuestionForm(type) {
	if (type === "question-response") {
		return tag("form", { id: type, class: "question-form", role: "form"}, 
				[tag("h3", {}, "Complete the form for a new question:"),
				
				 questionTag()].concat(answerAndSubmitTags())
				 );
		
	} else if (type === "fill-in-blank") {
		return tag("form", 
				{ id: type, class: "question-form", role: "form"},
				[tag("h3", {}, "Complete the form for a new question:"),
				 tag("p", {}, "Denote the blank as a single underscore _"),
				 questionTag()].concat(answerAndSubmitTags())
				 );
		
	} else if (type === "multiple-choice") {
		return tag("form", 
				{ id: type, class: "question-form", role: "form"},
				multipleChoiceContents());
	
	} else if (type === "picture-response") {
		return tag("form", 
				{ id: type, class: "question-form", role: "form"},
				 [tag("h3", {}, "Complete the form for a new question:"),
				 imageTag()].concat(answerAndSubmitTags())
				 );
	}
}

/**
 * Returns the contents of a multiple choice question form as a list of elements.
 */
function multipleChoiceContents() {
	return [tag("h3", {}, "Complete the form for a new question:"),
			 questionTag(),
			 tag("br", {}, []),
			 tag("p", {}, "Choices:"), 
			 tag("ul", {id: "possible-choices"}, []),
			 addChoiceTag(), clearChoicesTag(),
			 tag("br", {}, []),tag("br", {}, []),
			 submitTag()
			 ];
}

/**
 * Returns a common portion of a question form indicating the answers to a question, 
 * adding and deleting answers, and the submit button.
 */
function answerAndSubmitTags() {
	return [tag("br", {}, []), 
			tag("p", {}, "Answers (select preferred):"), 
			tag("ul", {id: "possible-answers"}, []),
			addAnswerTag(),
			clearAnswersTag(),
			tag("br", {}, []), 
			tag("br", {}, []), 
			submitTag()];
}

/** Helper function generating an HTML input for a question in the question form. */
function questionTag() {
	return tag("div", {}, 
			[tag("p", {}, "Question:"),
			 tag("input", {
				 type: "text",
				 name: "question",
				 placeholder: "Question",
				 class: "query",
			 }, [])
		 ]);
}

/** Helper function generating an HTML submit button in the question form. */
function submitTag() {
	return tag("input", {
		 type: "submit",
		 class: "btn btn-success"
	 }, []);
}

/** Helper function generating a HTML list item for a choice in the question form. */
function singleChoiceTag() {
	return tag("li", {}, 
			[tag("input", {type: "radio", name: "option"}, []),
			 
			 tag("input", {
				type: "text",
				name: "possible-choice",
				placeholder: "Choice",
			}, [])]
			);
}

/** Helper function generating an HTML button for adding a choice in the question form. */
function addChoiceTag() {
	return tag("input", {
		type: "button",
		class: "btn btn-primary",
		id: "add-choice",
		value: "Add choice",
		style: "margin-right: 5px",
	}, []);
}

/** Helper function generating an HTML button for clearing all choices in the question form. */
function clearChoicesTag() {
	return tag("input", {
		type: "button",
		class: "btn btn-warning",
		id: "clear-choices",
		value: "Clear all choices",
	}, []);
}

/** Helper function generating a HTML input for URL in the question form. */
function imageTag() {
	return tag("div", {}, 
			[tag("input", {
				 type: "text",
				 name: "image-location",
				 placeholder: "Image location",
				 class: "query",
			 }, [])
		 ]);
}

/** Helper function generating an HTML input for an answer in the question form. */
function singleAnswerTag() {
	return tag("li", {}, 
			[tag("input", {type: "radio", name: "option"}, []),
			 
			 tag("input", {
				type: "text",
				name: "possible-answer",
				placeholder: "answer",
			}, [])]
			);
}

/** Helper function generating an HTML button for adding a valid answer in the question form.*/
function addAnswerTag() {
	return tag("input", {
		type: "button",
		class: "btn btn-primary",
		id: "add-answer",
		value: "Add answer",
		style: "margin-right: 5px",
	}, []);
}

/** Helper function generating an HTML button for clearing all choices in the question form. */
function clearAnswersTag() {
	return tag("input", {
		type: "button",
		class: "btn btn-warning",
		id: "clear-answers",
		value: "Clear all answers",
	}, []);
}


//
// Functions for rendering questions onto the left pane.
//

/**
 * Returns an HTMLElement for display a simple question. Identifies the question
 * by having a type and question number as the ID.
 */
function renderSimpleQuestion(type, number, question, $answers, preferredIndex) {
	return tag("div", {
		class: type + " added-question",
		id: number,
	}, [tag("b", {}, "Question " + number + ": "),
	    tag("p", {class: "question"}, question),
	    tag("b", {}, "Answers:"),
	    tag("ul", {id: "answers"}, answersListTags($answers, preferredIndex))
	    ]
	);
}

/** Returns a list of HTML list items, each one representing an answer. */
function answersListTags($answers, preferredIndex) {
	return $answers.map(function(index) {
		if (index === preferredIndex)
			return tag("li", {class: "preferred"}, [tag("answer", {}, this), "  (preferred)"]);
		else
			return tag("li", {}, tag("answer", {}, this));
	}).get();
}

/**
 * Returns an HTMLElement for displaying a multiple choice question. Identifies the question
 * by having a type and question number as the ID.
 */
function renderMCQuestion(type, number, question, $choices, answerIndex) {
	return tag("div", {
		class: type + " added-question",
		id: number,
	}, [tag("b", {}, "Question " + number + ": "),
	    tag("p", {class: "question"}, question),
	    tag("b", {}, "Choices:"),
	    tag("ul", {id: "choices"}, choicesListTags($choices, answerIndex))
	    ]
	);
}

/** Returns a list of HTML list items, each one representing a multiple choice option. */
function choicesListTags($choices, correctIndex) {
	return $choices.map(function(index) {
		if (index === correctIndex)
			return tag("li", {class: "correct"}, [tag("answer", {}, this),"  (answer)"]);
		else
			return tag("li", {}, tag("answer", {}, this));
	}).get();
}


/**
 * Creates and returns an HTMLElement representing a tag of the given name.
 * attrs is an object, where the key-value pairs represent HTML attributes to
 * set on the tag. contents is an array of strings/HTMLElements (or just
 * a single string/HTMLElement) that will be contained within the tag.
 */
function tag(name, attrs, contents) {
  var element = document.createElement(name);
  for (var attr in attrs) {
    element.setAttribute(attr, attrs[attr]);
  }

  // If contents is a single string or HTMLElement, make it an array of one
  // element; this guarantees that contents is an array below.
  if (!(contents instanceof Array)) {
    contents = [contents];
  }

  contents.forEach(function(piece) {
    if (piece instanceof HTMLElement) {
      element.appendChild(piece);
    } else {
      // must create a text node for a raw string
      element.appendChild(document.createTextNode(piece));
    }
  });

  return element;
}