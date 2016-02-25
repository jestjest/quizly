// Handles question-type selection
$(".dropdown-menu li a").click(function(event) {
	event.preventDefault();
	$(".dropdown-toggle").text($(this).text());
	$(".dropdown-toggle").attr('id', $(this).attr('id'));
});

var $rightPane = $('#right-pane');
var $leftPane = $('#left-pane');
var $newQuestionBtn = $("#new-question");
var $quizSubmission = $('.quiz-submission');
var QUESTION_NUM = 1;

$rightPane.html(tag("h2", {}, "Create a question!"));

/**
 * Adds a click listener when the user wants to create a new question, creates
 * the form on the right pane for the user to fill out.
 */
$newQuestionBtn.click(function (event) {
	event.preventDefault();
	$rightPane.html("");
	var type = $(".dropdown-toggle").attr('id');
	$rightPane.append(renderQuestionForm(type));
	if (type === "multiple-choice") {
		addChoicesListener();
	}
	addAnswersListener();
	addNewQuestionListener();
});

/**
 * 
 */
$quizSubmission.submit(function (event) {
	event.preventDefault();
	var root = buildXMLQuiz($quizSubmission, $('.added-question'));
	console.log(new XMLSerializer().serializeToString(root));
//	$.post('CreateQuizServlet', {xml: new XMLSerializer().serializeToString(root)})
//		.done(function() {
//			// redirect to ???
//		});
});

/**
 * Adds an event listener for when the user wants to add a new question. 
 * Displays the question on the left pane and clears the contents from the right pane.
 */
function addNewQuestionListener() {
	$('.question-form').submit(function (event) {
		event.preventDefault();
		var questionElement;
		var type = $('.question-form').attr('id');
		
		switch (type) {
		
		case "question-response":
		case "fill-in-blank":
		case "picture-response":
			var $answers = $('.question-form').find("[name='possible-answer']").map(function() {
				return this.value;
			});
			
			var preferredIndex = getSelectedIndex();
			
			questionElement = renderSimpleQuestion(type, QUESTION_NUM,
					$('.question-form').find("[name='question'], [name='image-location']").val(),
					$answers, preferredIndex);
						
			break;
			
		case "multiple-choice":
			var $choices = $('.question-form').find("[name='possible-choice']").map(function() {
				return this.value;
			});
			
			var correctIndex = getSelectedIndex();
			
			questionElement = renderMCQuestion(type, QUESTION_NUM,
					$('.question-form').find("[name='question']").val(),
					$choices, correctIndex);
			
			break;
		}
			
		$leftPane.append(questionElement);
		QUESTION_NUM++;
		$rightPane.html(tag("h2", {}, "Create a question!"));
	});
}

/** Adds event listeners to add/remove possible answers. */
function addAnswersListener() {
	$("#add-answer").click(function(event) {
		event.preventDefault();
		$("#possible-answers").append(singleAnswerTag());
	});
	
	$("#clear-answers").click(function(event) {
		event.preventDefault();
		$("#possible-answers").html("");
	});
	
}

/** Adds event listeners to add/remove choice buttons. */
function addChoicesListener() {
	$("#add-choice").click(function(event) {
		event.preventDefault();
		$("#possible-choices").append(singleChoiceTag());
	});
	
	$("#clear-choices").click(function(event) {
		event.preventDefault();
		$("#possible-choices").html("");
	});
}

/** Helper function which returns the index of the selected radio button/choice. */
function getSelectedIndex() {
	var selectedAnswer = $('.question-form').find("[type='radio']").filter(function(index, elem) {
		return elem.checked;
	});
	
	return $('.question-form').find("[type='radio']").index(selectedAnswer);
}