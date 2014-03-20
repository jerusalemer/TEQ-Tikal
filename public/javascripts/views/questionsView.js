define([
	'backbone',
	'models/questionsModel',
	'text!templates/questionsTemplate.html'
], function(Backbone, QuestionsModel, QuestionsTemplate) {
	return Backbone.View.extend({
		el: "#mainTable",
		template: _.template(QuestionsTemplate),
		initialize: function() {
			this.model = new QuestionsModel();
			this.listenTo(this.model, 'sync', this.render);
			this.model.fetch();
		},

		render: function() {
			console.debug(this.model);
			_.each(this.model.get("expertises"), function(v, k) {
				console.debug(k);
			});
			this.$el.html(this.template(this.model));
			return this;
		}
	});
});










