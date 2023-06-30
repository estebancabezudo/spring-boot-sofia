package net.cabezudo.sofia.security;

import net.cabezudo.sofia.accounts.Account;
import net.cabezudo.sofia.accounts.AccountManager;
import net.cabezudo.sofia.accounts.mappers.EntityToBusinessAccountMapper;
import net.cabezudo.sofia.accounts.persistence.AccountRepository;
import net.cabezudo.sofia.core.SofiaRuntimeException;
import net.cabezudo.sofia.sites.Site;
import net.cabezudo.sofia.sites.SiteManager;
import net.cabezudo.sofia.sites.SiteNotFoundException;
import net.cabezudo.sofia.userpreferences.UserPreferencesManager;
import net.cabezudo.sofia.users.SofiaUser;
import net.cabezudo.sofia.users.mappers.EntityToBusinessUserMapper;
import net.cabezudo.sofia.users.persistence.GroupEntity;
import net.cabezudo.sofia.users.persistence.GroupsEntity;
import net.cabezudo.sofia.users.persistence.UserEntity;
import net.cabezudo.sofia.users.persistence.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;

@Service
@Transactional
public class CustomDetailsService implements UserDetailsService {

  private @Resource AccountRepository accountRepository;
  private @Resource UserRepository userRepository;
  private @Autowired HttpServletRequest request;
  private @Autowired EntityToBusinessUserMapper entityToBusinessUserMapper;

  private @Autowired EntityToBusinessAccountMapper entityToBusinessAccountMapper;
  private @Autowired SiteManager siteManager;
  private @Autowired AccountManager accountManager;
  private @Autowired UserPreferencesManager userPreferencesManager;

  @Override
  public SofiaUser loadUserByUsername(String email) throws UsernameNotFoundException {
    Account accountFromSession = (Account) request.getSession().getAttribute("account");
    try {
      Site site = siteManager.get(request);

      UserEntity userEntityToGetId = userRepository.findByEmail(email);
      if (userEntityToGetId == null) {
        throw new UsernameNotFoundException(email);
      }
      int userId = userEntityToGetId.getId();
      Account accountToSet = accountManager.getActualAccount(accountFromSession, email, site, userId);

      UserEntity userEntity = userRepository.get(accountToSet.getId(), userId);
      SofiaUser sofiaUser = entityToBusinessUserMapper.map(accountToSet, userEntity);
      return sofiaUser;
    } catch (SiteNotFoundException e) {
      throw new SofiaRuntimeException(e);
    }
  }

  private Collection<GrantedAuthority> getAuthorities(UserEntity userEntity) {
    GroupsEntity groupsEntity = userEntity.getEntityGroups();
    Collection<GrantedAuthority> authorities = new ArrayList<>(groupsEntity.size());
    for (GroupEntity groupEntity : groupsEntity) {
      authorities.add(new SimpleGrantedAuthority(groupEntity.getName()));
    }
    return authorities;
  }
}
