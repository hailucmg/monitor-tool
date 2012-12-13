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
	var root = this;
	var App;
	if (typeof exports !== 'undefined') {
		App = exports;
	} else {
		App = root.App = {};
	}
	App.isTouchDevice = !!('ontouchstart' in window) || !!('onmsgesturechange' in window);
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
	App.isNumber = function(n) {
		return !isNaN(parseFloat(n)) && isFinite(n);
	};

	App.handler = {

	};
	App.requestPermission = false;
	App.currentUser = null;
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
		this.currentUser = new this._models.UserMonitor();
		this.currentUser.fetch({
			async : false
		}, {});
		this.currentUser.set({
			isAuth : false
		});
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
	App.intervalFunc = function() {
		_log.log("I'm in interval !!!");
		if (typeof App._router.page != 'undefined' && typeof App._router.page != 'undefined' && App._router.currentPage != 'undefined'
				&& App._router.currentPage == App._common.page.PAGE_SYSTEM_DETAIL) {
			if (App._router.page.getItem() == App._common.method.items.DETAIL) {
				if (typeof App._collections._cpus != 'undefined' && App._collections._cpus.length > 1) {
					App._collections._cpus.addRandomValue(20);
					if (App._collections._cpus.isValid()) {
						App._router.page.drawGaugeCPU();
						App._router.page.drawAreaCPU();
					}
				}
				if (typeof App._collections._mems != 'undefined' && App._collections._mems.length > 1) {
					App._collections._mems.addRandomValue(10);
					if (App._collections._mems.isValid()) {
						App._router.page.drawGaugeMem();
						App._router.page.drawAreaMem();
					}
				}
			}
		}
	};
	App.onOrientationChange = function(orient, screen) {
		var $sc = $('#splash-screen');
		if ($sc) {
			var w = $(window).width();
			var h = $(window).height();
			var _w = w, _h = h;
			if (orient == 90 || orient == -90) {
				_w = w < h ? h : w;
				_h = w < h ? w : h;
			}
			$sc.css('width', _w + 'px').css('height', _h + 'px');
		}
		if (typeof _log != 'undefined') {
			App.orient = orient;
			if (typeof App._router.page != 'undefined' && typeof App._router.page != 'undefined' && App._router.currentPage != 'undefined'
					&& App._router.currentPage == App._common.page.PAGE_SYSTEM_DETAIL) {
				App.refreshCPU = false;
				App.refreshMem = false;
				_log.log('start redraw chart');
				App._router.clearChart();
				App._router.page.renderForm();
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
		this.SCOPE = 'https://www.googleapis.com/auth/plus.me';
		this.CLIENT_ID = '371638778910-mjpkecge6b7bq4v6j17aags33rp8fk5p.apps.googleusercontent.com';
		this.API_KEY = 'AIzaSyBAVK0V8M7yW_lZrwfwShePRJb8MEcAFXQ';
		this.IS_DEBUG = false;
		this.NONE_TRANSACTION = false;

		this.IS_FINISH_LOAD = false;
		this.SPLASH_TIMEOUT = 4000;
		this.IS_REFRESH = true;
		this.REFRESH_TIME = 3000;
		this.RANDOM_RANGE = 15;

		this.ISSUE_RECEIVE_LENGTH = 3;
		this.currentReceive = 0;
		// System information
		this.DATA_HANDLER_URL = '/mobile/handler';
		this.TEMP_DIR = 'templates';
		var self = this;
		self = this || self;
		this.roles = {
			ADMIN : 1,
			USER : 2,
			GUEST : 3
		}, this.templates = {
			DASHBOARD : 'dashboard',
			ABOUT_US : 'about-us',
			ADMINISTRATION : 'administration',
			CONFIRM_LOGOUT : 'confirm-logout',
			HELP_CONTENT : 'help-content',
			SPLASH : 'splash',
			SYSTEM_DETAIL : 'system-detail',
			items : {
				MESSAGE : 'items/message',
				DASHBOARD_SYSTEM : 'items/dashboard-system',
				CPU_MEM_ZONE : 'items/cpu-mem-zone',
				SYSTEM_ISSUE : 'items/system-issue',
				LOCAL_DISK_ZONE : 'items/local-disk-zone',
			}
		};

		this.S4 = function() {
			return (((1 + Math.random()) * 0x10000) | 0).toString(16).substring(1);
		};

		// Generate a pseudo-GUID by concatenating random hexadecimal.
		this.guid = function() {
			return (self.S4() + self.S4() + "-" + self.S4() + "-" + self.S4() + "-" + self.S4() + "-" + self.S4() + self.S4() + self.S4());
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
				SYSTEM_MONITOR : 'system-monitor',
				USER_MONITOR : 'user'
			},
			items : {
				JVM : 'jvm',
				CPU : 'cpu',
				MEM : 'mem',
				SERVICE : 'service',
				ISSUE : 'issue',
				DETAIL : 'detail',
				STATISTIC : 'statistic',
				FILE_SYSTEM : 'file-system'
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

		this.UserMonitor = Backbone.Model.extend({
			methodUrl : function(method) {
				return App._common.method.generateURL(App._common.method.types.USER_MONITOR, method);
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
			valid : false,
			url : App._common.method.generateURL(App._common.method.types.SYSTEM_MONITOR, App._common.method._READ, this.sid, this.item),
			initUsageValue : function() {
				var self = this;
				self = this || self;
				if (this.length > 1) {
					this.each(function(model) {
						var usage = -1;
						if (typeof model.get('cpuUsage') != 'undefined') {
							usage = model.get('cpuUsage');
							if (usage > 0) {
								self.valid = true;
							} else {
								usage = 0;
							}
						}
						if (typeof model.get('usedMemory') != 'undefined') {
							usage = model.get('usedMemory');
							if (typeof model.get('totalMemory') != 'undefined') {
								usage = Math.round((model.get('usedMemory') / model.get('totalMemory')) * 100);
							}
							if (usage > 0) {
								self.valid = true;
							} else {
								usage = 0;
							}
						}
						model.set({
							'usage' : (usage == -1 ? 0 : usage)
						});
					});
				}
			},
			isValid : function() {
				return this.valid;
			},
			addRandomValue : function(randomRange) {
				if (this.length > 1) {
					var _m = Math;
					if (typeof this.lastUsage == 'undefined') {
						this.lastUsage = this.last().get('usage');
					}
					var usage = this.lastUsage;
					if (typeof randomRange == 'undefined') {
						randomRange = App._common.RANDOM_RANGE;
					}
					usage += (_m.round(_m.random() * randomRange - randomRange / 2));
					usage = usage < 0 ? 0 : usage;
					usage = usage > 100 ? 100 : usage;
					this.remove(this.at(0));
					this.add({
						id : App._common.guid(),
						usage : usage
					});
				}
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
		this.DashBoardView = Backbone.View.extend({
			ulRoot : '#dashboard-system-list',
			render : function(eventName) {
				App._router.showLoading(true);
				$(this.el).html(App._common.render(App._common.templates.DASHBOARD, {
					isTouchDevice : App.isTouchDevice
				}));
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
				$(this.el).html(App._common.render(App._common.templates.SPLASH, {
					displayName : _profile.displayName,
					imageURL : _profile.imageURL,
					isAuth : _profile.isAuth
				}));
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
				App.currentUser.set({
					isAuth : window._profile.isAuth,
					imageURL : window._profile.imageURL,
					displayName : window._profile.displayName,
					reqPer : App.requestPermission,
					isTouchDevice : App.isTouchDevice
				});
				$(this.el).html(App._common.render(App._common.templates.CONFIRM_LOGOUT, App.currentUser.toJSON()));
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
			getItem : function() {
				return this.options ? (typeof this.options.fItem != 'undefined' ? this.options.fItem : App._common.method.items.DETAIL) : App._common.method.items.DETAIL;
			},
			render : function(eventName) {
				App._router.showLoading(true);
				_log.log(this.id);
				var item = this.options ? this.options.fItem : App._common.method.items.DETAIL;
				if (typeof item != 'undefined') {
					_log.log(item);
				} else {
					item = App._common.method.items.DETAIL;
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
					if (item == App._common.method.items.DETAIL) {
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

						App._collections._cpus = new App._collections.SystemDetails();
						App._collections._cpus.fetch({
							data : {
								item : App._common.method.items.CPU,
								sid : App._models._sys.get('encodedKey')
							},
							async : false
						}, {});
						App._collections._cpus.initUsageValue();
						App._collections._mems = new App._collections.SystemDetails();
						App._collections._mems.fetch({
							data : {
								item : App._common.method.items.MEM,
								sid : App._models._sys.get('encodedKey')
							},
							async : false
						}, {});
						App._collections._mems.initUsageValue();
						App._collections._filesystems = new App._collections.SystemDetails();
						App._collections._filesystems.fetch({
							data : {
								item : App._common.method.items.FILE_SYSTEM,
								sid : App._models._sys.get('encodedKey')
							},
							async : false
						}, {});
					} else if (item == App._common.method.items.STATISTIC) {
						App._collections._issues = new App._collections.SystemDetails();
						App._collections._issues.fetch({
							data : {
								item : App._common.method.items.ISSUE,
								sid : App._models._sys.get('encodedKey')
							},
							async : false
						}, {});
					}
				}

				$(this.el).html(App._common.render(App._common.templates.SYSTEM_DETAIL, {
					id : this.id,
					item : item,
					sys : App._models._sys,
					isTouchDevice : App.isTouchDevice
				}));
				return this;
			},
			initChart : function(width, height) {
				var item = App._router.page.getItem();
				var _s = (App.orient == 90 || App.orient == -90);
				var _w = (width >= height ? height : width);
				var _h = (width >= height ? width : height);
				if (item == App._common.method.items.DETAIL) {
					$div_s = $('#table-services');

					if ($div_s) {
						$div_s.css("width", (width - 30) + 'px').css('margin-left', "-15px");
					}
					var _ot = App.charts.option.table;
					if (typeof _ot == 'undefined') {
						_ot = {
							allowHtml : true
						};
					}
					_ot.width = 0.92 * width;
					App.charts.option.table = _ot;

					var _otf = App.charts.option.tableFs;
					if (typeof _otf == 'undefined') {
						_otf = {
							allowHtml : true
						};
					}
					_otf.width = 0.92 * (_s ? (_h - _w) : _w);
					App.charts.option.tableFs = _otf;

					var _op = App.charts.option.pie;
					if (typeof _op == 'undefined') {
						_op = {
							backgroundColor : 'transparent',
							is3D : true,
							legend : {
								alignment : 'center'
							},
							chartArea : {
								top : 20
							},
							titleTextStyle : {
								fontSize : 13
							}
						};
					}
					_op.legend.position = _s ? 'right' : 'bottom';
					var _ps = (_s ? 0.8 : 0.9) * width;
					_op.width = _ps;
					_op.height = (_s ? 0.6 : 1) * _ps;
					_op.chartArea.width = (_s ? 0.8 : 0.75) * _ps;
					_op.chartArea.height = (_s ? 0.5 : 0.75) * _ps;
					_op.chartArea.left = 0.08 * _ps;
					App.charts.option.pie = _op;

					var _opf = App.charts.option.pieFs;
					if (typeof _opf == 'undefined') {
						_opf = {
							backgroundColor : 'transparent',
							is3D : true,
							legend : {
								alignment : 'center'
							},
							chartArea : {
								top : 20
							},
							titleTextStyle : {
								fontSize : 13
							}
						};
					}
					_opf.legend.position = !_s ? 'right' : 'bottom';
					var _psf = (_s ? 0.8 : 1) * _w;
					_opf.width = (_s ? 0.7 : 1) * _psf;
					_opf.height = (_s ? 0.7 : 0.5) * _psf;
					_opf.chartArea.width = (_s ? 0.65 : 0.8) * _psf;
					_opf.chartArea.height = (_s ? 0.5 : 0.45) * _psf;
					_opf.chartArea.left = (_s ? -0.02 : 0.02) * _psf;
					App.charts.option.pieFs = _opf;

					var _fc = App.charts.format.color;
					if (typeof _fc == 'undefined') {
						_fc = new google.visualization.ColorFormat();
						_fc.addRange(0, 200, "blue", "");
						_fc.addRange(200, 500, "orange", "");
						_fc.addRange(500, 1000000, "red", "");
						App.charts.format.color = _fc;
					}
					var _og = App.charts.option.gauge;
					if (typeof _og == 'undefined') {
						_og = {
							backgroundColor : 'transparent',
							redFrom : 90,
							redTo : 100,
							yellowFrom : 75,
							yellowTo : 90,
							minorTicks : 5
						};
					}
					_og.width = (_s ? 0.45 : 0.4) * _w;
					_og.height = (_s ? 0.45 : 0.4) * _w;
					App.charts.option.gauge = _og;

					$cm_zone = $('#cpu-mem-zone');
					if (typeof $cm_zone != 'undefined' && $cm_zone) {
						$cm_zone.html(App._common.render(App._common.templates.items.CPU_MEM_ZONE, {
							orient : App.orient
						}));
					}
					$ld_zone = $('#local-disk-zone');
					if (typeof $ld_zone != 'undefined' && $ld_zone) {
						$ld_zone.html(App._common.render(App._common.templates.items.LOCAL_DISK_ZONE, {
							orient : App.orient
						}));
						$ld_zone.css("margin-left", (_s ? "-15px" : "0"));
					}
					$div_l = $('#table-file-system');
					if ($div_l) {
						$div_l.css("width", ((_s ? (_h - 0.8 * 0.7 * _w) : _w) - 30) + 'px').css('margin-left', "-15px");
					}
					var _oa = App.charts.option.area;
					if (typeof _oa == 'undefined') {
						_oa = {
							backgroundColor : 'transparent',
							areaOpacity : 0.2,
							titlePosition : 'in',
							legend : {
								position : 'none'
							},
							lineWidth : 1,
							vAxis : {
								maxValue : 100,
								minValue : 0
							},
							chartArea : {
								top : 20,
								left : 35
							}
						};
					}
					_oa.width = _s ? (_h - 0.55 * _w) : (0.9 * _w);
					_oa.height = (_s ? 0.5 : 0.4) * (0.9 * _w);
					_oa.chartArea.height = (_s ? 0.35 : 0.3) * (0.9 * _w);
					_oa.chartArea.width = 0.8 * (_s ? (_h - 0.5 * _w) : (0.9 * _w));
					App.charts.option.area = _oa;
				}
			},

			drawTableFileSystem : function() {
				var div = document.getElementById('table-file-system');
				if (div) {
					if (typeof App._collections._filesystems != 'undefined' && App._collections._filesystems.length > 0) {
						if (App._collections._filesystems.length == 1 && typeof App._collections._filesystems.at(0).get('name') == 'undefined'
								&& typeof App._collections._filesystems.at(0).get('size') == 'undefined' && typeof App._collections._filesystems.at(0).get('used') == 'undefined') {
							// mare sure table will not draw when have no data
							return;
						}
						_log.log("Filesystem length: " + App._collections._filesystems.length);
						var data = new google.visualization.DataTable();
						var options = App.charts.option.tableFs;
						data.addColumn(App.charts.columnType.STRING, "");
						data.addColumn(App.charts.columnType.STRING, "Name");
						data.addColumn(App.charts.columnType.NUMBER, "Used");
						data.addColumn(App.charts.columnType.NUMBER, "Size");
						App._collections._filesystems.each(function(model) {
							var name = (model.get('name') && model.get('name').length > 0) ? model.get('name') : 'N/A';
							var type = model.get('type');
							var icon = '<img src="images/icon/';
							if (name.toLowerCase().indexOf('c:') != -1) {
								icon += 'system';
							} else if (type.toLowerCase().indexOf('rom') != -1) {
								icon += 'cd';
							} else {
								icon += 'local';
							}
							icon += "";
							icon += '-disk.png" width="24" height="24" style="display: block; margin-left: auto; margin-right: auto"/>';

							name += " (";
							name += type;
							name += ")";
							data.addRow([ icon, name, {
								v : model.get('used'),
								f : model.get('strUsed')
							}, {
								v : model.get('size'),
								f : model.get('strSize')
							} ]);
						});
						if (typeof App.charts.tableFileSystem == 'undefined' || !App.charts.tableFileSystem) {
							App.charts.tableFileSystem = new google.visualization.Table(div);
							google.visualization.events.addListener(App.charts.tableFileSystem, 'select', function(event) {
								if (typeof App.charts.tableFileSystem != 'undefined' && App.charts.tableFileSystem) {
									var selection = App.charts.tableFileSystem.getSelection();
									if (selection && selection.length > 0) {
										var row = selection[0].row;
										var currentPage = App._router.page;
										if (typeof currentPage != 'undefined' && currentPage) {
											currentPage.drawPieFileSystem(App._collections._filesystems.at(row));
										}
										_log.log(row);
									}

								}
							});
						}
						App.charts.tableFileSystem.draw(data, options);
						var currentPage = App._router.page;
						if (typeof currentPage != 'undefined') {
							currentPage.drawPieFileSystem(App._collections._filesystems.first());
						}
					}
				}
			},
			drawPieFileSystem : function(model) {
				var div = document.getElementById('pie-file-system');
				if (div) {
					if (typeof App.charts.pieFileSystem == 'undefined' || !App.charts.pieFileSystem) {
						App.charts.pieFileSystem = new google.visualization.PieChart(div);
					}
					var data = new google.visualization.DataTable();
					var options = App.charts.option.pieFs;
					data.addColumn(App.charts.columnType.STRING, "Task");
					data.addColumn(App.charts.columnType.NUMBER, "Memory");
					data.addRow([ 'Free space', {
						v : model.get('free'),
						f : model.get('strFree')
					} ]);
					data.addRow([ 'Used Space', {
						v : model.get('used'),
						f : model.get('strUsed')
					} ]);
					var name = (model.get('name') && model.get('name').length > 0) ? model.get('name') : 'N/A';
					name += " (";
					name += model.get('type');
					name += ")";
					options.title = name;
					App.charts.pieFileSystem.draw(data, options);
				}
			},
			drawGaugeCPU : function() {
				var div = document.getElementById('gauge-cpu');
				if (div) {
					if (typeof App._collections._cpus != 'undefined' && App._collections._cpus.length > 0) {
						if (!App._collections._cpus.isValid()) {
							// mare sure table will not draw when have no data
							return;
						}
						var data = google.visualization.arrayToDataTable([ [ 'Label', 'Value' ], [ 'CPU', App._collections._cpus.last().get('usage') ] ]);
						if (!App.charts.gaugeCPU || !App.refreshCPU) {
							_log.log("Init gauge CPU");
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
						if (!App._collections._mems.isValid()) {
							// mare sure table will not draw when have no data
							return;
						}
						var data = google.visualization.arrayToDataTable([ [ 'Label', 'Value' ], [ 'Memory', App._collections._mems.last().get('usage') ] ]);
						if (!App.charts.gaugeMem || !App.refreshMem) {
							_log.log("Init gauge Mem");
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
					if (!App._collections._cpus.isValid()) {
						// mare sure table will not draw when have no data
						return;
					}
					var data = new google.visualization.DataTable();
					data.addColumn(App.charts.columnType.STRING, "Title");
					data.addColumn(App.charts.columnType.NUMBER, "Usage");
					App._collections._cpus.each(function(model) {
						data.addRow([ '', model.get('usage') ]);
					});
					App.charts.areaCPU = new google.visualization.AreaChart(div);
					App.charts.option.area.title = 'CPU usage';
					App.charts.areaCPU.draw(data, App.charts.option.area);
				}
			},
			drawAreaMem : function() {
				var div = document.getElementById('area-mem');
				if (div) {
					if (!App._collections._mems.isValid()) {
						// mare sure table will not draw when have no data
						return;
					}
					var data = new google.visualization.DataTable();
					data.addColumn(App.charts.columnType.STRING, "Title");
					data.addColumn(App.charts.columnType.NUMBER, "Usage");
					App._collections._mems.each(function(model) {
						data.addRow([ '', model.get('usage') ]);
					});
					App.charts.areaMem = new google.visualization.AreaChart(div);
					App.charts.option.area.title = 'Memory usage (RAM)';
					App.charts.areaMem.draw(data, App.charts.option.area);
				}
			},
			drawPieJVM : function() {
				var div = document.getElementById('pie-jvm');
				if (div) {
					if (typeof App._models._jvm != 'undefined' && typeof App._models._jvm.get('message') == 'undefined') {
						var data = new google.visualization.DataTable();
						var options = App.charts.option.pie;
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
						App.charts.option.pie.title = 'Total ' + App._models._jvm.get('strTotalMemory') + " of " + App._models._jvm.get('strMaxMemory');
						App.charts.pieJVM = new google.visualization.PieChart(div);
						App.charts.pieJVM.draw(data, options);
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
			renderIssue : function() {
				$ul = $('#system-issues');
				if (typeof $ul != 'undefined' && $ul) {
					var issues = App._collections._issues;
					if (typeof issues != 'undefined' && issues.length > 0) {
						if (issues.length == 1 && typeof issues.at(0).get('name') == 'undefined' && typeof issues.at(0).get('timeStamp') == 'undefined') {
							// mare sure table will not draw when have no data
							return;
						}
						_log.log("Issue length: " + issues.length);
						var temp = '';
						if (App.isTouchDevice) {
							var count = 0;
							var l = App._common.ISSUE_RECEIVE_LENGTH;
							var _c = 0;
							issues.each(function(issue) {
								if (count < l) {
									temp += App._common.render(App._common.templates.items.SYSTEM_ISSUE, issue.toJSON());
									_c++;
								}
								count++;
							});
							_log.log("Count: " + _c + " / " + count);
							while (_c > 0) {
								issues.remove(issues.at(0));
								_c--;
							}
						} else {
							issues.each(function(issue) {
								temp += App._common.render(App._common.templates.items.SYSTEM_ISSUE, issue.toJSON());
							});
						}
						_log.log("Issue length: " + issues.length);
						$ul.append(temp).listview("refresh");
					}
				}
			},
			renderForm : function() {
				var item = App._router.page.getItem();
				var currentPage = App._router.page;
				var _w = $(window).width();
				var _h = $(window).height();
				var width = _w;
				var height = _h;
				if (typeof App.orient == 'undefined') {
					App.orient = _w > _h ? 90 : 0;
				}
				switch (App.orient) {
				case 90:
				case -90:
					width = (_w <= _h) ? _h : _w;
					height = (_w <= _h) ? _w : _h;
					break;
				case 0:
				default:
					break;
				}
				_log.log("Screen width: " + width + " | height: " + height);
				currentPage.initChart(width, height);
				if (item == App._common.method.items.DETAIL) {
					currentPage.drawTableService();
					currentPage.drawPieJVM();

					App.refreshMem = false;
					App.refreshCPU = false;
					currentPage.drawGaugeCPU();
					currentPage.drawGaugeMem();
					currentPage.drawAreaCPU();
					currentPage.drawAreaMem();
					currentPage.drawTableFileSystem();
					if (typeof App._common.IS_REFRESH != 'undefined' && App._common.IS_REFRESH == true) {
						if (typeof App.handler.ref != 'undefined' || App.handler.ref != 0) {
							clearInterval(App.handler.ref);
							App.handler.ref = 0;
						}
						App.handler.ref = setInterval(App.intervalFunc, App._common.REFRESH_TIME);
					}
				} else if (item == App._common.method.items.STATISTIC) {
					currentPage.renderIssue();
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
				transition : App._common.page.transitions.SLIDE
			}
		};
		this.currentPage = App._common.page.PAGE_SPLASH;
		this.clearChart = function() {
			var charts = App.charts;
			if (charts.gaugeCPU) {
				charts.gaugeCPU.clearChart();
				charts.gaugeCPU = null;
			}
			if (charts.gaugeMem) {
				charts.gaugeMem.clearChart();
				charts.gaugeMem = null;
			}
			if (charts.tableService) {
				charts.tableService.clearChart();
				charts.tableService = null;
			}
			if (charts.areaCPU) {
				charts.areaCPU.clearChart();
				charts.areaCPU = null;
			}
			if (charts.pieJVM) {
				charts.pieJVM.clearChart();
				charts.pieJVM = null;
			}
			if (charts.areaMem) {
				charts.areaMem.clearChart();
				charts.areaMem = null;
			}
			if (charts.tableFileSystem) {
				charts.tableFileSystem.clearChart();
				charts.tableFileSystem = null;
			}
			if (charts.pieFileSystem) {
				charts.pieFileSystem.clearChart();
				charts.pieFileSystem = null;
			}
		};

		this.gotPullDownData = function(event, data) {
			_log.log("Handle pull down");
			data.iscrollview.refresh();
		};

		this.gotPullUpData = function(event, data) {
			_log.log("Handle pull up");
			$ul = ('#system-issues');
			if ($ul) {
				var $l = $('#system-issues > li:last-child');
				App._router.page.renderIssue();
				var iscrollview = data.iscrollview;
				iscrollview.refresh(null, null, $.proxy(function afterRefreshCallback(iscrollview) {
					this.scrollToElement($l.next(), 500);
				}, iscrollview));
			}
		};

		this.onPullDown = function(event, data) {
			setTimeout(function fakeRetrieveDataTimeout() {
				App._router.gotPullDownData(event, data);
			}, 1500);
		};

		this.onPullUp = function(event, data) {
			setTimeout(function fakeRetrieveDataTimeout() {
				App._router.gotPullUpData(event, data);
			}, 1500);
		};
		this.showLoading = function(b) {
			if (typeof $.mobile != 'undefined') {
				setTimeout(function() {
					$.mobile.loading(b ? 'show' : 'hide');
				}, 0);
			}
		}, this.router = Backbone.Router.extend({
			routes : {
				"" : "splash",
				"dashboard" : "dashboard",
				"about" : "about",
				"help" : "help",
				"logout" : "logout",
				"administration" : "administration",
				"dashboard/system/:item/:id" : "systemDetail"
			},

			initialize : function() {
				// var self = this;
				this.firstPage = true;
			},

			dashboard : function() {
				App._router.showLoading(true);
				var options = {};
				if (App._router.currentPage == App._common.page.PAGE_LOGOUT || App._router.currentPage == App._common.page.PAGE_ADMINISTRATION) {
					options = {
						transition : App._common.page.transitions.SLIDE
					};
				}
				if (App._router.currentPage == App._common.page.PAGE_ABOUT || App._router.currentPage == App._common.page.PAGE_SYSTEM_DETAIL || App._router.currentPage == App._common.page.PAGE_HELP) {
					options = {
						transition : App._common.page.transitions.SLIDE,
						reverse : true
					};
				}
				App._router.currentPage = App._common.page.PAGE_DASHBOARD;
				this.changePage(new App._views.DashBoardView(), options);
			},

			splash : function() {
				App._router.showLoading(true);
				self = this;
				if (App._common.IS_FINISH_LOAD) {
					this.dashboard();
				} else {
					App._router.currentPage = App._common.page.PAGE_SPLASH;
					this.changePage(new App._views.SplashView(), {
						transition : App._common.page.transitions.SLIDE_FADE
					});
					setTimeout(App._router._instance.hideSplash, App._common.SPLASH_TIMEOUT);
				}
			},

			help : function() {
				App._router.showLoading(true);
				App._router.currentPage = App._common.page.PAGE_HELP;
				this.changePage(new App._views.HelpView(), {
					transition : App._common.page.transitions.SLIDE
				});
			},

			about : function() {
				App._router.showLoading(true);
				var options = {};
				options.transition = App._common.page.transitions.SLIDE;
				if (App._router.currentPage == App._common.page.PAGE_HELP) {
					options.reverse = true;
				}
				App._router.currentPage = App._common.page.PAGE_ABOUT;
				this.changePage(new App._views.AboutView(), options);
			},

			logout : function() {
				App._router.showLoading(true);
				var options = {};
				if (App._router.currentPage == App._common.page.PAGE_DASHBOARD) {
					options = {
						transition : App._common.page.transitions.SLIDE,
						reverse : true
					};
				}
				App._router.currentPage = App._common.page.PAGE_LOGOUT;
				this.changePage(new App._views.LogoutView(), options);
			},

			administration : function() {
				App._router.showLoading(true);
				App._router.currentPage = App._common.page.PAGE_ADMINISTRATION;
				this.changePage(new App._views.AdministrationView(), {
					transition : App._common.page.transitions.SLIDE
				});
			},

			systemDetail : function(item, id) {
				App._router.showLoading(true);
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
					var role = App.currentUser.get("role");
					App._router.destinationSplash.currentPage = (role == App._common.roles.GUEST) ? App._common.page.PAGE_LOGOUT : App._router.currentPage;
					App._router.destinationSplash.page = (role == App._common.roles.GUEST) ? (new App._views.LogoutView()) : page;
					this.splash();
					return;
				}
				if (typeof App.handler.ref != 'undefined' || App.handler.ref != 0) {
					clearInterval(App.handler.ref);
					App.handler.ref = 0;
				}
				App._router.clearChart();
				$(page.el).attr('data-role', 'page');
				if (App.isTouchDevice) {
					$(page.el).addClass('pull-page');
				}
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
				_log.log('Go to page ' + App._router.currentPage + ' with transaction ' + options.transition + ' reverse: ' + (options.reverse ? 'true' : 'false'));
				if (typeof $.mobile != 'undefined') {
					$.mobile.changePage($(page.el), options);
					$('.expandable-content').trigger('expand');
					if (App._router.currentPage == App._common.page.PAGE_SYSTEM_DETAIL) {
						App._router.page.renderForm();
					}
					var $sc = $('#splash-screen');
					if ($sc) {
						$sc.css('width', $(window).width()).css('height', $(window).height());
					}
					if (App._router.currentPage == App._common.page.PAGE_LOGOUT) {
						if (App.currentUser.get('role') == App._common.roles.GUEST) {
							var $btn = $('#btn-req-per');
							var $btnR = $('#btn-reset');
							var $form = $('#form-req-per');
							var $f = $("#_firstname");
							var $l = $("#_lastname");
							var $d = $("#_description");
							if ($btn && $form && $f && $l && $d && $btnR) {
								$btnR.live('click', function() {
									$f.val('');
									$l.val('');
									$d.val('');
								});
								$btn.live('click', function() {
									var fn = $f.val();
									var ln = $l.val();
									if (fn.toString().trim().length == 0 || ln.toString().trim().length == 0) {
										return false;
									}
									App.currentUser.set({
										firstName : fn,
										lastName : ln,
										description : $d.val(),
									});
									App.currentUser.save();
									$form.fadeOut('fast');
									App.requestPermission = true;
									return false;
								});
							}
						}
					}
				}
			}

		});
		this._instance = new this.router();
	};
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

	$(document).delegate("div.pull-page", "pageinit", function bindPullPagePullCallbacks(event) {
		$(".iscroll-wrapper", this).bind({
			iscroll_onpulldown : App._router.onPullDown,
			iscroll_onpullup : App._router.onPullUp
		});
	});

	$(document).bind("pagechange", function(e, data) {
		if (App._router) {
			App._router.showLoading(false);
		}
	});

}).call(this);
