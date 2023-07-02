package net.cabezudo.sofia.security;

import net.cabezudo.sofia.accounts.Account;
import net.cabezudo.sofia.accounts.AccountManager;
import net.cabezudo.sofia.accounts.persistence.AccountUserRelationEntity;
import net.cabezudo.sofia.sites.Site;
import net.cabezudo.sofia.users.SofiaUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
public class PermissionManager {
  private final Logger logger = LoggerFactory.getLogger(PermissionManager.class);
  private final Map<Site, Permissions> permissionBySite = new TreeMap<>();
  private @Autowired AccountManager accountManager;

  public void add(Site site, Permission permission) {
    logger.debug("Add permission " + permission + " to site " + site.getName());
    Permissions permissions = permissionBySite.get(site);
    if (permissions == null) {
      permissions = new Permissions();
      permissionBySite.put(site, permissions);
    }
    permissions.add(permission);
  }

  public boolean hasPermission(Site site, Account account, SofiaUser user, String group, boolean isSecurityActive) {
    AccountUserRelationEntity relation = accountManager.findRelation(site, account, user);
    return user.hasPermission(group) || (relation != null && relation.getOwner());
  }

  public boolean hasPermission(SofiaUser user, Site site, Account account, String requestURI) {
    logger.debug("Try to authorize user");
    logger.debug("Actual user=" + user);
    logger.debug("Actual site=" + site);
    logger.debug("Actual account=" + account);
    logger.debug("Actual requestURI=" + requestURI);

    String username = user == null ? null : user.getUsername();

    if (accountManager.ownsTheAccount(user, account)) {
      return true;
    } else {
      logger.debug("The user DO NOT own the account.");
    }

    Collection<GrantedAuthority> authorities = user == null ? new ArrayList<>() : user.getAuthorities();

    Permissions sitePermissions = permissionBySite.get(site);
    if (sitePermissions == null) {
      logger.debug("The site DO NOT have permissions.");
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
        if (contains(authorities, grantPermission.getGroup()) && grantPermission.getUser().equals(Permission.USER_ALL) && grantPermission.getAccess().equals(Permission.ACCESS_GRANT)) {
          logger.debug("Authorized because the user is all, the group match and is granted for " + requestURI + ".");
          return true;
        }
        if (grantPermission.getGroup().equals(Permission.GROUP_ALL) && grantPermission.getUser().equals(username) && grantPermission.getAccess().equals(Permission.ACCESS_GRANT)) {
          logger.debug("Authorized because the user match, the group is all is granted for " + requestURI + ".");
          return true;
        }
        if (contains(authorities, grantPermission.getGroup()) && grantPermission.getUser().equals(username) && grantPermission.getAccess().equals(Permission.ACCESS_GRANT)) {
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
