package cmg.org.monitor.services.google;

/*
 * Copyright (c) 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

import static javax.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringEscapeUtils;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.AuthorizationCodeResponseUrl;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.servlet.auth.oauth2.AbstractAuthorizationCodeCallbackServlet;

/**
 * A servlet which handles the OAuth 2.0 authentication result and retrieves an access
 * token on successful authentication. 
 * 
 * @author Jennifer Murphy
 * @author Lee Denison
 */
public class OAuth2CallbackServlet extends AbstractAuthorizationCodeCallbackServlet {

	private static final long serialVersionUID = 1L;
	
	private static final Logger LOG = Logger.getLogger(OAuth2CallbackServlet.class.getName());

	@Override
	protected void onSuccess(HttpServletRequest request, HttpServletResponse response, Credential credential)
	    throws ServletException, IOException {
		response.sendRedirect("/");
	}

	@Override
	protected void onError(
	    HttpServletRequest req, HttpServletResponse resp, AuthorizationCodeResponseUrl errorResponse)
	    throws ServletException, IOException {
		
            StringBuilder err = new StringBuilder("There was a problem during authentication: ");
            
            if (errorResponse != null) {
            	err.append(errorResponse.getError());
            
            	if (errorResponse.getErrorUri() != null) {
            		err.append(" [")
            		    .append(errorResponse.getErrorUri())
            		    .append("]");
            	}
            
            	if (errorResponse.getErrorDescription() != null) {
            		err.append(": ")
            			.append(errorResponse.getErrorDescription());
            	}
            }
            
            LOG.warning(err.toString());
	    resp.sendError(SC_INTERNAL_SERVER_ERROR, StringEscapeUtils.escapeHtml4(Util.stripTags(err.toString())));
	}

	@Override
	protected String getRedirectUri(HttpServletRequest request) throws ServletException, IOException {
		String redirectURL = request.getRequestURL().toString();
		if (redirectURL.contains("/index.jsp")) {
			redirectURL = redirectURL.substring(0,redirectURL.lastIndexOf("/"));
		}
		if (redirectURL.contains("/connect")) {
			redirectURL = redirectURL.substring(0,redirectURL.lastIndexOf("/"));
		}
		if (redirectURL.contains("/oauth2callback")) {
			redirectURL = redirectURL.substring(0,redirectURL.lastIndexOf("/"));
		}
	    //return ConfigHelper.REDIRECT_URI;
		redirectURL += "/oauth2callback";
		System.out.println("redirect URL: " + redirectURL);
		return redirectURL;
	}

	@Override
	protected AuthorizationCodeFlow initializeFlow() throws IOException {
	    return Util.getFlow();
	}

	@Override
	protected String getUserId(HttpServletRequest request) throws ServletException, IOException {
	    return  request.getSession(true).getId(); 
	}
	
}
