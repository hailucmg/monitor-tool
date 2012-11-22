/**
 * Copyright (c) CMG Ltd All rights reserved.
 *
 * This software is the confidential and proprietary information of CMG
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with CMG.
 */

/**
 * Constant value & Common function *
 * 
 * @Creator Hai Lu
 * @author $Author$
 * @version $Revision$
 * @Last changed: $LastChangedDate$
 */

window.IS_DEBUG = true;

window.TEMP_DIR = '/m/templates';
window.PAGE_SPLASH = 'splash';
window.PAGE_DASHBOARD = 'dashboard';
window.PAGE_ABOUT = 'about';
window.PAGE_HELP = 'help';
window.PAGE_ADMINISTRATION = 'administration';
window.PAGE_LOGOUT = 'confirm-logout';
window.PAGE_SYSTEM_DETAIL = "system-detail";

/**
 * Write out the log on Browser console
 */
window.jLog = {
		ERROR : "error",
		WARNING : "warning",
		INFO : "info",
		log : function(data, level) {
			if (data && IS_DEBUG && typeof console!= 'undefined') {
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
		}
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
function render(tmpl_name, tmpl_data) {
    if ( !render.tmpl_cache ) { 
        render.tmpl_cache = {};
    }

    if ( ! render.tmpl_cache[tmpl_name] ) {     
        var tmpl_url = TEMP_DIR + '/' + tmpl_name + '.html';

        var tmpl_string;
        $.ajax({
            url: tmpl_url,
            method: 'GET',
            async: false,
            success: function(data) {
                tmpl_string = data;
            }
        });

        render.tmpl_cache[tmpl_name] = _.template(tmpl_string);
    }

    return render.tmpl_cache[tmpl_name](tmpl_data);
}