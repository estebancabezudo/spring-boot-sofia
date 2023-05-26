package net.cabezudo.sofia.accounts;

import net.cabezudo.sofia.accounts.mappers.EntityToBusinessAccountMapper;
import net.cabezudo.sofia.accounts.persistence.AccountEntity;
import net.cabezudo.sofia.accounts.persistence.AccountRepository;
import net.cabezudo.sofia.accounts.persistence.AccountUserRelationEntity;
import net.cabezudo.sofia.users.SofiaUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class AccountManager {
  private @Resource AccountRepository accountRepository;
  private @Autowired EntityToBusinessAccountMapper entityToBusinessAccountMapper;

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

  public boolean ownsTheAccount(SofiaUser user, Account account) {
    if (account == null) {
      return false;
    }
    Account userAccount = user.getAccount();
    return userAccount.id() == account.id();
  }

  public Account get(int id) {
    AccountEntity accountEntity = accountRepository.get(id);
    return entityToBusinessAccountMapper.map(accountEntity);
  }
}
