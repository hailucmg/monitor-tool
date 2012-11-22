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