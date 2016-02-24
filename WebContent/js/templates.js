function renderQuestionForm(type) {
	if (type === "qr") {
		return tag("form", { id: "qr", class: "question-form" }, 
				[tag("h3", {}, "Complete the form for a new question:"),
				
				 questionTag(), answerTag(), submitTag()
				 ]);
		
	} else if (type === "fill") {
		return tag("form", { id: "fill", class: "question-form" },
				[tag("h3", {}, "Complete the form for a new question:"),
				 tag("p", {}, "Denote the blank as a single underscore _"),
				 
				 questionTag(), answerTag(), submitTag()
				 ]);
		
	} else if (type === "mc") {
		return tag("form", { id: "mc", class: "question-form"},
				[tag("h3", {}, "Complete the form for a new question:"),
				 
				 questionTag(),
				 tag("br", {}, []),
				 tag("p", {}, "Choices:"), 
				 choiceTag(), addChoiceTag(),
				 tag("br", {}, []),tag("br", {}, []),
				 submitTag()
				 ]);
	
	} else if (type === "picture") {
		return tag("form", { id: "picture", class: "question-form"},
				[tag("h3", {}, "Complete the form for a new question:"),
				 
				 urlTag(),answerTag(), submitTag()
				 ]);
	}
}

function questionTag() {
	return tag("div", {}, 
			[tag("input", {
				 type: "text",
				 name: "question",
				 placeholder: "Question",
			 }, [])
		 ]);
}

function answerTag() {
	return tag("div", {}, 
			[tag("input", {
				 type: "text",
				 name: "answer",
				 placeholder: "Answer",
			 }, [])
		 ]);
}

function submitTag() {
	return tag("input", {
		 type: "submit",
		 class: "btn btn-success"
	 }, []);
}

function choiceTag() {
	return tag("ul", {id: "choices"}, 
			[tag("li", {}, 
					[tag("input", {
						type: "text",
						name: "choice",
						placeholder: "Choice",
					}, [])]
			)]
	);
}

function addChoiceTag() {
	return tag("input", {
		type: "button",
		class: "btn btn-primary",
		id: "add-choice",
		value: "Add choice",
	}, []);
}

function urlTag() {
	return tag("div", {}, 
			[tag("input", {
				 type: "text",
				 name: "question",
				 placeholder: "url",
			 }, [])
		 ]);
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