package net.cabezudo.sofia.accounts.service;

import net.cabezudo.sofia.accounts.mappers.EntityToBusinessAccountListMapper;
import net.cabezudo.sofia.accounts.mappers.EntityToBusinessAccountMapper;
import net.cabezudo.sofia.accounts.persistence.AccountEntity;
import net.cabezudo.sofia.accounts.persistence.AccountRepository;
import net.cabezudo.sofia.accounts.persistence.AccountUserRelationEntity;
import net.cabezudo.sofia.core.persistence.EntityList;
import net.cabezudo.sofia.sites.Site;
import net.cabezudo.sofia.userpreferences.UserPreferencesManager;
import net.cabezudo.sofia.users.service.SofiaUser;
import net.cabezudo.sofia.web.client.WebClientData;
import net.cabezudo.sofia.web.client.WebClientDataManager;
import net.cabezudo.sofia.web.user.WebUserData;
import net.cabezudo.sofia.web.user.WebUserDataManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Service
public class AccountManager {
  private static final Logger log = LoggerFactory.getLogger(AccountManager.class);
  private @Resource AccountRepository accountRepository;
  private @Autowired EntityToBusinessAccountMapper entityToBusinessAccountMapper;
  private @Autowired UserPreferencesManager userPreferencesManager;
  private @Autowired EntityToBusinessAccountListMapper entityToBusinessAccountListMapper;
  private @Autowired WebClientDataManager webClientDataManager;
  private @Autowired WebUserDataManager webUserDataManager;

  public AccountUserRelationEntity findRelation(Site site, Account account, SofiaUser user) {
    return accountRepository.getByAccountAndUser(account.getId(), user.getId());
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

  public Account getAccountToSetForUser(String email, Site site, int userId) {
    log.debug("Get the account to use with the user " + email);
    Account accountFromPreferences = userPreferencesManager.getAccountByUserId(userId);
    if (accountFromPreferences == null) {
      log.debug("Account from preferences is null");
      AccountEntity accountEntity = accountRepository.getAccountOwnedByEMail(site.getId(), email);
      if (accountEntity == null) {
        throw new UsernameNotFoundException(email);
      }
      return entityToBusinessAccountMapper.map(accountEntity);
    } else {
      log.debug("FOUND account in preferences. Select account from preferences");
      return accountFromPreferences;
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

  public Account getByName(Site site, String name) {
    log.debug("Get the account with the name " + name);
    AccountEntity accountEntity = accountRepository.getByName(site.getId(), name);
    Account account = entityToBusinessAccountMapper.map(accountEntity);
    return account;
  }

  public void setSiteAccount(HttpServletRequest request, Site site, String name) {
    Account account = getByName(site, name);
    WebClientData webClientData = webClientDataManager.getFromCookie(request);
    webClientData.setAccount(account);
    webClientDataManager.update(webClientData.getId(), webClientData);
  }

  public void setUserAccount(HttpServletRequest request, Site site, String name) {
    Account account = getByName(site, name);
    WebUserData webUserData = webUserDataManager.getFromSession(request);
    webUserData.setAccount(account);
    SofiaUser user = webUserData.getUser();
    userPreferencesManager.setAccount(user, account);
  }

  // If the client have an account, change the webClientData. If the client doesn't have account and there is a user logged change the user account
  public void setSessionAccount(HttpServletRequest request, Site site, String name) {
    WebClientData webClientData = webClientDataManager.getFromCookie(request);
    if (webClientData.getAccount() == null) {
      setUserAccount(request, site, name);
    } else {
      setSiteAccount(request, site, name);
    }
  }

  public Account getAccount(HttpServletRequest request) {
    WebUserData webUserData = webUserDataManager.getFromSession(request);
    WebClientData webClientData = webClientDataManager.getFromCookie(request);

    Account accountFromUser = webUserData.getAccount();
    Account accountFromClientData = webClientData.getAccount();

    return getAccount(accountFromUser, accountFromClientData);
  }

  private Account getAccount(Account accountFromUser, Account accountFromClientData) {
    Account account;
    if (accountFromClientData == null) {
      account = accountFromUser;
    } else {
      account = accountFromClientData;
    }
    return account;
  }

}
