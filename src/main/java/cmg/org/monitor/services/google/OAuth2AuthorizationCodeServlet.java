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

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.extensions.servlet.auth.oauth2.AbstractAuthorizationCodeServlet;

/**
 * OAuth2AuthorizationCodeServlet starts the OAuth2 flow for the demo application.
 * 
 * <ol>
 * <li>An unauthenticated user will be redirected to Util.GOOGLE_OAUTH2_AUTH_URI to begin
 * the authentication flow.</li>
 * <li>After remote authentication the user will be redirected to OAuth2CallbackServlet to
 * exchange the authentication code for an access token.</li>
 * <li>The user is then redirected back to the OAuth2CallbackServlet with the access token 
 * result.</li>
 * <li>Finally the user is redirected back to the home page.</li> 
 * </ol>
 *
 * @author Lee Denison
 */
public class OAuth2AuthorizationCodeServlet extends AbstractAuthorizationCodeServlet {
	
	private static final long serialVersionUID = 1L;

	/**
	 * If the user already has a valid credential held in the AuthorizationCodeFlow they
	 * are simply returned to the home page.
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.sendRedirect("/");
	}

	/**
	 * Returns the URI to redirect to with the authentication result.
	 */
	@Override
	protected String getRedirectUri(HttpServletRequest request)
			throws ServletException, IOException {
		return ConfigHelper.REDIRECT_URI;
	}

	/**
	 * Returns the HTTP session id as the identifier for the current user.  The users
	 * credentials are stored against this ID.
	 */
	@Override
	protected String getUserId(HttpServletRequest request)
			throws ServletException, IOException {
		return request.getSession(true).getId();
	}

	@Override
	protected AuthorizationCodeFlow initializeFlow() throws ServletException,
			IOException {
		return Util.getFlow();
	}

}
