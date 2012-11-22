/**
 * Copyright (c) CMG Ltd All rights reserved.
 *
 * This software is the confidential and proprietary information of CMG
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with CMG.
 */

/**
 * Application router
 * Define the way to move around application by URI hash
 * 
 * @Creator Hai Lu
 * @author $Author$
 * @version $Revision$
 * @Last changed: $LastChangedDate$
 */

var AppRouter = Backbone.Router.extend({
	currentPage : PAGE_DASHBOARD,
	routes : {
		"" : "dashboard",
		"about" : "about",
		"help" : "help",
		"logout" : "logout",
		"administration" : "administration",
		"dashboard/system/detail/:id" : "systemDetail"
	},

	initialize : function() {
		var self = this;
		$(document).on('click', '.back', function(e) {
			e.preventDefault();
			self.back = true;
			window.history.back();
		});

		this.firstPage = true;
	},

	dashboard : function() {
		var options = {};
		if (this.currentPage == window.PAGE_LOGOUT
				|| this.currentPage == window.PAGE_ADMINISTRATION) {
			options = {
				transition : 'slideup',
				reverse : true
			};
		}
		if (this.currentPage == window.PAGE_ABOUT
				|| this.currentPage == window.PAGE_HELP) {
			options = {
				transition : 'slidedown',
				reverse : true
			};
		}
		if (this.currentPage == window.PAGE_SYSTEM_DETAIL) {
			options = {
				transition : 'slide',
				reverse : true
			};
		}
		this.currentPage = window.PAGE_DASHBOARD;
		this.changePage(new DashBoardView(), options);
	},

	help : function() {
		this.currentPage = window.PAGE_HELP;
		this.changePage(new HelpView(), {
			transition : "slidedown"
		});
	},

	about : function() {
		this.currentPage = window.PAGE_ABOUT;
		this.changePage(new AboutView(), {
			transition : "slidedown"
		});
	},

	logout : function() {
		this.currentPage = window.PAGE_LOGOUT;
		this.changePage(new LogoutView(), {
			transition : "slideup"
		});
	},

	administration : function() {
		this.currentPage = window.PAGE_ADMINISTRATION;
		this.changePage(new AdministrationView(), {
			transition : "slideup"
		});
	},

	systemDetail : function(id) {
		log(id);
		this.currentPage = window.PAGE_SYSTEM_DETAIL;
		this.changePage(new SystemDetailView());
	},

	changePage : function(page, options) {
		log(this.currentPage);
		$(page.el).attr('data-role', 'page');
		page.render();
		$('body').append($(page.el));
		if (!options) {
			options = {
				transition : "slide"
			};
		}
		options.changeHash = false;
		// $.mobile.defaultPageTransition;
		if (this.firstPage) {
			options.transition = 'none';
			this.firstPage = false;
		}
		$.mobile.changePage($(page.el), options);
	}

});