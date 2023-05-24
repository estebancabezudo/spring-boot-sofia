package net.cabezudo.sofia.accounts;

import net.cabezudo.sofia.accounts.persistence.AccountEntity;
import net.cabezudo.sofia.accounts.persistence.AccountRepository;
import net.cabezudo.sofia.accounts.persistence.AccountUserRelationEntity;
import net.cabezudo.sofia.users.SofiaUser;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class AccountManager {
  @Resource
  private AccountRepository accountRepository;

  public AccountUserRelationEntity findRelation(Account account, SofiaUser user) {
    return accountRepository.find(account.id(), user.getId());
  }

  public Accounts getAll(SofiaUser user) {
    Accounts accounts = new Accounts();
    List<AccountEntity> list = accountRepository.findAll(user.getId());
    for (AccountEntity accountEntity : list) {
      accounts.add(new Account(accountEntity.id(), accountEntity.siteId()));
    }
    return accounts;
  }
}
