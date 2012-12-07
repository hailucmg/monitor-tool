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
	App.license = 'Copyright (c) CMG Ltd All rights reserved. ' + '\nThis software is the confidential and proprietary information of CMG '
			+ '\n("Confidential Information"). You shall not disclose such Confidential ' + '\nInformation and shall use it only in accordance with the terms of the '
			+ '\nlicense agreement you entered into with CMG.';
	App.creator = {
		name : 'Hai Lu',
		email : 'hai.lu@c-mg.com',
		skypeID : 'lh.hai'
	};
	App.charts = {
		option : {},
		data : {},
		format : {},
		columnType : {
			STRING : 'string',
			NUMBER : 'number',
			DATE : 'date',
			BOOLEAN : 'boolean'
		}
	};
	App.init = function() {
		this._common = new App.Common();
		this._common.doLogin();
		_log = new App.Log();
		_log.welcome();
		this._models = new App.Models();
		this._collections = new App.Collections();
		this._views = new App.Views();
		this._router = new App.Routers();
		setInterval(App.intervalFunc, App._common.REFRESH_TIME);
	};

	/**
	 * Start application
	 */
	App.start = function() {
		App.init();
		Backbone.history.start();
	};
	App.intervalFunc = function() {
		if (typeof App._router.page != 'undefined' && typeof App._router.page != 'undefined' && App._router.currentPage != 'undefined'
				&& App._router.currentPage == App._common.page.PAGE_SYSTEM_DETAIL) {
			App._router.page.drawGaugeCPU();
			App._router.page.drawGaugeMem();
		}
	};
	App.onOrientationChange = function(orient, screen) {
		if (typeof _log != 'undefined') {
			App.orient = orient;
			_log.log('Device change Orientation: ' + orient + " | Screen width: " + screen.width + " | Screen height: " + screen.height);
			if (typeof App._router.page != 'undefined' && typeof App._router.page != 'undefined' && App._router.currentPage != 'undefined'
					&& App._router.currentPage == App._common.page.PAGE_SYSTEM_DETAIL) {
				App.refreshCPU = false;
				App.refreshMem = false;
				_log.log('start redraw chart');
				App._router.page.drawChart();
			}
		}
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
			return (h > 9 ? h.toString() : '0' + h) + ":" + (m > 9 ? m.toString() : '0' + m) + ":" + (s > 9 ? s.toString() : '0' + s) + " "
					+ (ms > 99 ? ms.toString() : (ms > 9 ? '0' + ms : '00' + ms));
		};
		this.welcome = function() {
			this.log("\n# Welcome to " + App.name + "\n# Vesion: " + App.VERSION + "\n# Creator: " + App.creator.name + "\n# License: \n" + App.license);
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
		this.NONE_TRANSACTION = true;

		this.IS_FINISH_LOAD = false;
		this.SPLASH_TIMEOUT = 1000;
		this.REFRESH_TIME = 2000;
		this.RANDOM_RANGE = 10;
		// System information
		this.DATA_HANDLER_URL = '/mobile/handler';
		this.TEMP_DIR = '/m/templates';
		var self = this;
		self = this || self;
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

		this.doLogin = function() {
			if (self.IS_DEBUG) {
				// $.ajax({
				// type : "GET",
				// url : '/_ah',
				// async : false,
				// data : {
				// 'email' : App.creator.email,
				// 'isAdmin' : true,
				// 'action' : 'Log in',
				// 'continue' : ''
				// },
				// });
			}
			;
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
			generateURL : function(objType, method, id, item) {
				return App._common._instance().DATA_HANDLER_URL + "?" + "type=" + objType + "&method=" + method + (method == this._CREATE ? "" : ("&id=" + id))
						+ (typeof item != 'undefined' ? ("&item=" + item) : "");
			},
			types : {
				SYSTEM_MONITOR : 'system-monitor'
			},
			items : {
				JVM : 'jvm',
				CPU : 'cpu',
				MEM : 'mem',
				SERVICE : 'service',
				FILE_SYSTEM : 'file-system',
				PROPERTIES : 'properties',
				ISSUES : 'issues'
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
				FADE : self.NONE_TRANSACTION ? 'none' : 'fade',
				POP : self.NONE_TRANSACTION ? 'none' : 'pop',
				FLIP : self.NONE_TRANSACTION ? 'none' : 'flip',
				TURN : self.NONE_TRANSACTION ? 'none' : 'turn',
				FLOW : self.NONE_TRANSACTION ? 'none' : 'flow',
				SLIDE_FADE : self.NONE_TRANSACTION ? 'none' : 'slidefade',
				SLIDE : self.NONE_TRANSACTION ? 'none' : 'slide',
				SLIDE_UP : self.NONE_TRANSACTION ? 'none' : 'slideup',
				SLIDE_DOWN : self.NONE_TRANSACTION ? 'none' : 'slidedown',
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
			init : function() {
				this.id = this.get('encodedKey');
				this.set({
					strSearch : this.get('healthStatus') + ' ' + (this.get('isActive') ? 'online' : 'offline'),
					viewBar : this.get('lastestCpuUsage') != -1 && this.get('lastestMemoryUsage') != -1
				});
			},
			template : App._common.templates.items.DASHBOARD_SYSTEM,
			methodUrl : function(method) {
				return App._common.method.generateURL(App._common.method.types.SYSTEM_MONITOR, method, this.get('id'));
			},
			getTitle : function() {
				return (typeof this.get('isDeleted') != 'undefined' && this.get('isDeleted') == true) ? 'Unknown' : (this.get('code') + ' - ' + this.get('name'));
			}
		});
		this.SystemDetail = Backbone.Model.extend({
			methodUrl : function(method) {
				return App._common.method.generateURL(App._common.method.types.SYSTEM_MONITOR, method, this.get('id'), this.get('item'));
			},
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
			url : App._common.method.generateURL(App._common.method.types.SYSTEM_MONITOR, App._common.method._LIST),
			render : function() {
				if (this.length > 0) {
					var temp = '';
					this.each(function(model) {
						model.init();
						temp += App._common.render(model.template, model.toJSON());
					});
					return temp;
				}
				return '';
			},
			getSys : function(id) {
				if (this.length > 0) {
					var sys = null;
					this.each(function(model) {
						_log.log(model.get('encodedKey') + ' | ' + id);
						if (model.get('encodedKey') == id) {
							sys = model;
						}
					});
					return sys;
				}
				return null;
			}
		});
		this.SystemDetails = Backbone.Collection.extend({
			model : App._models.SystemDetail,
			url : App._common.method.generateURL(App._common.method.types.SYSTEM_MONITOR, App._common.method._READ, this.sid, this.item)
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
		this.DashBoardView = Backbone.View.extend({
			ulRoot : '#dashboard-system-list',
			render : function(eventName) {
				$(this.el).html(App._common.render(App._common.templates.DASHBOARD, {}));
				App._collections._systems = new App._collections.SystemMonitors();
				var message = '';
				App._collections._systems.fetch({
					async : false,
					success : function(data) {
						if (data.length > 0) {
							if (typeof data.at(0).get('message') != 'undefined') {
								message = data.at(0).get('message');
							}
						} else {
							message = "No system found";
						}
						_log.log("Fetch list system monitor success. Length: " + data.length, _log.INFO);
					},
					error : function(jqXHR, textStatus, errorThrown) {
						_log.log(textStatus.statusText, _log.ERROR);
						message = textStatus.statusText;
					}
				}, {});
				if (App._collections._systems.length > 0 && message.length == 0) {
					$(this.el).find(this.ulRoot).html(App._collections._systems.render());
				} else {
					$(this.el).find(this.ulRoot).html(App._common.render(App._common.templates.items.MESSAGE, {
						message : message
					}));
				}
				return this;
			}
		});
		this.SplashView = Backbone.View.extend({
			render : function(eventName) {
				$(this.el).html(App._common.render(App._common.templates.SPLASH, {}));
				return this;
			}
		});

		this.AboutView = Backbone.View.extend({
			render : function(eventName) {
				$(this.el).html(App._common.render(App._common.templates.ABOUT_US, {}));
				return this;
			}
		});

		this.HelpView = Backbone.View.extend({
			render : function(eventName) {
				$(this.el).html(App._common.render(App._common.templates.HELP_CONTENT, {}));
				return this;
			}
		});

		this.LogoutView = Backbone.View.extend({
			render : function(eventName) {
				$(this.el).html(App._common.render(App._common.templates.CONFIRM_LOGOUT, {}));
				return this;
			}
		});

		this.AdministrationView = Backbone.View.extend({
			render : function(eventName) {
				$(this.el).html(App._common.render(App._common.templates.ADMINISTRATION, {}));
				return this;
			}
		}),

		this.SystemDetailView = Backbone.View.extend({
			render : function(eventName) {
				_log.log(this.id);
				var item = this.options ? this.options.fItem : App._common.method.items.SERVICE;
				if (typeof item != 'undefined') {
					_log.log(item);
				} else {
					item = App._common.method.items.SERVICE;
					_log.log('No item found', _log.WARNING);
				}
				if (typeof App._collections._systems == 'undefined' || App._collections._systems.length == 0) {
					if (typeof App._models._sys == 'undefined') {
						App._models._sys = new App._models.SystemMonitor();
						App._models._sys.set({
							id : this.id
						});
						App._models._sys.fetch({
							async : false
						}, {});
					}
				} else {
					App._models._sys = App._collections._systems.getSys(this.id);
				}
				App._models._sys.init();
				if (typeof App._models._sys.get('isDeleted') != 'undefined' && !App._models._sys.get('isDeleted')) {
					if (item == App._common.method.items.SERVICE) {
						App._collections._services = new App._collections.SystemDetails();
						App._collections._services.fetch({
							data : {
								item : App._common.method.items.SERVICE,
								sid : App._models._sys.get('encodedKey')
							},
							async : false
						}, {});
						App._models._jvm = new App._models.SystemDetail();
						App._models._jvm.fetch({
							data : {
								item : App._common.method.items.JVM,
								sid : App._models._sys.get('encodedKey')
							},
							async : false
						}, {});
					} else if (item == App._common.method.items.PROPERTIES) {
						App._collections._cpus = new App._collections.SystemDetails();
						App._collections._cpus.fetch({
							data : {
								item : App._common.method.items.CPU,
								sid : App._models._sys.get('encodedKey')
							},
							async : false
						}, {});
						App._collections._mems = new App._collections.SystemDetails();
						App._collections._mems.fetch({
							data : {
								item : App._common.method.items.MEM,
								sid : App._models._sys.get('encodedKey')
							},
							async : false
						}, {});
						App._collections._filesystems = new App._collections.SystemDetails();
						App._collections._filesystems.fetch({
							data : {
								item : App._common.method.items.FILE_SYSTEM,
								sid : App._models._sys.get('encodedKey')
							},
							async : false
						}, {});
					} else if (item == App._common.method.items.ISSUES) {

					}
				}

				$(this.el).html(App._common.render(App._common.templates.SYSTEM_DETAIL, {
					id : this.id,
					item : item,
					sys : App._models._sys
				}));
				return this;
			},
			initChart : function(width) {
				if (typeof App.charts.option.table == 'undefined') {
					App.charts.option.table = {
						allowHtml : true,
						showRowNumber : true
					};
				}
				App.charts.option.table.width = 0.92 * width;
				if (typeof App.charts.option.pie == 'undefined') {
					App.charts.option.pie = {
						backgroundColor : 'transparent',
						is3D : true,
						legend : {
							position : 'bottom'
						},
						chartArea : {
							top : 20
						},
						titleTextStyle : {
							fontSize : 13
						}
					};
				}
				App.charts.option.pie.width = width;
				App.charts.option.pie.height = width;
				App.charts.option.pie.chartArea.width = 0.8 * width;
				App.charts.option.pie.chartArea.height = 0.8 * width;
				App.charts.option.pie.chartArea.left = 0.05 * width;
				if (typeof App.charts.tableService != 'undefined') {
					App.charts.tableService.clearChart();
				}
				if (typeof App.charts.format.color == 'undefined') {
					App.charts.format.color = new google.visualization.ColorFormat();
					App.charts.format.color.addRange(0, 200, "blue", "");
					App.charts.format.color.addRange(200, 500, "orange", "");
					App.charts.format.color.addRange(500, 1000000, "red", "");
				}
				if (typeof App.charts.option.gauge == 'undefined') {
					App.charts.option.gauge = {
						backgroundColor : 'transparent',
						redFrom : 90,
						redTo : 100,
						yellowFrom : 75,
						yellowTo : 90,
						minorTicks : 5
					};
				}
				App.charts.option.gauge.width = 0.45 * width;
				App.charts.option.gauge.height = 0.45 * width;

				$zone = $('#cpu-mem-zone');
				if (typeof $zone != 'undefined') {
					$zone.html('<table><tbody><tr><td><div id="gauge-cpu"></div></td><td><div id="gauge-mem"></div></td></tbody></tr></table>');
				}
			},
			drawTableFileSystem : function() {

			},
			drawGaugeCPU : function() {
				var div = document.getElementById('gauge-cpu');
				if (div) {
					if (typeof App._collections._cpus != 'undefined' && App._collections._cpus.length > 0) {
						if (App._collections._cpus.length == 1 || typeof App._collections._cpus.last().get('cpuUsage') == 'undefined' || App._collections._cpus.last().get('cpuUsage') == -1) {
							// mare sure table will not draw when have no data
							return;
						}
						var usage = App._collections._cpus.last().get('cpuUsage');

						usage += (Math.round(Math.random() * App._common.RANDOM_RANGE - App._common.RANDOM_RANGE/2));
						usage = usage < 0 ? 0 : usage;
						usage = usage > 100 ? 100 : usage;

						var data = google.visualization.arrayToDataTable([ [ 'Label', 'Value' ], [ 'CPU', usage ] ]);
						if (typeof App.charts.gaugeCPU == 'undefined' || !App.refreshCPU) {
							App.charts.gaugeCPU = new google.visualization.Gauge(div);
							App.refreshCPU = true;
						}
						App.charts.gaugeCPU.draw(data, App.charts.option.gauge);
					}
				} else {
					App.refreshCPU = false;
				}
			},
			drawGaugeMem : function() {
				var div = document.getElementById('gauge-mem');
				if (div) {
					if (typeof App._collections._mems != 'undefined' && App._collections._mems.length > 0) {
						if (App._collections._mems.length == 1 || typeof App._collections._mems.last().get('usedMemory') == 'undefined' || App._collections._mems.last().get('usedMemory') == -1) {
							// mare sure table will not draw when have no data
							return;
						}
						var usage = Math.round((App._collections._mems.last().get('usedMemory') / App._collections._mems.last().get('totalMemory')) * 100);

						usage += (Math.round(Math.random() * App._common.RANDOM_RANGE - App._common.RANDOM_RANGE / 2));
						usage = usage < 0 ? 0 : usage;
						usage = usage > 100 ? 100 : usage;

						var data = google.visualization.arrayToDataTable([ [ 'Label', 'Value' ], [ 'Memory', usage ] ]);
						if (typeof App.charts.gaugeMem == 'undefined' || !App.refreshMem) {
							App.charts.gaugeMem = new google.visualization.Gauge(div);	
							App.refreshMem = true;
						}
						App.charts.gaugeMem.draw(data, App.charts.option.gauge);
					}
				} else {
					App.refreshMem = false;
				}
			},
			drawAreaCPU : function() {
				var div = document.getElementById('area-cpu');
				if (div) {
					var data = google.visualization.arrayToDataTable([ [ '', 'Usage' ], [ '', 660 ], [ '', 1030 ] ]);
					var options = {
						backgroundColor : '#F1F1F1',
						width : 300,
						isStacked : false
					};
					App.charts.areaCPU = new google.visualization.AreaChart(div);
					App.charts.areaCPU.draw(data, options);
				}
			},
			drawAreaMem : function() {
				var div = document.getElementById('area-mem');
				if (div) {
					var data = google.visualization.arrayToDataTable([ [ '', 'Usage' ], [ '', 660 ], [ '', 1030 ] ]);
					var options = {
						backgroundColor : '#F1F1F1',
						width : 300,
						isStacked : false
					};
					App.charts.areaCPU = new google.visualization.AreaChart(div);
					App.charts.areaCPU.draw(data, options);
				}
			},
			drawPieJVM : function() {
				var div = document.getElementById('pie-jvm');
				if (div) {
					if (typeof App._models._jvm != 'undefined' && typeof App._models._jvm.get('message') == 'undefined') {
						var data = new google.visualization.DataTable();
						data.addColumn(App.charts.columnType.STRING, "Task");
						data.addColumn(App.charts.columnType.NUMBER, "Memory");
						data.addRow([ 'Free space', {
							v : App._models._jvm.get('freeMemory'),
							f : App._models._jvm.get('strFreeMemory')
						} ]);
						data.addRow([ 'Used Space', {
							v : App._models._jvm.get('usedMemory'),
							f : App._models._jvm.get('strUsedMemory')
						} ]);
						App.charts.option.pie.title = 'Total ' + App._models._jvm.get('strTotalMemory') + " of " + App._models._jvm.get('strMaxMemory') + ' max memory';
						App.charts.pieJVM = new google.visualization.PieChart(div);
						App.charts.pieJVM.draw(data, App.charts.option.pie);
					}
				}
			},
			drawTableService : function() {
				// draw table for Service Information
				var div = document.getElementById('table-services');
				if (div) {
					if (typeof App._collections._services != 'undefined' && App._collections._services.length > 0) {
						if (App._collections._services.length == 1 && typeof App._collections._services.at(0).get('name') == 'undefined'
								&& typeof App._collections._services.at(0).get('ping') == 'undefined' && typeof App._collections._services.at(0).get('status') == 'undefined'
								&& typeof App._collections._services.at(0).get('strSystemDate') == 'undefined') {
							// mare sure table will not draw when have no data
							return;
						}
						_log.log("Service length: " + App._collections._services.length);
						var data = new google.visualization.DataTable();
						data.addColumn(App.charts.columnType.STRING, "Name");
						data.addColumn(App.charts.columnType.STRING, "System date");
						data.addColumn(App.charts.columnType.NUMBER, "Ping");
						data.addColumn(App.charts.columnType.STRING, "Status");
						App._collections._services.each(function(model) {
							data.addRow([
									(model.get('name') && model.get('name').length > 0) ? model.get('name') : 'N/A',
									model.get('strSystemDate'),
									{
										v : model.get('ping'),
										f : (model.get('ping') + ' ms')
									},
									'<img src="images/icon/' + (model.get('status') ? 'true' : 'false')
											+ '_icon.png" width="24" height="24" style="display: block; margin-left: auto; margin-right: auto"/>' ]);
						});
						App.charts.format.color.format(data, 2);
						App.charts.tableService = new google.visualization.Table(div);
						App.charts.tableService.draw(data, App.charts.option.table);
					}
				}
			},
			drawChart : function() {
				var currentPage = App._router.page;
				var width = $(window).width();
				var height = $(window).height();
				switch (App.orient) {
				case 90:
				case -90:
					width = (width <= height) ? height : width;
					break;
				case 0:
				default:
					break;
				}
				_log.log("Screen width: " + width + " | height: " + height);
				currentPage.initChart(width);
				currentPage.drawTableService();
				currentPage.drawPieJVM();
				currentPage.drawGaugeCPU();
				currentPage.drawGaugeMem();
				currentPage.drawAreaCPU();
				currentPage.drawAreaMem();
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
		this.router = Backbone.Router.extend({
			routes : {
				"" : "splash",
				"dashboard" : "dashboard",
				"about" : "about",
				"help" : "help",
				"logout" : "logout",
				"administration" : "administration",
				"dashboard/system/detail/:id" : "systemDetail",
				"dashboard/system/detail/:id/:item" : "systemDetail"
			},

			initialize : function() {
				// var self = this;
				this.firstPage = true;
			},

			dashboard : function() {
				var options = {};
				if (App._router.currentPage == App._common.page.PAGE_LOGOUT || App._router.currentPage == App._common.page.PAGE_ADMINISTRATION) {
					options = {
						transition : App._common.page.transitions.SLIDE_UP,
						reverse : true
					};
				}
				if (App._router.currentPage == App._common.page.PAGE_ABOUT || this.currentPage == App._common.page.PAGE_HELP) {
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
				this.changePage(new App._views.DashBoardView(), options);
			},

			splash : function() {
				self = this;
				if (App._common.IS_FINISH_LOAD) {
					this.dashboard();
				} else {
					App._router.currentPage = App._common.page.PAGE_SPLASH;
					this.changePage(new App._views.SplashView(), {
						transition : App._common.page.transitions.SLIDE_DOWN
					});
					setTimeout(App._router._instance.hideSplash, App._common.SPLASH_TIMEOUT);
				}
			},

			help : function() {
				var tran = (App._router.currentPage == App._common.page.PAGE_ABOUT ? App._common.page.transitions.FLIP : App._common.page.transitions.SLIDE_DOWN);
				App._router.currentPage = App._common.page.PAGE_HELP;
				this.changePage(new App._views.HelpView(), {
					transition : tran
				});
			},

			about : function() {
				var tran = (App._router.currentPage == App._common.page.PAGE_HELP ? App._common.page.transitions.FLIP : App._common.page.transitions.SLIDE_DOWN);
				App._router.currentPage = App._common.page.PAGE_ABOUT;
				this.changePage(new App._views.AboutView(), {
					transition : tran
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

			systemDetail : function(id, item) {
				var tran = (App._router.currentPage == App._common.page.PAGE_SYSTEM_DETAIL ? App._common.page.transitions.NONE : App._common.page.transitions.SLIDE);
				App._router.currentPage = App._common.page.PAGE_SYSTEM_DETAIL;
				App._router.page = new App._views.SystemDetailView({
					id : id,
					fItem : item
				});
				this.changePage(App._router.page, {
					transition : tran
				});
			},

			hideSplash : function() {
				App._common.IS_FINISH_LOAD = true;
				App._router.currentPage = App._router.destinationSplash.currentPage;
				App._router._instance.changePage(App._router.destinationSplash.page, App._router.destinationSplash.options);
			},

			changePage : function(page, options) {
				if (!App._common.IS_FINISH_LOAD && App._router.currentPage != App._common.page.PAGE_SPLASH) {
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
				_log.log('Go to page ' + App._router.currentPage + ' with transaction ' + options.transition);
				if (typeof $.mobile != 'undefined') {
					$.mobile.changePage($(page.el), options);
					if (App._router.currentPage == App._common.page.PAGE_SYSTEM_DETAIL) {
						setTimeout(App._router.page.drawChart, 300);
					}
				}
			}
		});
		this._instance = new this.router();
	};
}).call(this);

google.load("visualization", "1", {
	packages : [ 'corechart', 'gauge', 'table' ]
});
google.setOnLoadCallback(App.start);

var supportsOrientationChange = "onorientationchange" in window, orientationEvent = supportsOrientationChange ? "orientationchange" : "resize";

window.addEventListener(orientationEvent, function() {
	if (typeof App != 'undefined' && typeof App.onOrientationChange != 'undefined') {
		App.onOrientationChange(window.orientation, screen);
	}
}, false);
