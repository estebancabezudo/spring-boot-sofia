package net.cabezudo.sofia.security;

import net.cabezudo.sofia.accounts.persistence.AccountEntity;
import net.cabezudo.sofia.accounts.persistence.AccountRepository;
import net.cabezudo.sofia.accounts.persistence.AccountUserRelationEntity;
import net.cabezudo.sofia.accounts.service.Account;
import net.cabezudo.sofia.core.SofiaRuntimeException;
import net.cabezudo.sofia.emails.persistence.EMailEntity;
import net.cabezudo.sofia.emails.persistence.EMailRepository;
import net.cabezudo.sofia.people.OAuth2PersonAdapter;
import net.cabezudo.sofia.people.OAuthPersonData;
import net.cabezudo.sofia.people.Person;
import net.cabezudo.sofia.people.service.PeopleManager;
import net.cabezudo.sofia.sites.Site;
import net.cabezudo.sofia.sites.SiteNotFoundException;
import net.cabezudo.sofia.sites.service.SiteManager;
import net.cabezudo.sofia.userpreferences.UserPreferencesManager;
import net.cabezudo.sofia.users.mappers.EntityToBusinessUserMapper;
import net.cabezudo.sofia.users.persistence.UserEntity;
import net.cabezudo.sofia.users.persistence.UserRepository;
import net.cabezudo.sofia.users.service.SofiaUser;
import net.cabezudo.sofia.web.client.Language;
import net.cabezudo.sofia.web.client.WebClientData;
import net.cabezudo.sofia.web.client.WebClientDataManager;
import net.cabezudo.sofia.web.user.WebUserData;
import net.cabezudo.sofia.web.user.WebUserDataManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.Locale;

@Service
public class SofiaSecurityManager {

  private static final Logger log = LoggerFactory.getLogger(SofiaSecurityManager.class);
  private @Autowired AccountRepository accountRepository;
  private @Autowired UserRepository userRepository;
  private @Autowired EntityToBusinessUserMapper entityToBusinessUserMapper;
  private @Autowired EMailRepository emailRepository;
  private @Autowired HttpServletRequest request;
  private @Autowired UserPreferencesManager userPreferencesManager;
  private @Autowired WebClientDataManager webClientDataManager;
  private @Autowired SiteManager siteManager;
  private @Autowired OAuth2PersonAdapter oAuth2PersonAdapter;
  private @Autowired PeopleManager peopleManager;
  private @Autowired WebUserDataManager webUserDataManager;

  @Transactional
  public SofiaUser getLoggedUser() {
    log.debug("Get the logged user");
    WebClientData webClientData = webClientDataManager.getFromCookie(request);
    WebUserData webUserData = webUserDataManager.getFromSession(request);
    if (webUserData == null) {
      return null;
    }
    SofiaUser userFromSession = webUserData.getUser();

    if (userFromSession != null) {
      return userFromSession;
    }
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication instanceof UsernamePasswordAuthenticationToken) {
      UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = (UsernamePasswordAuthenticationToken) authentication;
      SofiaUser user = (SofiaUser) usernamePasswordAuthenticationToken.getPrincipal();
      setUserInWebUserData(webClientData, webUserData, user, user.getAccount());
      return user;
    }

    if (authentication instanceof OAuth2AuthenticationToken) {
      OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
      OAuth2User principal = oAuth2AuthenticationToken.getPrincipal();

      Site site;
      SofiaUser user;
      try {
        site = siteManager.getByHostname(request.getServerName());
      } catch (SiteNotFoundException e) {
        throw new SofiaRuntimeException(e);
      }

      String email = principal.getAttribute("email");
      OAuthPersonData oAuthPersonData = oAuth2PersonAdapter.build(site, email, oAuth2AuthenticationToken);

      // TODO Get the account from the stored as default for the user with that email
      AccountEntity accountEntity = accountRepository.getAccountOwnedByEMail(site.getId(), email);
      if (accountEntity == null) {
        Language language = webUserData.getLanguage();
        Locale locale;
        if (language == null) {
          locale = new Locale("en");
        } else {
          locale = language.toLocale();
        }
        SofiaUser newUser = newUser(site, locale, email);

        Account accountFromWebClientData = webClientData.getAccount();
        Account account;
        if (accountFromWebClientData == null) {
          account = newUser.getAccount();
        } else {
          account = accountFromWebClientData;
        }

        AccountUserRelationEntity accountUserRelation = accountRepository.getByAccountAndUser(account.getId(), newUser.getId());
        if (accountUserRelation == null) {
          accountRepository.createAccountUserRelation(account.getId(), newUser.getId(), false);
        }
        setUserInWebUserData(webClientData, webUserData, newUser, account);
        user = newUser;
      } else {
        final UserEntity userEntity = userRepository.findByEmailAndAccount(accountEntity.getId(), email);
        if (userEntity == null) {
          throw new UsernameNotFoundException(email);
        }
        
        Account accountFromWebClientData = webClientData.getAccount();
        if (accountFromWebClientData != null) {
          AccountUserRelationEntity accountUserRelation = accountRepository.getByAccountAndUser(accountFromWebClientData.getId(), userEntity.getId());
          if (accountUserRelation == null) {
            accountRepository.createAccountUserRelation(accountFromWebClientData.getId(), userEntity.getId(), false);
          }
        }

        user = entityToBusinessUserMapper.map(userEntity);
        setUserInWebUserData(webClientData, webUserData, user, user.getAccount());
      }

      Person personFromDataBase = peopleManager.getByEMail(email);
      if (personFromDataBase == null) {
        String name = oAuthPersonData.getName();
        String secondName = oAuthPersonData.getSecondName();
        String lastName = oAuthPersonData.getLastName();
        String secondLastName = oAuthPersonData.getSecondLlastName();
        Person person = peopleManager.create(name, secondName, lastName, secondLastName, null);
        peopleManager.relate(user, person);
      }
      return user;
    }
    return null;
  }

  private void setUserInWebUserData(WebClientData webClientData, WebUserData webUserData, SofiaUser user, Account account) {
    webUserData.setUser(user);
    log.debug("Web user data " + webUserData);

    if (webUserData.getAccount() == null) {
      Account preferredAccount = userPreferencesManager.getAccountByUserId(user.getId());
      log.debug("Account recovered from user preferences=" + preferredAccount);

      if (preferredAccount == null) {
        log.debug("The user don't have a preferred account. Set the user account " + user.getAccount() + " as preferred in web client data session object.");
        webUserData.setAccount(account);
        userPreferencesManager.setAccount(webUserData.getUser(), webUserData.getAccount());
      } else {
        log.debug("Setting the user preferred account " + user.getAccount() + " in web client data session object.");
        webUserData.setAccount(preferredAccount);
      }
    }

    if (webUserData.getLanguage() == null) {
      Language language;
      Language languageFromPreferences = userPreferencesManager.getLanguageByUserId(user.getId());
      if (languageFromPreferences == null) {
        language = webClientData.getLanguage();
        userPreferencesManager.setLanguage(user, language);
      } else {
        language = languageFromPreferences;
      }
      webUserData.setLanguage(language);
    }
  }

  private SofiaUser newUser(Site site, Locale locale, String newEMailAddress) {
    EMailEntity emailForAccount;
    EMailEntity emailEntityFromRepository = emailRepository.get(newEMailAddress);
    if (emailEntityFromRepository == null) {
      emailForAccount = emailRepository.create(newEMailAddress);
    } else {
      emailForAccount = emailEntityFromRepository;
    }

    int userEntityId;
    Integer userEntityIdFromDatabase = userRepository.getIdByEMail(emailForAccount.getId());
    if (userEntityIdFromDatabase == null) {
      UserEntity userEntity = userRepository.create(emailForAccount, locale.toString(), true);
      userEntityId = userEntity.getId();
    } else {
      userEntityId = userEntityIdFromDatabase;
    }

    int siteId = site.getId();
    String emailAddress = emailForAccount.getEmail();

    AccountEntity userAccountEntity;
    AccountEntity accountFromDatabase = accountRepository.getAccountByEMail(siteId, emailAddress);
    if (accountFromDatabase == null) {
      userAccountEntity = accountRepository.create(siteId, emailAddress);
    } else {
      userAccountEntity = accountFromDatabase;
    }
    accountRepository.createAccountUserRelation(userAccountEntity.getId(), userEntityId, true);

    UserEntity newUser = userRepository.get(userEntityId);
    SofiaUser user = entityToBusinessUserMapper.map(newUser);
    return user;
  }
}
