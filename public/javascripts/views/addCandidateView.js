define([
	'backbone',
	'text!templates/addCandidateTemplate.html'
], function(Backbone, AddCandidateTemplate) {
	return Backbone.View.extend({
		template: _.template(AddCandidateTemplate),
		el: "#addCandidate",
		initialize: function() {

		},

		render: function() {
			this.$el.html(this.template());
		}
	});
});
