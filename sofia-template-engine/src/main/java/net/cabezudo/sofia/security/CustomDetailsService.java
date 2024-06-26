package net.cabezudo.sofia.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import net.cabezudo.sofia.accounts.persistence.AccountRepository;
import net.cabezudo.sofia.accounts.service.Account;
import net.cabezudo.sofia.accounts.service.AccountManager;
import net.cabezudo.sofia.core.SofiaRuntimeException;
import net.cabezudo.sofia.language.Language;
import net.cabezudo.sofia.sites.Site;
import net.cabezudo.sofia.sites.SiteNotFoundException;
import net.cabezudo.sofia.sites.service.SiteManager;
import net.cabezudo.sofia.userpreferences.UserPreferencesManager;
import net.cabezudo.sofia.userpreferences.persistence.UserPreferencesRepository;
import net.cabezudo.sofia.users.mappers.EntityToBusinessUserMapper;
import net.cabezudo.sofia.users.persistence.GroupEntity;
import net.cabezudo.sofia.users.persistence.GroupsEntity;
import net.cabezudo.sofia.users.persistence.SofiaUserEntity;
import net.cabezudo.sofia.users.persistence.SofiaUserRepository;
import net.cabezudo.sofia.users.service.SofiaUser;
import net.cabezudo.sofia.web.client.WebClientData;
import net.cabezudo.sofia.web.client.WebClientDataManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
@Transactional
public class CustomDetailsService implements UserDetailsService {

  private @Autowired AccountRepository accountRepository;
  private @Autowired SofiaUserRepository sofiaUserRepository;
  private @Autowired HttpServletRequest request;
  private @Autowired HttpServletResponse response;
  private @Autowired EntityToBusinessUserMapper entityToBusinessUserMapper;
  private @Autowired SiteManager siteManager;
  private @Autowired AccountManager accountManager;
  private @Autowired WebClientDataManager webClientDataManager;
  private @Autowired UserPreferencesRepository userPreferencesRepository;

  @Override
  public SofiaUser loadUserByUsername(String email) throws UsernameNotFoundException {
    try {
      Site site = siteManager.get(request);

      SofiaUserEntity userLogged = sofiaUserRepository.findByEmail(email);
      if (userLogged == null) {
        throw new UsernameNotFoundException(email);
      }
      int userId = userLogged.getId();

      WebClientData webClientData = webClientDataManager.getFromCookie(request);

      String languageCodeFromDatabase = userPreferencesRepository.get(userId, UserPreferencesManager.LANGUAGE);
      if (languageCodeFromDatabase != null) {
        Language language = new Language(languageCodeFromDatabase);
        if (!webClientData.getLanguage().equals(language)) {
          webClientData.setLanguage(language);
        }
      }

      Account accountToSet = accountManager.getAccountToSetForUser(email, site, userId);
      if (webClientData.getAccount() == null || accountToSet.getId() != webClientData.getAccount().getId()) {
        webClientData.setAccount(accountToSet);
        userPreferencesRepository.update(userId, UserPreferencesManager.ACCOUNT, accountToSet.getId());
      }

      webClientDataManager.setCookie(response, webClientData);

      SofiaUserEntity sofiaUserEntity = sofiaUserRepository.getForAccount(accountToSet.getId(), userId);
      userLogged.setGroups(sofiaUserEntity.getGroups());

      SofiaUser sofiaUserLogged = entityToBusinessUserMapper.map(userLogged);
      return sofiaUserLogged;
    } catch (SiteNotFoundException e) {
      throw new SofiaRuntimeException(e);
    }
  }

  private Collection<GrantedAuthority> getAuthorities(SofiaUserEntity sofiaUserEntity) {
    GroupsEntity groupsEntity = sofiaUserEntity.getGroups();
    Collection<GrantedAuthority> authorities = new ArrayList<>(groupsEntity.size());
    for (GroupEntity groupEntity : groupsEntity) {
      authorities.add(new SimpleGrantedAuthority(groupEntity.getName()));
    }
    return authorities;
  }
}
