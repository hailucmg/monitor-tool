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
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Assorted utility methods.
 *
 * @author Will Norris
 * @author Jenny Murphy
 */
public class ConfigHelper {
  private static final Logger log = Logger.getLogger(ConfigHelper.class.getName());
  private static final String CONFIG_PROPERTIES = "/config.properties";
  public static final Properties config = getConfig();

  // OAuth client ID
  public static String CLIENT_ID = ConfigHelper.getProperty("oauth_client_id");

  // OAuth client secret
  public static String CLIENT_SECRET = ConfigHelper.getProperty("oauth_client_secret");

  // OAuth client secret
  public static String GOOGLE_API_KEY = ConfigHelper.getProperty("google_api_key");

  // Space separated list of OAuth scopes
  public static String SCOPES = ConfigHelper.getProperty("oauth_scopes");

  // OAuth redirect URI
  public static String REDIRECT_URI = ConfigHelper.getProperty("oauth_redirect_uri");


  /**
   * Load the configuration file for this application.
   *
   * @return application configuration properties
   */
  private static Properties getConfig() {
	InputStream input = Util.class.getResourceAsStream(CONFIG_PROPERTIES);
    Properties config = new Properties();
    try {
      config.load(input);
    } catch (IOException e) {
      throw new RuntimeException("Unable to load config file: " + CONFIG_PROPERTIES);
    }
    return config;
  }

  /**
   * A simple static helper method that fetches a configuration
   *
   * @param key The name of the property for which you would like the configured
   *            value
   * @return A String representation of the configured property value
   * @throws RuntimeException if request property can not be found
   */
  public static String getProperty(String key) {
    if (!config.containsKey(key) || config.getProperty(key).trim().isEmpty()) {
      log.severe("Could not find property " + key);
      throw new RuntimeException("Could not find property " + key);
    }
    return config.getProperty(key).trim();
  }
}
