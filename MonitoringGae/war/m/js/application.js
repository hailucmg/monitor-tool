/**
 * Copyright (c) CMG Ltd All rights reserved.
 *
 * This software is the confidential and proprietary information of CMG
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with CMG.
 */

/**
 * Monitor Mobile Application
 * 
 * @Creator Hai Lu
 * @author $Author$
 * @version $Revision$
 * @Last changed: $LastChangedDate$
 */

(function() {
	// Initial Setup
	// -------------

	// Save a reference to the global object (`window` in the browser, `global`
	// on the server).
	var root = this;
	// Save the previous value of the `App` variable, so that it can be
	// restored later on, if `noConflict` is used.
	var App;
	if (typeof exports !== 'undefined') {
		App = exports;
	} else {
		App = root.App = {};
	}
	App.VERSION = '4.0.0';
	App.license = 'Copyright (c) CMG Ltd All rights reserved. '
			+ 'This software is the confidential and proprietary information of CMG '
			+ '("Confidential Information"). You shall not disclose such Confidential '
			+ 'Information and shall use it only in accordance with the terms of the '
			+ ' license agreement you entered into with CMG.';
	App.creator = {
		name : 'Hai Lu',
		email : 'hai.lu@c-mg.com',
		skypeID : 'lh.hai'
	};
	App.init = function() {
		_log = new App.Log();
		this._router = new App.Routers();
	};

	/**
	 * Start application
	 */
	App.start = function() {
		this.init();
		Backbone.history.start();
	};

	/**
	 * Write out the log on Browser console
	 */
	App.Log = function() {
		this.ERROR = "error";
		this.WARNING = "warning";
		this.INFO = "info";
		this.log = function(data, level) {
			if (data && App._common.IS_DEBUG && typeof console != 'undefined') {
				var val = '';
				if (!level) {
					val += "|    INFO: ";
				} else if (level == this.ERROR) {
					val += "|   ERROR: ";
				} else if (level == this.WARNING) {
					val += "| WARNING: ";
				}
				val += data;
				console.log(val);
			}
		};
	};
	/**
	 * The application common attributes, functions
	 */

	App.Common = function() {
		this.IS_DEBUG = true, this.IS_FINISH_LOAD = false;
		this.SPLASH_TIMEOUT = 1000;
		this.templates = {
			DASHBOARD : 'dashboard',
			ABOUT_US : 'about-us',
			ADMINISTRATION : 'administration',
			CONFIRM_LOGOUT : 'confirm-logout',
			HELP_CONTENT : 'help-content',
			SPLASH : 'splash',
			SYSTEM_DETAIL : 'system-detail',
			items : {
				DASHBOARD_SYSTEM : 'items/dashboard-system'
			}
		};

		// Object sync method
		this.method = {
			/**
			 * Generate the method URL to sync between JS object with server
			 * 
			 * @param objType
			 *            the type of object
			 * @param method
			 *            the method _CREATE, _DELETE, _READ, _UPDATE
			 * @param id
			 */
			generateURL : function(objType, method, id) {
				return DATA_HANDLER_URL + "?" + "type=" + objType + "&method="
						+ method + (method == _CREATE ? "" : ("&id=" + id));
			},
			types : {
				SYSTEM_MONITOR : 'system-monitor'
			},
			_READ : 'read',
			_CREATE : 'create',
			_DELETE : 'delete',
			_UPDATE : 'update',
			_LIST : 'list'
		};

		// System information
		this.DATA_HANDLER_URL = '/mobile/handler';
		this.TEMP_DIR = '/m/templates';

		// Name of page
		this.page = {
			PAGE_SPLASH : 'splash',
			PAGE_DASHBOARD : 'dashboard',
			PAGE_ABOUT : 'about',
			PAGE_HELP : 'help',
			PAGE_ADMINISTRATION : 'administration',
			PAGE_LOGOUT : 'confirm-logout',
			PAGE_SYSTEM_DETAIL : "system-detail"
		},

				/**
				 * Render the HTML string with special name
				 * 
				 * @param tmpl_name
				 *            the path and name of template
				 * @param tmpl_data
				 *            the data object to render
				 * @returns
				 */
				this.render = function(tmpl_name, tmpl_data) {
					self = this;
					if (!self.tmpl_cache) {
						self.tmpl_cache = {};
					}
					if (!self.tmpl_cache[tmpl_name]) {
						var tmpl_url = App._common.TEMP_DIR + '/' + tmpl_name
								+ '.html';
						var tmpl_string = '';
						$.ajax({
							url : tmpl_url,
							method : 'GET',
							async : false,
							success : function(data) {
								tmpl_string = data;
							}
						});

						self.tmpl_cache[tmpl_name] = _.template(tmpl_string);
					}
					return self.tmpl_cache[tmpl_name](tmpl_data);
				};
	};
	App._common = new App.Common();
	/**
	 * Application Views
	 */
	App.Views = function() {
		this.DashBoardView = Backbone.View.extend({
			template : _.template(App._common.render(
					App._common.templates.DASHBOARD, {})),
			ulRoot : '#dashboard-system-list',
			render : function(eventName) {
				$(this.el).html(this.template());
				var sys = new App._models.SystemMonitor();
				$(this.el).find(this.ulRoot).append(sys.render());
				return this;
			}
		});

		this.SplashView = Backbone.View.extend({

			template : _.template(App._common.render(
					App._common.templates.SPLASH, {})),

			render : function(eventName) {
				$(this.el).html(this.template());
				return this;
			}
		});

		this.AboutView = Backbone.View.extend({

			template : _.template(App._common.render(
					App._common.templates.ABOUT_US, {})),

			render : function(eventName) {
				$(this.el).html(this.template());
				return this;
			}
		});

		this.HelpView = Backbone.View.extend({

			template : _.template(App._common.render(
					App._common.templates.HELP_CONTENT, {})),

			render : function(eventName) {
				$(this.el).html(this.template());
				return this;
			}
		});

		this.LogoutView = Backbone.View.extend({

			template : _.template(App._common.render(
					App._common.templates.CONFIRM_LOGOUT, {})),
			render : function(eventName) {
				$(this.el).html(this.template());
				return this;
			}
		});

		this.AdministrationView = Backbone.View.extend({

			template : _.template(App._common.render(
					App._common.templates.ADMINISTRATION, {})),

			render : function(eventName) {
				$(this.el).html(this.template());
				return this;
			}
		}),

		this.SystemDetailView = Backbone.View.extend({
			template : _.template(App._common.render(
					App._common.templates.SYSTEM_DETAIL, {})),
			render : function(eventName) {
				$(this.el).html(this.template());
				return this;
			}
		});
	};
	App._views = new App.Views();
	/**
	 * Application models object
	 */
	App.Models = function() {
		this.SystemMonitor = Backbone.Model.extend({
			template : App._common.templates.items.DASHBOARD_SYSTEM,
			methodUrl : function(method) {
				return App._common.method.generateURL(
						App_common.method.types.SYSTEM_MONITOR, method, id);
			},

			defaults : function() {
				return {

				};
			},
			render : function() {
				self = this;
				return _.template(App._common.render(self.template, self));
			}
		});
	};
	App._models = new App.Models();
	App.Collections = function() {
		this.SystemMonitors = Backbone.Collection.extend({
			model : App._models.SystemMonitor,			
			url : App._common.method.generateURL(App._common.method.types.SYSTEM_MONITOR, App._common.method._LIST),
			active : function() {
				return this.filter(function(stock) {
					return stock.get('isActive');
				});
			},
			deactivate : function() {
				return this.without.apply(this, this.active());
			},
			nextOrder : function() {
				if (!this.length)
					return 1;
				return this.last().get('order') + 1;
			},
			comparator : function(stock) {
				return stock.get('order');
			}
		});
	};
	App._collections = new App.Collections();
	/**
	 * Application routers
	 */
	App.Routers = function() {
		this.destinationSplash = {
			currentPage : App._common.page.PAGE_DASHBOARD,
			page : new App._views.DashBoardView(),
			options : {
				transition : "flip"
			}
		};
		this.currentPage = App._common.page.PAGE_SPLASH;
		this.router = Backbone.Router
				.extend({
					routes : {
						"" : "splash",
						"dashboard" : "dashboard",
						"about" : "about",
						"help" : "help",
						"logout" : "logout",
						"administration" : "administration",
						"dashboard/system/detail/:id" : "systemDetail"
					},

					initialize : function() {
						// var self = this;
						this.firstPage = true;
					},

					dashboard : function() {
						var options = {};
						if (this.currentPage == App._common.page.PAGE_LOGOUT
								|| this.currentPage == App._common.page.PAGE_ADMINISTRATION) {
							options = {
								transition : 'slideup',
								reverse : true
							};
						}
						if (this.currentPage == App._common.page.PAGE_ABOUT
								|| this.currentPage == App._common.page.PAGE_HELP) {
							options = {
								transition : 'slidedown',
								reverse : true
							};
						}
						if (this.currentPage == App._common.page.PAGE_SYSTEM_DETAIL) {
							options = {
								transition : 'slide',
								reverse : true
							};
						}
						this.currentPage = App._common.page.PAGE_DASHBOARD;
						this
								.changePage(new App._views.DashBoardView(),
										options);
					},

					splash : function() {
						self = this;
						if (App._common.IS_FINISH_LOAD) {
							this.dashboard();
						} else {
							this.currentPage = App._common.page.PAGE_SPLASH;
							this.changePage(new App._views.SplashView(), {
								transition : "slidedown"
							});
							setTimeout(self.hideSplash,
									App._common.SPLASH_TIMEOUT);
						}
					},

					help : function() {
						this.currentPage = App._common.page.PAGE_HELP;
						this.changePage(new App._views.HelpView(), {
							transition : "slidedown"
						});
					},

					about : function() {
						this.currentPage = App._common.page.PAGE_ABOUT;
						this.changePage(new App._views.AboutView(), {
							transition : "slidedown"
						});
					},

					logout : function() {
						this.currentPage = App._common.page.PAGE_LOGOUT;
						this.changePage(new App._views.LogoutView(), {
							transition : "slideup"
						});
					},

					administration : function() {
						this.currentPage = App._common.page.PAGE_ADMINISTRATION;
						this.changePage(new App._views.AdministrationView(), {
							transition : "slideup"
						});
					},

					systemDetail : function(id) {
						_log.log(id);
						this.currentPage = App._common.page.PAGE_SYSTEM_DETAIL;
						this.changePage(new App._views.SystemDetailView());
					},

					hideSplash : function() {
						App._common.IS_FINISH_LOAD = true;
						App._router.currentPage = App._router.destinationSplash.currentPage;
						App._router._instance.changePage(
								App._router.destinationSplash.page,
								App._router.destinationSplash.options);
					},

					changePage : function(page, options) {
						if (!App._common.IS_FINISH_LOAD
								&& this.currentPage != App._common.page.PAGE_SPLASH) {
							this.destinationSplash.currentPage = this.currentPage;
							this.destinationSplash.page = page;
							this.splash();
							return;
						}
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
						_log.log('Go to page ' + this.currentPage
								+ ' with transaction ' + options.transition);
						if (typeof $.mobile != 'undefined') {
							$.mobile.changePage($(page.el), options);
						}
					}

				});
		this._instance = new this.router();
	};
}).call(this);
