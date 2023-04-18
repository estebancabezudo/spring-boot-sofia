package net.cabezudo.sofia.config;

import net.cabezudo.html.nodes.Attribute;
import net.cabezudo.html.nodes.Element;
import net.cabezudo.html.nodes.TagName;
import net.cabezudo.sofia.core.modules.ModuleException;
import net.cabezudo.sofia.core.modules.SofiaSecurityModule;
import net.cabezudo.sofia.creator.Caller;
import net.cabezudo.sofia.creator.ContentManager;
import net.cabezudo.sofia.security.Permission;
import net.cabezudo.sofia.security.PermissionManagerImplementation;
import net.cabezudo.sofia.sites.Site;
import net.cabezudo.sofia.sites.SiteManager;
import net.cabezudo.sofia.sites.SiteNotFoundException;
import net.cabezudo.sofia.users.Group;
import net.cabezudo.sofia.users.Groups;
import net.cabezudo.sofia.users.SofiaUser;
import net.cabezudo.sofia.users.rest.BusinessToRestUserMapper;
import net.cabezudo.sofia.users.rest.RestUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.List;

@Component
public class SofiaSecurityModuleImplementation implements SofiaSecurityModule {
  private static final Logger log = LoggerFactory.getLogger(SofiaSecurityModuleImplementation.class);

  @Autowired
  ContentManager contentManager;

  @Autowired
  BusinessToRestUserMapper businessToRestUserMapper;

  @Override
  public void config(ConfigurationFileYAMLData configurationFileYAMLData, SiteManager siteManager) throws ConfigurationException {
    try {
      log.debug("Add permissions.");
      addPermissions(configurationFileYAMLData, siteManager);
    } catch (SiteNotFoundException e) {
      throw new ConfigurationException(e);
    }
  }

  private void addPermissions(ConfigurationFileYAMLData configurationFileYAMLData, SiteManager siteManager) throws SiteNotFoundException {
    log.debug("Create permissions with data in configuration file.");
    List<ConfigurationFileYAMLSiteData> sites = configurationFileYAMLData.getSites();
    for (ConfigurationFileYAMLSiteData siteData : sites) {
      Site site = siteManager.get(siteData.getName());
      log.debug("Site: " + site.getName());

      List<String> apis = siteData.getAPIs();
      apis.add("/logout");
      for (String api : apis) {
        contentManager.add(api);
        String permissionString = "all:all:grant:" + api + "/**";
        Permission permission = new Permission(permissionString);
        PermissionManagerImplementation.getInstance().add(site, permission);
        log.debug("Add " + permissionString + " to site " + site.getName());
      }

      List<String> permissions = siteData.getPermissions();
      for (String permissionData : permissions) {
        Permission permission = new Permission(permissionData);
        PermissionManagerImplementation.getInstance().add(site, permission);
        log.debug("Add " + permissionData + " to site " + site.getName());
      }

    }
  }

  public void processElement(Site site, Path requestFilePath, Element element, Caller caller, Path voidRootFilePath) throws ModuleException {
    if (TagName.BODY.equals(element.getTagName())) {
      processBodyTag(site, requestFilePath, element, caller, voidRootFilePath);
    }
  }

  @Override
  public RestUser getUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication instanceof UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) {
      SofiaUser sofiaUser = (SofiaUser) usernamePasswordAuthenticationToken.getPrincipal();
      return businessToRestUserMapper.map(sofiaUser);
    }
    return null;
  }

  private void processBodyTag(Site site, Path requestFilePath, Element body, Caller caller, Path voidRootFilePath) throws ModuleException {
    Attribute attribute;
    if ((attribute = body.getAttribute(Attribute.GROUPS)) != null) {
      Groups groups = new Groups(attribute.getValue());
      if (groups.isEmpty()) {
        log.debug("No groups found in tag");
      } else {
        for (Group group : groups) {
          log.debug("Found group: " + group.name());
          String fileResource = '/' + requestFilePath.toString();
          Permission filePermission = new Permission(group.name(), Permission.USER_ALL, Permission.ACCESS_GRANT, fileResource);
          PermissionManagerImplementation.getInstance().add(site, filePermission);
          String textsResource = "/texts/" + voidRootFilePath.toString() + "/**";
          Permission textsPermission = new Permission(group.name(), Permission.USER_ALL, Permission.ACCESS_GRANT, textsResource);
          PermissionManagerImplementation.getInstance().add(site, textsPermission);
        }
      }
    } else {
      log.debug("No groups tag found");
    }
  }
}
