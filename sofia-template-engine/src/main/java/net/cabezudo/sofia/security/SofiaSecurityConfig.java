package net.cabezudo.sofia.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SofiaSecurityConfig {
  public static final String DEFAULT_LOGIN_PAGE = "/login.html";
  public static final String DEFAULT_LOGIN_WEB_SERVICE = "/v1/login";
  public static final String DEFAULT_LOGOUT_SUCCESS_URL = "/";
  public static final String DEFAULT_LOGIN_SUCCESS_URL = "/";
  private static final Logger log = LoggerFactory.getLogger(SofiaSecurityConfig.class);

}
