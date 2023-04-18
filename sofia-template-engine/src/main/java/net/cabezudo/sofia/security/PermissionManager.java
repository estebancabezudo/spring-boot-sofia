package net.cabezudo.sofia.security;

import net.cabezudo.sofia.accounts.Account;
import net.cabezudo.sofia.accounts.AccountManager;
import net.cabezudo.sofia.config.SofiaEnvironment;
import net.cabezudo.sofia.users.SofiaUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PermissionManager {

  @Autowired
  private SofiaEnvironment sofiaEnvironment;

  @Autowired
  private AccountManager accountManager;

  public boolean hasPermission(Account account, SofiaUser user, String group) {
    if (sofiaEnvironment.isSecurityNotActive()) {
      return true;
    }
    if (user.hasPermission(group)) {
      return accountManager.accountContainsUser(account, user);
    }
    return false;
  }
}
