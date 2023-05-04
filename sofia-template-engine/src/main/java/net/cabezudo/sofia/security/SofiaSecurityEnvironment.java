package net.cabezudo.sofia.security;

import net.cabezudo.sofia.sites.SiteNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SofiaSecurityEnvironment { //implements SofiaLoadConfigurationFileYAMLData {
  public static final String DEFAULT_LOGIN_PAGE = "/login.html";
  public static final String DEFAULT_LOGIN_WEB_SERVICE = "/v1/login";
  public static final String DEFAULT_LOGOUT_SUCCESS_URL = "/";
  private static final Logger log = LoggerFactory.getLogger(SofiaSecurityEnvironment.class);

  private void addPermissions() throws SiteNotFoundException {
//    log.debug("Create permissions with data in configuration file.");
//    List<ConfigurationFileYAMLSiteData> sites = configurationFileYAMLData.getSites();
//    for (ConfigurationFileYAMLSiteData configurationFileYAMLSiteData : sites) {
//      Site site = siteManager.get(configurationFileYAMLSiteData.getName());
//      log.debug("Site: " + site.getName());
//      List<String> permissions = configurationFileYAMLSiteData.getPermissions();
//      if (permissions != null) {
//        for (String permissionData : permissions) {
//          Permission permission = new Permission(permissionData);
//          PermissionManager.getInstance().add(site, permission);
//          log.debug("Add " + permissionData + " to site " + site.getName());
//        }
//      }
//    }
  }

}
