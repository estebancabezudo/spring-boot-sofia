package net.cabezudo.sofia.security;

import net.cabezudo.sofia.accounts.persistence.AccountUserRelationEntity;
import net.cabezudo.sofia.accounts.service.Account;
import net.cabezudo.sofia.accounts.service.AccountManager;
import net.cabezudo.sofia.sites.Site;
import net.cabezudo.sofia.users.persistence.GroupEntity;
import net.cabezudo.sofia.users.persistence.GroupsRepository;
import net.cabezudo.sofia.users.service.Group;
import net.cabezudo.sofia.users.service.SofiaUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Service
public class PermissionManager {
  private final Logger logger = LoggerFactory.getLogger(PermissionManager.class);
  private final Map<Site, Permissions> permissionBySite = new TreeMap<>();
  private @Autowired AccountManager accountManager;
  private @Autowired GroupsRepository groupsRepository;

  public void add(Site site, Permission permission) {
    logger.debug("Add permission " + permission + " to site " + site.getName());
    Permissions permissions = permissionBySite.get(site);
    if (permissions == null) {
      permissions = new Permissions();
      permissionBySite.put(site, permissions);
    }
    permissions.add(permission);
  }

  public boolean hasPermission(Site site, Account account, SofiaUser user, boolean isSecurityActive, String... groups) {
    if (!isSecurityActive || site == null || account == null || user == null || groups == null || groups.length == 0) {
      return false;
    }
    List<GroupEntity> databaseGroups = groupsRepository.get(account.getId(), user.getId());
    // TODO Improve this
    for (GroupEntity groupEntity : databaseGroups) {
      for (String group : groups) {
        if (group.equals(groupEntity.getName())) {
          return true;
        }
      }
    }
    AccountUserRelationEntity relation = accountManager.findRelation(site, account, user);
    return relation != null && relation.getOwner();
  }

  public boolean hasPermission(SofiaUser user, Site site, Account account, String requestURI) {
    logger.debug("Try to authorize user");
    logger.debug("Actual user=" + user);
    logger.debug("Actual site=" + site);
    logger.debug("Actual account=" + account);
    logger.debug("Actual requestURI=" + requestURI);

    String username = user == null ? null : user.getUsername();

    if (user != null && account != null && accountManager.ownsTheAccount(user, account)) {
      return true;
    } else {
      logger.debug("The user DO NOT own the account.");
    }

    List<String> groups;
    if (account != null && user != null) {
      List<GroupEntity> groupsEntity = groupsRepository.get(account.getId(), user.getId());
      groups = groupsEntity.stream().map(GroupEntity::getName).collect(Collectors.toList());
    } else {
      groups = new ArrayList<>();
    }

    Permissions sitePermissions = permissionBySite.get(site);
    if (sitePermissions == null) {
      logger.debug("The site DO NOT have permissions.");
      return false;
    }
    // TODO Improve this search
    List<Permission> permissions = sitePermissions.get(requestURI);

    for (Permission permission : permissions) {
      logger.debug("Try to pass a logged user ");
      if (user != null && permission.getGroupName().equals(Group.USER)) {
        logger.debug("There is a user with the group " + permission.getGroupName());
        return true;
      }
      logger.debug("Try to deny with " + permission);
      if (permission.getUser().equals(Permission.USER_ALL) && permission.getGroupName().equals(Permission.GROUP_ALL) && permission.getAccess().equals(Permission.ACCESS_DENY)) {
        logger.debug("Deny because group and user are all and is granted for " + requestURI + ".");
        return false;
      }
      if (permission.getUser().equals(Permission.USER_ALL) && groups.contains(permission.getGroupName()) && permission.getAccess().equals(Permission.ACCESS_DENY)) {
        for (String groupName : groups) {
          if (groupName.equals(permission.getGroupName())) {
            logger.debug("Deny because the user is all, the group match and is granted for " + requestURI + ".");
            return false;
          }
        }
      }
      if (permission.getGroupName().equals(Permission.GROUP_ALL) && permission.getUser().equals(username) && permission.getAccess().equals(Permission.ACCESS_DENY)) {
        logger.debug("Deny because the user match, the group is all is granted for " + requestURI + ".");
        return false;
      }
      if (permission.getUser().equals(username) && contains(groups, permission.getGroupName()) && permission.getAccess().equals(Permission.ACCESS_DENY)) {
        for (String groupName : groups) {
          if (groupName.equals(permission.getGroupName())) {
            logger.debug("Deny because the user is all, the group match and is granted for " + requestURI + ".");
            return false;
          }
        }
      }
      for (Permission grantPermission : permissions) {
        logger.debug("Try to authorize with " + grantPermission);
        if (grantPermission.getUser().equals(Permission.USER_ALL) && grantPermission.getGroupName().equals(Permission.GROUP_ALL) && grantPermission.getAccess().equals(Permission.ACCESS_GRANT)) {
          logger.debug("Authorized because group and user are all and is granted for " + requestURI + ".");
          return true;
        }
        logger.debug(grantPermission.getUser() + " " + groups + " contain(" + grantPermission.getGroupName() + ")=" + groups.contains(grantPermission.getGroupName()) + " " + grantPermission.getAccess());
        if (contains(groups, grantPermission.getGroupName()) && grantPermission.getUser().equals(Permission.USER_ALL) && grantPermission.getAccess().equals(Permission.ACCESS_GRANT)) {
          logger.debug("Authorized because the user is all, the group match and is granted for " + requestURI + ".");
          return true;
        }
        if (grantPermission.getGroupName().equals(Permission.GROUP_ALL) && grantPermission.getUser().equals(username) && grantPermission.getAccess().equals(Permission.ACCESS_GRANT)) {
          logger.debug("Authorized because the user match, the group is all is granted for " + requestURI + ".");
          return true;
        }
        if (contains(groups, grantPermission.getGroupName()) && grantPermission.getUser().equals(username) && grantPermission.getAccess().equals(Permission.ACCESS_GRANT)) {
          logger.debug("Authorized because the user is all, the group match and is granted for " + requestURI + ".");
          return true;
        }
        logger.debug("No permission for: " + grantPermission);
        return false;
      }
    }
    logger.debug("No permission for: " + requestURI);
    return false;
  }

  private boolean contains(Collection<String> groupsNames, String name) {
    for (String groupName : groupsNames) {
      if (groupName.equals(name)) {
        return true;
      }
    }
    return false;
  }
}
