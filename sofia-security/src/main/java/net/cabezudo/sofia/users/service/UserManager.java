package net.cabezudo.sofia.users.service;

import net.cabezudo.sofia.accounts.Account;
import net.cabezudo.sofia.sites.Site;
import net.cabezudo.sofia.users.SofiaUser;
import net.cabezudo.sofia.users.mappers.BusinessToEntityUserMapper;
import net.cabezudo.sofia.users.mappers.EntityToBusinessUserListMapper;
import net.cabezudo.sofia.users.mappers.EntityToBusinessUserMapper;
import net.cabezudo.sofia.users.persistence.GroupEntity;
import net.cabezudo.sofia.users.persistence.GroupsEntity;
import net.cabezudo.sofia.users.persistence.UserEntity;
import net.cabezudo.sofia.users.persistence.UserEntityList;
import net.cabezudo.sofia.users.persistence.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;

@Service
@Transactional
public class UserManager {

  private @Autowired EntityToBusinessUserListMapper entityToBusinessUserListMapper;
  private @Autowired BusinessToEntityUserMapper businessToEntityUserMapper;
  private @Autowired EntityToBusinessUserMapper entityToBusinessUserMapper;
  private @Resource UserRepository userRepository;
  private @Autowired HttpServletRequest request;

  public SofiaUser loadUserByUsername(String email) throws UsernameNotFoundException {
    Site site = (Site) request.getSession().getAttribute("site");
    final UserEntity userEntity = userRepository.findByEmail(site.getId(), email);
    if (userEntity == null) {
      throw new UsernameNotFoundException(email);
    }
    SofiaUser sofiaUser = new SofiaUser(userEntity.getId(), userEntity.getUsername(), userEntity.getPassword(), getAuthorities(userEntity), userEntity.isEnabled());
    return sofiaUser;
  }

  private Collection<GrantedAuthority> getAuthorities(UserEntity userEntity) {
    GroupsEntity userGroups = userEntity.getEntityGroups();
    Collection<GrantedAuthority> authorities = new ArrayList<>(userGroups.size());
    for (GroupEntity groupEntity : userGroups) {
      authorities.add(new SimpleGrantedAuthority(groupEntity.getName().toUpperCase()));
    }
    return authorities;
  }

  public UserList findAll(Site site, Account account) {
    final UserEntityList entityList = userRepository.findAll(site.getId(), account.id());
    return entityToBusinessUserListMapper.map(entityList);
  }

  public SofiaUser get(Account account, Integer id) {
    final UserEntity placeEntity = userRepository.get(id);
    return entityToBusinessUserMapper.map(placeEntity);
  }

  public SofiaUser create(Account account, SofiaUser user) {
    UserEntity placeEntityToSave = businessToEntityUserMapper.map(account, user);
    final UserEntity userEntity = userRepository.create(placeEntityToSave);
    return entityToBusinessUserMapper.map(userEntity);
  }
}


