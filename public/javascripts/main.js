require.config({
	paths: {
		app: 'app',
		jquery: '../bower_components/jquery/dist/jquery',
		underscore: '../bower_components/underscore/underscore',
		backbone: '../bower_components/backbone/backbone',
		text: '../bower_components/requirejs-text/text'
	},

	shim: {
		'backbone': {
			deps: ['underscore', 'jquery'],
			exports: 'Backbone'
		},
		'jquery': {
			exports: '$'
		},
		'underscore': {
			exports: '_'
		}
	}
})

require(['backbone', 'routers/router'], function(Backbone, Router) {
	new Router;
	Backbone.history.start();
});
