define([
	'jquery',
	'underscore',
	'backbone',
	'models/candidatesModel',
	'text!templates/candidatesTemplate.html',
], function($, _, Backbone, CandidatesModel, CandidatesTemplate) {
	return Backbone.View.extend({
		el: "#mainTable",
		template: _.template(CandidatesTemplate),
		initialize: function() {
			this.model = new CandidatesModel();
			this.listenTo(this.model, 'sync', this.render);
			this.model.fetch();
		},
		render: function() {
			this.$el.html(this.template({data: this.model.attributes}));
			return this;
		}
	});
});
