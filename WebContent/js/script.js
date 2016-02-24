$(function() {
	$(".dropdown-menu li a").click(function() {
		$(".dropdown-toggle").text($(this).text());
		$(".dropdown-toggle").attr('id', $(this).attr('id'));
	});
});

var $rightPane = $('#right-pane');
var $leftPane = $('#left-pane');
var $newQuestionBtn = $("#new-question");
var numQuestions = 0;

$rightPane.html(tag("h2", {}, "Create a question!"));

$newQuestionBtn.click(function (event) {
	event.preventDefault();
	$rightPane.html("");
	$rightPane.append(renderQuestionForm($(".dropdown-toggle").attr('id')));
});

function addNewQuestionListener() {
	$(".question-form").submit(function (event) {
		event.preventDefault();
		
	});
}