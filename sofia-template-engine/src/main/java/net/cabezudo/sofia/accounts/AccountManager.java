package net.cabezudo.sofia.accounts;

import net.cabezudo.sofia.accounts.persistence.AccountEntity;
import net.cabezudo.sofia.accounts.persistence.AccountRepository;
import net.cabezudo.sofia.users.SofiaUser;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class AccountManager {
  @Resource
  private AccountRepository accountRepository;

  public boolean accountContainsUser(Account account, SofiaUser user) {
    return accountRepository.find(account.id(), user.getId()) != null;
  }

  public Accounts getAll(SofiaUser user) {
    Accounts accounts = new Accounts();
    List<AccountEntity> list = accountRepository.findAll(user.getId());
    for (AccountEntity accountEntity : list) {
      accounts.add(new Account(accountEntity.getId(), accountEntity.getSiteId()));
    }
    return accounts;
  }
}
