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

window.PAGE_DASHBOARD = 'dashboard';
window.PAGE_ABOUT = 'about';
window.PAGE_HELP = 'help';
window.PAGE_ADMINISTRATION = 'administration';
window.PAGE_LOGOUT = 'confirm-logout';
window.PAGE_SYSTEM_DETAIL = "system-detail";

/**
 * Write out the log on Browser console
 * @param l the log content
 */
function log(l) {
	if (IS_DEBUG) {
		if (typeof console!= 'undefined') {
			console.log(l);
		} else {
			alert(l);
		}		
	}
}
/**
 * Render the HTML string with special name
 * @param tmpl_name the path and name of template
 * @param tmpl_data the data object to render
 * @returns
 */
function render(tmpl_name, tmpl_data) {
    if ( !render.tmpl_cache ) { 
        render.tmpl_cache = {};
    }

    if ( ! render.tmpl_cache[tmpl_name] ) {
        var tmpl_dir = '/static/templates';
        var tmpl_url = tmpl_dir + '/' + tmpl_name + '.html';

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