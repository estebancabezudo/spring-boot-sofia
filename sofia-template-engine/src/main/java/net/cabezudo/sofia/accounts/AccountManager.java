package net.cabezudo.sofia.accounts;

import net.cabezudo.sofia.accounts.mappers.EntityToBusinessAccountMapper;
import net.cabezudo.sofia.accounts.persistence.AccountEntity;
import net.cabezudo.sofia.accounts.persistence.AccountRepository;
import net.cabezudo.sofia.accounts.persistence.AccountUserRelationEntity;
import net.cabezudo.sofia.sites.Site;
import net.cabezudo.sofia.sites.SiteManager;
import net.cabezudo.sofia.users.SofiaUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class AccountManager {
  private @Resource AccountRepository accountRepository;
  private @Autowired EntityToBusinessAccountMapper entityToBusinessAccountMapper;
  private @Autowired SiteManager siteManager;

  public AccountUserRelationEntity findRelation(Site site, Account account, SofiaUser user) {
    return accountRepository.find(account.getId(), user.getId(), site.getId());
  }

  public Accounts getAll(SofiaUser user) {
    Accounts accounts = new Accounts();
    List<AccountEntity> list = accountRepository.findAll(user.getId(), user.getAccount().getSite().getId());
    for (AccountEntity accountEntity : list) {
      Site site = siteManager.get(accountEntity.getSiteId());
      accounts.add(new Account(accountEntity.getId(), site, accountEntity.getName()));
    }
    return accounts;
  }

  public boolean ownsTheAccount(Account account, SofiaUser user) {
    if (account == null) {
      return false;
    }
    Account userAccount = user.getAccount();
    return userAccount.getId() == account.getId();
  }

  public Account get(int id) {
    AccountEntity accountEntity = accountRepository.get(id);
    return entityToBusinessAccountMapper.map(accountEntity);
  }
}
