package net.cabezudo.sofia.accounts.mappers;

import net.cabezudo.sofia.accounts.Account;
import net.cabezudo.sofia.accounts.persistence.AccountEntity;
import net.cabezudo.sofia.sites.SiteManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BusinessToEntityAccountMapper {
  private @Autowired SiteManager siteManager;

  public AccountEntity map(Account account) {
    if (account == null) {
      return null;
    }
    return new AccountEntity(account.getId(), account.getSite().getId(), account.getName());
  }
}
