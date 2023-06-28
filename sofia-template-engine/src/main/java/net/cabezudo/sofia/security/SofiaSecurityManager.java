package net.cabezudo.sofia.security;

import net.cabezudo.sofia.accounts.Account;
import net.cabezudo.sofia.accounts.mappers.EntityToBusinessAccountMapper;
import net.cabezudo.sofia.accounts.persistence.AccountEntity;
import net.cabezudo.sofia.accounts.persistence.AccountRepository;
import net.cabezudo.sofia.core.SofiaRuntimeException;
import net.cabezudo.sofia.emails.persistence.EMailEntity;
import net.cabezudo.sofia.emails.persistence.EMailRepository;
import net.cabezudo.sofia.sites.Site;
import net.cabezudo.sofia.sites.SiteManager;
import net.cabezudo.sofia.sites.SiteNotFoundException;
import net.cabezudo.sofia.userpreferences.UserPreferencesManager;
import net.cabezudo.sofia.users.SofiaUser;
import net.cabezudo.sofia.users.mappers.EntityToBusinessUserMapper;
import net.cabezudo.sofia.users.persistence.UserEntity;
import net.cabezudo.sofia.users.persistence.UserRepository;
import net.cabezudo.sofia.web.client.WebClientData;
import net.cabezudo.sofia.web.client.WebClientDataManager;
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
  private @Autowired EntityToBusinessAccountMapper entityToBusinessAccountMapper;
  private @Autowired EMailRepository emailRepository;
  private @Autowired HttpServletRequest request;
  private @Autowired UserPreferencesManager userPreferencesManager;
  private @Autowired WebClientDataManager webClientDataManager;
  private @Autowired SiteManager siteManager;

  @Transactional
  public SofiaUser getLoggedUser() {
    log.debug("Get the logged user");
    WebClientData webClientData = webClientDataManager.getFromSession(request);
    if (webClientData == null) {
      throw new SofiaRuntimeException("The web client data is null");
    }
    SofiaUser userFromRequest = webClientData.getUser();

    if (userFromRequest != null) {
      return userFromRequest;
    }
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication instanceof UsernamePasswordAuthenticationToken) {
      UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = (UsernamePasswordAuthenticationToken) authentication;
      SofiaUser user = (SofiaUser) usernamePasswordAuthenticationToken.getPrincipal();
      setUserInWebClientData(webClientData, user);
      return user;
    }

    if (authentication instanceof OAuth2AuthenticationToken) {
      OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
      OAuth2User principal = oAuth2AuthenticationToken.getPrincipal();
      String email = principal.getAttribute("email");
      Site site;
      try {
        site = siteManager.getByHostname(request.getServerName());
      } catch (SiteNotFoundException e) {
        throw new SofiaRuntimeException(e);
      }
      // TODO Get the account from the stored as default for the user with that email
      AccountEntity accountEntity = accountRepository.getAccountByEMail(email, site.getId());
      if (accountEntity == null) {
        SofiaUser newUser = newUser(site, webClientData.getLanguage().toLocale(), email);
        setUserInWebClientData(webClientData, newUser);
        return newUser;
      } else {
        final UserEntity userEntity = userRepository.findByEmail(accountEntity.getId(), email);
        if (userEntity == null) {
          throw new UsernameNotFoundException(email);
        }
        SofiaUser user = entityToBusinessUserMapper.map(accountEntity, userEntity);
        setUserInWebClientData(webClientData, user);
        return user;
      }
    }
    return null;
  }

  private void setUserInWebClientData(WebClientData webClientData, SofiaUser user) {
    webClientData.setUser(user);
    log.debug("Web client data=" + webClientData);
    if (webClientData.getAccount() == null) {
      Account preferredAccount = userPreferencesManager.getAccount(user);
      log.debug("Account recovered from user preferences=" + preferredAccount);
      if (preferredAccount == null) {
        log.debug("The user don't have a preferred account. Set the user account " + user.getAccount() + " as preferred in web client data session object.");
        webClientData.setAccount(user.getAccount());
        userPreferencesManager.createAccount(webClientData.getUser(), webClientData.getAccount());
      } else {
        log.debug("Setting the user preferred account " + user.getAccount() + " in web client data session object.");
        webClientData.setAccount(user.getAccount());
      }
    }
  }

  private SofiaUser newUser(Site site, Locale locale, String email) {
    EMailEntity emailForAccount;
    EMailEntity emailEntityFromRepository = emailRepository.get(email);
    if (emailEntityFromRepository == null) {
      emailForAccount = emailRepository.create(email);
    } else {
      emailForAccount = emailEntityFromRepository;
    }
    UserEntity userEntity = userRepository.create(emailForAccount, locale.toString(), true);
    AccountEntity newAccountEntity = accountRepository.create(site, emailForAccount.getEmail());
    accountRepository.create(newAccountEntity.getId(), userEntity.getId(), true);
    UserEntity newUser = userRepository.get(newAccountEntity.getId(), userEntity.getId());
    SofiaUser user = entityToBusinessUserMapper.map(newAccountEntity, newUser);
    return user;
  }
}
