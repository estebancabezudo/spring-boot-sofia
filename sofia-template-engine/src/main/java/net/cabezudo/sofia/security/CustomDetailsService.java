package net.cabezudo.sofia.security;

import net.cabezudo.sofia.accounts.Account;
import net.cabezudo.sofia.accounts.mappers.EntityToBusinessAccountMapper;
import net.cabezudo.sofia.accounts.persistence.AccountEntity;
import net.cabezudo.sofia.accounts.persistence.AccountRepository;
import net.cabezudo.sofia.sites.Site;
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
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
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

  private @Autowired OAuth2AuthorizedClientService authorizedClientService;
  private @Autowired EntityToBusinessAccountMapper entityToBusinessAccountMapper;

  @Override
  public SofiaUser loadUserByUsername(String email) throws UsernameNotFoundException {

    authorizedClientService.loadAuthorizedClient("google", email);

    Site site = (Site) request.getSession().getAttribute("site");
    Account account = (Account) request.getSession().getAttribute("account");

    int accountId;
    Account accountToSet;
    if (account == null) {
      AccountEntity accountEntity = accountRepository.getAccountFor(site, email);
      accountId = accountEntity.id();
      accountToSet = entityToBusinessAccountMapper.map(accountEntity);
    } else {
      accountId = account.id();
      accountToSet = account;
    }
    final UserEntity userEntity = userRepository.findByEmail(site, accountId, email);
    if (userEntity == null) {
      throw new UsernameNotFoundException(email);
    }

    SofiaUser sofiaUser = entityToBusinessUserMapper.map(userEntity);
    sofiaUser.setAccount(accountToSet);
    return sofiaUser;
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
