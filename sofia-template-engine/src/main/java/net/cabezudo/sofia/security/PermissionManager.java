package net.cabezudo.sofia.security;

import net.cabezudo.sofia.accounts.Account;
import net.cabezudo.sofia.accounts.AccountManager;
import net.cabezudo.sofia.accounts.persistence.AccountUserRelationEntity;
import net.cabezudo.sofia.config.SofiaEnvironment;
import net.cabezudo.sofia.users.SofiaUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PermissionManager {
  private @Autowired SofiaEnvironment sofiaEnvironment;
  private @Autowired AccountManager accountManager;

  public boolean hasPermission(Account account, SofiaUser user, String group) {
    AccountUserRelationEntity relation = accountManager.findRelation(account, user);
    return user.hasPermission(group) || (relation != null && relation.owner());
  }
}
