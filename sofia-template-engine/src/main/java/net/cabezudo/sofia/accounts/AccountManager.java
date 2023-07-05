package net.cabezudo.sofia.accounts;

import net.cabezudo.sofia.accounts.mappers.EntityToBusinessAccountListMapper;
import net.cabezudo.sofia.accounts.mappers.EntityToBusinessAccountMapper;
import net.cabezudo.sofia.accounts.persistence.AccountEntity;
import net.cabezudo.sofia.accounts.persistence.AccountRepository;
import net.cabezudo.sofia.accounts.persistence.AccountUserRelationEntity;
import net.cabezudo.sofia.core.persistence.EntityList;
import net.cabezudo.sofia.sites.Site;
import net.cabezudo.sofia.sites.SiteManager;
import net.cabezudo.sofia.userpreferences.UserPreferencesManager;
import net.cabezudo.sofia.users.service.SofiaUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class AccountManager {
  private static final Logger log = LoggerFactory.getLogger(AccountManager.class);
  private @Resource AccountRepository accountRepository;
  private @Autowired EntityToBusinessAccountMapper entityToBusinessAccountMapper;
  private @Autowired SiteManager siteManager;
  private @Autowired UserPreferencesManager userPreferencesManager;
  private @Autowired EntityToBusinessAccountListMapper entityToBusinessAccountListMapper;

  public AccountUserRelationEntity findRelation(Site site, Account account, SofiaUser user) {
    return accountRepository.find(account.getId(), user.getId());
  }

  public Accounts getAll(SofiaUser user) {
    EntityList<AccountEntity> entityList = accountRepository.findAll(user.getAccount().getSite().getId(), user.getId());
    return entityToBusinessAccountListMapper.map(entityList);
  }

  public boolean ownsTheAccount(SofiaUser user, Account account) {
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

  public Account getAccountToSetForUser(Account accountFromSession, String email, Site site, int userId) {
    log.debug("Get the account to use with the user " + email);
    if (accountFromSession == null) {
      log.debug("There is no an account defined in the session");
      Account accountFromPreferences = userPreferencesManager.getAccountByUserId(userId);
      if (accountFromPreferences == null) {
        log.debug("Account from preferences is null");
        AccountEntity accountEntity = accountRepository.getAccountByEMail(email, site.getId());
        if (accountEntity == null) {
          throw new UsernameNotFoundException(email);
        }
        return entityToBusinessAccountMapper.map(accountEntity);
      } else {
        log.debug("FOUND account in preferences. Select account from preferences");
        return accountFromPreferences;
      }
      // There is an account in the session
    } else {
      log.debug("FOUND account in session. Search for relation with the user");
      AccountUserRelationEntity relation = accountRepository.find(accountFromSession.getId(), userId);
      if (relation == null) {
        log.debug("Relation with user NOT FOUND. Using the account for the user");
        AccountEntity userOwnedAccount = accountRepository.getAccountByUserId(userId);
        return entityToBusinessAccountMapper.map(userOwnedAccount);
      } else {
        log.debug("FOUND relation between account and user. Use account from session");
        return accountFromSession;
      }
    }
  }

  public Accounts findAll(Site site) {
    log.debug("Get the account list for site " + site);
    EntityList<AccountEntity> accountEntityList = accountRepository.findAll(site.getId());
    Accounts list = entityToBusinessAccountListMapper.map(accountEntityList);
    return list;
  }

  public Accounts findAll(Site site, SofiaUser user) {
    log.debug("Get the account list for the user " + user + " on site " + site);
    EntityList<AccountEntity> accountEntityList = accountRepository.findAll(site.getId(), user.getId());
    Accounts list = entityToBusinessAccountListMapper.map(accountEntityList);
    return list;
  }
}
