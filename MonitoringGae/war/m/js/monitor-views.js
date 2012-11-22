/**
 * Copyright (c) CMG Ltd All rights reserved.
 *
 * This software is the confidential and proprietary information of CMG
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with CMG.
 */

/**
 * Define the Backbone Views
 * 
 * @Creator Hai Lu
 * @author $Author$
 * @version $Revision$
 * @Last changed: $LastChangedDate$
 */

window.DashBoardView = Backbone.View.extend({

	template : _.template(render("dashboard", {})),

	render : function(eventName) {
		$(this.el).html(this.template());
		return this;
	}
});

window.SplashView = Backbone.View.extend({

	template : _.template(render("splash", {})),

	render : function(eventName) {
		$(this.el).html(this.template());
		return this;
	}
});

window.AboutView = Backbone.View.extend({

	template : _.template(render("about-us", {})),

	render : function(eventName) {
		$(this.el).html(this.template());
		return this;
	}
});

window.HelpView = Backbone.View.extend({

	template : _.template(render("help-content", {})),

	render : function(eventName) {
		$(this.el).html(this.template());
		return this;
	}
});

window.LogoutView = Backbone.View.extend({

	template : _.template(render("confirm-logout", {})),
	render : function(eventName) {
		$(this.el).html(this.template());
		return this;
	}
});

window.AdministrationView = Backbone.View.extend({

	template : _.template(render("administration", {})),

	render : function(eventName) {
		$(this.el).html(this.template());
		return this;
	}
});

window.SystemDetailView = Backbone.View.extend({

	template : _.template(render("system-detail", {})),

	render : function(eventName) {
		$(this.el).html(this.template());
		return this;
	}
});