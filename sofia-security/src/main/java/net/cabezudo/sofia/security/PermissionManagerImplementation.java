package net.cabezudo.sofia.security;

import net.cabezudo.sofia.sites.Site;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class PermissionManagerImplementation {
  private static final PermissionManagerImplementation INSTANCE = new PermissionManagerImplementation();
  private final Logger logger = LoggerFactory.getLogger(PermissionManagerImplementation.class);
  private final Map<Site, Permissions> permissionBySite = new TreeMap<>();

  public static PermissionManagerImplementation getInstance() {
    return INSTANCE;
  }

  public void add(Site site, Permission permission) {
    logger.debug("Add permission " + permission + " to site " + site.getName());
    Permissions permissions = permissionBySite.get(site);
    if (permissions == null) {
      permissions = new Permissions();
      permissionBySite.put(site, permissions);
    }
    permissions.add(permission);
  }

  public boolean authorize(Site site, String requestURI, String username, Collection<? extends GrantedAuthority> authorities) {
    logger.debug("Authorize for " + username + ", " + authorities + " in " + site.getName() + requestURI);

    Permissions sitePermissions = permissionBySite.get(site);
    if (sitePermissions == null) {
      return false;
    }
    // TODO Improve this search
    List<Permission> permissions = sitePermissions.get(requestURI);

    for (Permission permission : permissions) {
      logger.debug("Try to deny with " + permission);
      if (permission.getUser().equals(Permission.USER_ALL) && permission.getGroup().equals(Permission.GROUP_ALL) && permission.getAccess().equals(Permission.ACCESS_DENY)) {
        logger.debug("Deny because group and user are all and is granted for " + requestURI + ".");
        return false;
      }
      if (permission.getUser().equals(Permission.USER_ALL) && authorities.contains(permission.getGroup()) && permission.getAccess().equals(Permission.ACCESS_DENY)) {
        for (GrantedAuthority authority : authorities) {
          if (authority.getAuthority().equals(permission.getGroup())) {
            logger.debug("Deny because the user is all, the group match and is granted for " + requestURI + ".");
            return false;
          }
        }
      }
      if (permission.getGroup().equals(Permission.GROUP_ALL) && permission.getUser().equals(username) && permission.getAccess().equals(Permission.ACCESS_DENY)) {
        logger.debug("Deny because the user match, the group is all is granted for " + requestURI + ".");
        return false;
      }
      if (permission.getUser().equals(username) && contains(authorities, permission.getGroup()) && permission.getAccess().equals(Permission.ACCESS_DENY)) {
        for (GrantedAuthority authority : authorities) {
          if (authority.getAuthority().equals(permission.getGroup())) {
            logger.debug("Deny because the user is all, the group match and is granted for " + requestURI + ".");
            return false;
          }
        }
      }
      for (Permission grantPermission : permissions) {
        logger.debug("Try to authorize with " + grantPermission);
        if (grantPermission.getUser().equals(Permission.USER_ALL) && grantPermission.getGroup().equals(Permission.GROUP_ALL) && grantPermission.getAccess().equals(Permission.ACCESS_GRANT)) {
          logger.debug("Authorized because group and user are all and is granted for " + requestURI + ".");
          return true;
        }
        logger.debug(grantPermission.getUser() + " " + authorities + " contain(" + grantPermission.getGroup() + ")=" + authorities.contains(grantPermission.getGroup()) + " " + grantPermission.getAccess());
        if (grantPermission.getUser().equals(Permission.USER_ALL) && contains(authorities, grantPermission.getGroup()) && grantPermission.getAccess().equals(Permission.ACCESS_GRANT)) {
          logger.debug("Authorized because the user is all, the group match and is granted for " + requestURI + ".");
          return true;
        }
        if (grantPermission.getGroup().equals(Permission.GROUP_ALL) && grantPermission.getUser().equals(username) && grantPermission.getAccess().equals(Permission.ACCESS_GRANT)) {
          logger.debug("Authorized because the user match, the group is all is granted for " + requestURI + ".");
          return true;
        }
        if (grantPermission.getUser().equals(username) && contains(authorities, grantPermission.getGroup()) && grantPermission.getAccess().equals(Permission.ACCESS_GRANT)) {
          logger.debug("Authorized because the user is all, the group match and is granted for " + requestURI + ".");
          return true;
        }
        return false;
      }
    }
    return false;
  }

  private boolean contains(Collection<? extends GrantedAuthority> authorities, String role) {
    for (GrantedAuthority authority : authorities) {
      if (authority.getAuthority().equals(role)) {
        return true;
      }
    }
    return false;
  }
}
