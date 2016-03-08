CREATE TABLE IF NOT EXISTS achievementGuidelines (
	type INT, 
	name VARCHAR(128), 
	pictureURL TEXT, 
	minimumValue INT, 
	description TEXT);

INSERT INTO achievementGuidelines VALUES (
	(1, "Amateur Author", "http://www.endlessicons.com/wp-content/uploads/2012/11/pencil-icon-614x460.png", 1, "write his/her first quiz"),
	(1, "Prolific Author", "http://www.endlessicons.com/wp-content/uploads/2012/10/badge-icon-614x460.png", 5, "write 5+ quizzes"),
	(1, "Prodigious Author", "http://www.endlessicons.com/wp-content/uploads/2012/10/award-certificate-icon-614x460.png", 10, "write 10+ quizzes"),
	(2, "Quiz Machine", "http://www.endlessicons.com/wp-content/uploads/2012/11/notebook-icon-614x460.png", 10, "take 10+ quizzes"),
	(2, "The Quizinator", "http://www.endlessicons.com/wp-content/uploads/2012/10/award-icon-614x460.png", 15, "take 15+ quizzes"),
	(2, "Quiz Taker Extraordinaire", "http://www.endlessicons.com/wp-content/uploads/2012/10/wreath-icon-614x460.png", 20, "take 20+ quizes"),
	(3, "I Am The Greatest!", "http://www.endlessicons.com/wp-content/uploads/2012/10/trophy-icon-614x460.png", 0, "earn highest score on a quiz"),
	(4, "Practice Makes Perfect", "http://www.endlessicons.com/wp-content/uploads/2014/05/mortar-board-614x460.png", 0, "take quiz in practice mode")
)

