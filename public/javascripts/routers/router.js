define([
	'backbone',
	'views/addCandidateView',
	'views/candidatesView',
	'views/questionsView'
], function(Backbone, AddCandidateView, CandidatesView, QuestionsView) {
	return Backbone.Router.extend({
		routes: {
			'': 'listCandidates',
			'questions': 'listQuestions',
			"*actions": 'noRoute'
		},

		initialize: function() {
		},

		listCandidates: function() {
			var newCandidateForm = new AddCandidateView();
			newCandidateForm.render();
			var candidates = new CandidatesView();
			candidates.render();
		},
		
		listQuestions: function() {
			var questions = new QuestionsView();
			questions.render();
		},
		
		noRoute: function() {
			console.debug('unknown route');
		}
	});
});
