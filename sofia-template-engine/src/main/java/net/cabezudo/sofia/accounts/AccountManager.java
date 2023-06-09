package net.cabezudo.sofia.accounts;

import net.cabezudo.sofia.accounts.mappers.EntityToBusinessAccountMapper;
import net.cabezudo.sofia.accounts.persistence.AccountEntity;
import net.cabezudo.sofia.accounts.persistence.AccountRepository;
import net.cabezudo.sofia.accounts.persistence.AccountUserRelationEntity;
import net.cabezudo.sofia.sites.Site;
import net.cabezudo.sofia.users.SofiaUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class AccountManager {
  private @Resource AccountRepository accountRepository;
  private @Autowired EntityToBusinessAccountMapper entityToBusinessAccountMapper;

  public AccountUserRelationEntity findRelation(Site site, Account account, SofiaUser user) {
    return accountRepository.find(site, account.id(), user.getId());
  }

  public Accounts getAll(Site site, SofiaUser user) {
    Accounts accounts = new Accounts();
    List<AccountEntity> list = accountRepository.findAll(site, user.getId());
    for (AccountEntity accountEntity : list) {
      accounts.add(new Account(accountEntity.id(), accountEntity.siteId()));
    }
    return accounts;
  }

  public boolean ownsTheAccount(Site site, Account account, SofiaUser user) {
    if (account == null) {
      return false;
    }
    Account userAccount = user.getAccount();
    return userAccount.id() == account.id();
  }

  public Account get(Site site, int id) {
    AccountEntity accountEntity = accountRepository.get(site, id);
    return entityToBusinessAccountMapper.map(accountEntity);
  }
}
