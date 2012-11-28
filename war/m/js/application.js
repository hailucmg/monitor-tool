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
	var App;
	if (typeof exports !== 'undefined') {
		App = exports;
	} else {
		App = root.App = {};
	}
	App.name = "CMG Monitor";
	App.VERSION = '4.0.0';
	App.license = 'Copyright (c) CMG Ltd All rights reserved. '
			+ '\nThis software is the confidential and proprietary information of CMG '
			+ '\n("Confidential Information"). You shall not disclose such Confidential '
			+ '\nInformation and shall use it only in accordance with the terms of the '
			+ '\nlicense agreement you entered into with CMG.';
	App.creator = {
		name : 'Hai Lu',
		email : 'hai.lu@c-mg.com',
		skypeID : 'lh.hai'
	};
	App.init = function() {
		this._common = new App.Common();
		_log = new App.Log();
		_log.welcome();
		this._models = new App.Models();
		this._collections = new App.Collections();
		this._views = new App.Views();
		this._router = new App.Routers();
	};

	/**
	 * Start application
	 */
	App.start = function() {
		App.init();
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
				var time = this.currentTime();
				if (!level || level == this.INFO) {
					console.log("INFO " + time + " > " + data);
				} else if (level == this.ERROR) {
					console.error("ERROR " + time + " > " + data);
				} else if (level == this.WARNING) {
					console.warn("WARNING " + time + " > " + data);
				}
			}
		};
		this.currentTime = function() {
			var current = new Date();
			h = current.getHours();
			m = current.getMinutes();
			s = current.getSeconds();
			ms = current.getMilliseconds();
			return (h > 9 ? h.toString() : '0' + h)
					+ ":"
					+ (m > 9 ? m.toString() : '0' + m)
					+ ":"
					+ (s > 9 ? s.toString() : '0' + s)
					+ " "
					+ (ms > 99 ? ms.toString()
							: (ms > 9 ? '0' + ms : '00' + ms));
		};
		this.welcome = function() {

			this.log("\n# Welcome to " + App.name + "\n# Vesion: "
					+ App.VERSION + "\n# Creator: " + App.creator.name
					+ "\n# License: \n" + App.license);
		};
	};
	/**
	 * The application common attributes, functions
	 */

	App.Common = function() {
		this._instance = function() {
			if (typeof App._common == 'undefined') {
				App._common = this;
				return this;
			} else {
				return App._common;
			}
		};
		this.IS_DEBUG = true;
		this.IS_FINISH_LOAD = false;
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
				MESSAGE : 'items/message',
				DASHBOARD_SYSTEM : 'items/dashboard-system'
			}
		};
		// System information
		this.DATA_HANDLER_URL = '/mobile/handler';
		this.TEMP_DIR = '/m/templates';

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
				return App._common._instance().DATA_HANDLER_URL + "?" + "type="
						+ objType + "&method=" + method
						+ (method == this._CREATE ? "" : ("&id=" + id));
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

		// Name of page
		this.page = {
			transitions : {
				FADE : 'fade',
				POP : 'pop',
				FLIP : 'flip',
				TURN : 'turn',
				FLOW : 'flow',
				SLIDE_FADE : 'slidefade',
				SLIDE : 'slide',
				SLIDE_UP : 'slideup',
				SLIDE_DOWN : 'slidedown',
				NONE : 'none'
			},
			PAGE_SPLASH : 'splash',
			PAGE_DASHBOARD : 'dashboard',
			PAGE_ABOUT : 'about',
			PAGE_HELP : 'help',
			PAGE_ADMINISTRATION : 'administration',
			PAGE_LOGOUT : 'confirm-logout',
			PAGE_SYSTEM_DETAIL : "system-detail"
		};

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
				var tmpl_url = App._common.TEMP_DIR + '/' + tmpl_name + '.html';
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

	/**
	 * Application models object
	 */
	App.Models = function() {
		this._instance = function() {
			if (typeof App._models == 'undefined') {
				App._models = this;
				return this;
			} else {
				return App._models;
			}
		};
		this.SystemMonitor = Backbone.Model.extend({
			template : App._common.templates.items.DASHBOARD_SYSTEM,
			methodUrl : function(method) {
				return App._common.method.generateURL(
						App._common.method.types.SYSTEM_MONITOR, method,
						this.id);
			}
		});
	};

	App.Collections = function() {
		this._instance = function() {
			if (typeof App._collections == 'undefined') {
				App._collections = this;
				return this;
			} else {
				return App._collections;
			}
		};
		this.SystemMonitors = Backbone.Collection.extend({
			model : App._models.SystemMonitor,
			url : App._common.method.generateURL(
					App._common.method.types.SYSTEM_MONITOR,
					App._common.method._LIST),
			render : function() {
				if (this.length > 0) {
					var temp = '';
					this.each(function(sys) {
						temp += App._common.render(sys.template, sys);
					});
					return temp;
				}
				return '';
			}
		});
	};

	/**
	 * Application Views
	 */
	App.Views = function() {
		this._instance = function() {
			if (typeof App._views == 'undefined') {
				App._views = this;
				return this;
			} else {
				return App._views;
			}
		};
		this.DashBoardView = Backbone.View
				.extend({
					ulRoot : '#dashboard-system-list',
					render : function(eventName) {
						$(this.el).html(
								App._common.render(
										App._common.templates.DASHBOARD, {}));
						var list = new App._collections.SystemMonitors();
						var message = '';
						list
								.fetch(
										{
											async : false,
											success : function(data) {
												if (data.length > 0) {
													if (typeof data.at(0).get(
															'message') != 'undefined') {
														message = data.at(0)
																.get('message');
													}
												} else {
													message = "No system found";
												}
												_log.log(
														"Fetch list system monitor success. Length: "
																+ data.length,
														_log.INFO);
											},
											error : function(jqXHR, textStatus,
													errorThrown) {
												_log.log(textStatus.statusText,
														_log.ERROR);
												message = textStatus.statusText;
											}
										}, {});
						if (list.length > 0 && message.length == 0) {
							$(this.el).find(this.ulRoot).html(list.render());
						} else {
							$(this.el)
									.find(this.ulRoot)
									.html(
											App._common
													.render(
															App._common.templates.items.MESSAGE,
															{
																message : message
															}));
						}
						return this;
					}
				});
		this.SplashView = Backbone.View.extend({
			render : function(eventName) {
				$(this.el).html(
						App._common.render(App._common.templates.SPLASH, {}));
				return this;
			}
		});

		this.AboutView = Backbone.View
				.extend({
					render : function(eventName) {
						$(this.el).html(
								App._common.render(
										App._common.templates.ABOUT_US, {}));
						return this;
					}
				});

		this.HelpView = Backbone.View.extend({
			render : function(eventName) {
				$(this.el).html(
						App._common.render(App._common.templates.HELP_CONTENT,
								{}));
				return this;
			}
		});

		this.LogoutView = Backbone.View.extend({
			render : function(eventName) {
				$(this.el).html(
						App._common.render(
								App._common.templates.CONFIRM_LOGOUT, {}));
				return this;
			}
		});

		this.AdministrationView = Backbone.View.extend({
			render : function(eventName) {
				$(this.el).html(
						App._common.render(
								App._common.templates.ADMINISTRATION, {}));
				return this;
			}
		}),

		this.SystemDetailView = Backbone.View.extend({
			render : function(eventName) {
				$(this.el).html(
						App._common.render(App._common.templates.SYSTEM_DETAIL,
								{}));
				return this;
			},
			
			drawChart : function() {
				// draw piechart for Service Information
				var chartDivService = document
						.getElementById('chart_div_Service');
				if (chartDivService) {
					var data = google.visualization.arrayToDataTable([
							[ 'Task', 'Local Disk' ], [ 'Free Space', 9 ],
							[ 'Used Space', 11 ] ]);

					var options = {
						backgroundColor : '#F6F6F6',
						title : 'Java Virtual Machine (506.5 MB of 1012.6 MB)',
						width : 400,
						height : 240,
						is3D : true

					};
					var chartService = new google.visualization.PieChart(
							chartDivService);
					chartService.draw(data, options);
				} else {
					_log.log("cannot find id chart_div_Service", _log.ERROR);
				}

				// draw piechart for File System Information
				var chartDivFileSystem = document
						.getElementById('chart_div_File_System');
				if (chartDivFileSystem) {
					var data = google.visualization.arrayToDataTable([
							[ 'Task', 'Local Disk' ], [ 'Free Space', 9 ],
							[ 'Used Space', 11 ] ]);

					var options = {
						backgroundColor : '#F6F6F6',
						title : 'Local Disk C:\ (NTFS/local)',
						width : 400,
						height : 240,
						is3D : true

					};
					var chartService = new google.visualization.PieChart(
							chartDivFileSystem);
					chartService.draw(data, options);
				} else {
					_log.log("cannot find id chart_div_File_System",_log.ERROR);
				}
				// draw gauge chart for CPU
				var chartDivCPU = document
						.getElementById('chart_div_Gauge_CPU');
				if (chartDivCPU) {
					var data = google.visualization.arrayToDataTable([
							[ 'Label', 'Value' ], [ 'CPU', 80 ] ]);
					var options = {
						backgroundColor : '#F1F1F1',
						width : 400,
						height : 120,
						redFrom : 90,
						redTo : 100,
						yellowFrom : 75,
						yellowTo : 90,
						minorTicks : 5

					};
					var chartService = new google.visualization.Gauge(
							chartDivCPU);
					chartService.draw(data, options);
				} else {
					_log.log("cannot find id chart_div_Gauge_CPU", _log.ERROR);
				}

				// draw gauge for MEMORY
				var chartDivMemory = document
						.getElementById('chart_div_Gauge_MEMORY');
				if (chartDivMemory) {
					var data = google.visualization.arrayToDataTable([
							[ 'Label', 'Value' ], [ 'MEMORY', 50 ] ]);
					var options = {
						backgroundColor : '#F1F1F1',
						width : 400,
						height : 120,
						redFrom : 90,
						redTo : 100,
						yellowFrom : 75,
						yellowTo : 90,
						minorTicks : 5

					};
					var chartService = new google.visualization.Gauge(
							chartDivMemory);
					chartService.draw(data, options);
				} else {
					_log.log("cannot find id chart_div_Gauge_MEMORY",
							_log.ERROR);
				}

				// draw arena chart for CPU usage
				var chartArenaDivCPU = document.getElementById('chart_div_Arena_CPU');
				if (chartArenaDivCPU) {
					var data = google.visualization.arrayToDataTable([
							[ '', 'Usage'],
							[ '', 660 ], [ '', 1030] ]);
					var options = {
							backgroundColor : '#F1F1F1',
							width:300,
							isStacked : false
					};

					var chartService = new google.visualization.AreaChart(chartArenaDivCPU);
					chartService.draw(data, options);
				} else {
					_log.log("cannot find id chart_div_Arena_CPU",_log.ERROR);
				}
				
				//draw arena chart for MEMORY
				var chartArenaDivMEMORY = document.getElementById('chart_div_Arena_MEMORY');
				if (chartArenaDivMEMORY) {
					var data = google.visualization.arrayToDataTable([
							[ '', 'Usage'],
							[ '', 660 ], [ '', 1030] ]);
					var options = {
							backgroundColor : '#F1F1F1',
							width:300,
							isStacked : false
					};

					var chartService = new google.visualization.AreaChart(chartArenaDivMEMORY);
					chartService.draw(data, options);
				} else {
					_log.log("cannot find id chart_div_Arena_MEMORY",_log.ERROR);
				}
				
				
			}
		});
	};

	/**
	 * Application routers
	 */
	App.Routers = function() {
		this.destinationSplash = {
			currentPage : App._common.page.PAGE_DASHBOARD,
			page : new App._views.DashBoardView(),
			options : {
				transition : App._common.page.transitions.FLIP
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
						if (App._router.currentPage == App._common.page.PAGE_LOGOUT
								|| App._router.currentPage == App._common.page.PAGE_ADMINISTRATION) {
							options = {
								transition : App._common.page.transitions.SLIDE_UP,
								reverse : true
							};
						}
						if (App._router.currentPage == App._common.page.PAGE_ABOUT
								|| this.currentPage == App._common.page.PAGE_HELP) {
							options = {
								transition : App._common.page.transitions.SLIDE_DOWN,
								reverse : true
							};
						}
						if (App._router.currentPage == App._common.page.PAGE_SYSTEM_DETAIL) {
							options = {
								transition : App._common.page.transitions.SLIDE,
								reverse : true
							};
						}
						App._router.currentPage = App._common.page.PAGE_DASHBOARD;
						this
								.changePage(new App._views.DashBoardView(),
										options);
					},

					splash : function() {
						self = this;
						if (App._common.IS_FINISH_LOAD) {
							this.dashboard();
						} else {
							App._router.currentPage = App._common.page.PAGE_SPLASH;
							this
									.changePage(
											new App._views.SplashView(),
											{
												transition : App._common.page.transitions.SLIDE_DOWN
											});
							setTimeout(App._router._instance.hideSplash,
									App._common.SPLASH_TIMEOUT);
						}
					},

					help : function() {
						App._router.currentPage = App._common.page.PAGE_HELP;
						this
								.changePage(
										new App._views.HelpView(),
										{
											transition : App._common.page.transitions.SLIDE_DOWN
										});
					},

					about : function() {
						App._router.currentPage = App._common.page.PAGE_ABOUT;
						this
								.changePage(
										new App._views.AboutView(),
										{
											transition : App._common.page.transitions.SLIDE_DOWN
										});
					},

					logout : function() {
						App._router.currentPage = App._common.page.PAGE_LOGOUT;
						this.changePage(new App._views.LogoutView(), {
							transition : App._common.page.transitions.SLIDE_UP
						});
					},

					administration : function() {
						App._router.currentPage = App._common.page.PAGE_ADMINISTRATION;
						this.changePage(new App._views.AdministrationView(), {
							transition : App._common.page.transitions.SLIDE_UP
						});
					},

					systemDetail : function(id) {
						_log.log(id);
						App._router.currentPage = App._common.page.PAGE_SYSTEM_DETAIL;
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
								&& App._router.currentPage != App._common.page.PAGE_SPLASH) {
							App._router.destinationSplash.currentPage = App._router.currentPage;
							App._router.destinationSplash.page = page;
							this.splash();
							return;
						}
						$(page.el).attr('data-role', 'page');
						page.render();
						$('body').append($(page.el));
						if (!options) {
							options = {
								transition : App._common.page.transitions.SLIDE
							};
						}
						options.changeHash = false;
						// $.mobile.defaultPageTransition;
						if (this.firstPage) {
							options.transition = App._common.page.transitions.NONE;
							this.firstPage = false;
						}
						_log.log('Go to page ' + App._router.currentPage
								+ ' with transaction ' + options.transition);
						if (typeof $.mobile != 'undefined') {
							$.mobile.changePage($(page.el), options);
							if (App._router.currentPage == App._common.page.PAGE_SYSTEM_DETAIL) {
								page.drawChart();
							}
						}
					}

				});
		this._instance = new this.router();
	};
}).call(this);
