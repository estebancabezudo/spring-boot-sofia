package net.cabezudo.sofia.security;

import net.cabezudo.sofia.accounts.mappers.EntityToBusinessAccountMapper;
import net.cabezudo.sofia.accounts.persistence.AccountEntity;
import net.cabezudo.sofia.accounts.persistence.AccountRepository;
import net.cabezudo.sofia.accounts.persistence.AccountUserRelationEntity;
import net.cabezudo.sofia.emails.persistence.EMailEntity;
import net.cabezudo.sofia.emails.persistence.EMailRepository;
import net.cabezudo.sofia.sites.Site;
import net.cabezudo.sofia.users.SofiaUser;
import net.cabezudo.sofia.users.mappers.EntityToBusinessUserMapper;
import net.cabezudo.sofia.users.persistence.GroupsRepository;
import net.cabezudo.sofia.users.persistence.UserEntity;
import net.cabezudo.sofia.users.persistence.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

@Component
public class SofiaSecurityManager {

  private static final Logger log = LoggerFactory.getLogger(SofiaSecurityManager.class);
  private @Autowired AccountRepository accountRepository;
  private @Autowired UserRepository userRepository;
  private @Autowired EntityToBusinessUserMapper entityToBusinessUserMapper;
  private @Autowired EntityToBusinessAccountMapper entityToBusinessAccountMapper;
  private @Autowired EMailRepository emailRepository;
  private @Autowired GroupsRepository groupsRepository;
  private @Autowired HttpServletRequest request;

  @Transactional
  public SofiaUser getLoggedUser() {
    log.debug("Get the logged user");
    SofiaUser userFromRequest = (SofiaUser) request.getSession().getAttribute("user");
    if (userFromRequest != null) {
      return userFromRequest;
    }
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication instanceof UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) {
      SofiaUser user = (SofiaUser) usernamePasswordAuthenticationToken.getPrincipal();
      request.getSession().setAttribute("user", user);
      return user;
    }

    if (authentication instanceof OAuth2AuthenticationToken oAuth2AuthenticationToken) {
      OAuth2User principal = oAuth2AuthenticationToken.getPrincipal();
      String email = principal.getAttribute("email");
      // TODO Get the account from the stored as default for the user with that email
      AccountEntity accountEntity = accountRepository.getAccountFor(email);
      if (accountEntity == null) {
        SofiaUser newUser = newUser(email);
        request.getSession().setAttribute("user", newUser);
        return newUser;
      } else {
        final UserEntity userEntity = userRepository.findByEmail(accountEntity.id(), email);
        if (userEntity == null) {
          throw new UsernameNotFoundException(email);
        }
        SofiaUser user = entityToBusinessUserMapper.map(userEntity);
        user.setAccount(entityToBusinessAccountMapper.map(accountEntity));
        return user;
      }
    }
    return null;
  }

  private SofiaUser newUser(String email) {
    EMailEntity emailForAccount;
    EMailEntity emailEntityFromRepository = emailRepository.get(email);
    if (emailEntityFromRepository == null) {
      emailForAccount = emailRepository.create(email);
    } else {
      emailForAccount = emailEntityFromRepository;
    }
    UserEntity userEntity = userRepository.create(emailForAccount, true);
    Site site = (Site) request.getSession().getAttribute("site");
    AccountEntity newAccountEntity = accountRepository.create(site.getId());
    AccountUserRelationEntity accountUserRelationEntity = accountRepository.create(newAccountEntity.id(), userEntity.getId(), true);
    UserEntity newUser = userRepository.get(userEntity.getId());
    SofiaUser user = entityToBusinessUserMapper.map(newUser);
    user.setAccount(entityToBusinessAccountMapper.map(newAccountEntity));
    return user;
  }
}
