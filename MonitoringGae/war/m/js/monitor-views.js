window.DashBoardView = Backbone.View.extend({

	template : _.template($('#page-dashboard').html()),

	render : function(eventName) {
		$(this.el).html(this.template());
		return this;
	}
});

window.AboutView = Backbone.View.extend({

	template : _.template($('#page-about').html()),

	render : function(eventName) {
		$(this.el).html(this.template());
		return this;
	}
});

window.HelpView = Backbone.View.extend({

	template : _.template($('#page-help').html()),

	render : function(eventName) {
		$(this.el).html(this.template());
		return this;
	}
});

window.LogoutView = Backbone.View.extend({

	template : _.template($('#page-logout').html()),
	render : function(eventName) {
		$(this.el).html(this.template());
		return this;
	}
});

window.AdministrationView = Backbone.View.extend({

	template : _.template($('#page-administration').html()),

	render : function(eventName) {
		$(this.el).html(this.template());
		return this;
	}
});

window.SystemDetailView = Backbone.View.extend({

	template : _.template($('#page-system-detail').html()),

	render : function(eventName) {
		$(this.el).html(this.template());
		return this;
	}
});