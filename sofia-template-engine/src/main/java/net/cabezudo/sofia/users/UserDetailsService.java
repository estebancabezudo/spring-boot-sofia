package net.cabezudo.sofia.users;

import net.cabezudo.sofia.sites.Site;
import net.cabezudo.sofia.users.mappers.EntityToBusinessUserMapper;
import net.cabezudo.sofia.users.persistence.GroupEntity;
import net.cabezudo.sofia.users.persistence.GroupsEntity;
import net.cabezudo.sofia.users.persistence.UserEntity;
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

@Service("userDetailsService")
@Transactional
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

  @Resource
  private UserRepository userRepository;
  private @Autowired HttpServletRequest request;
  private @Autowired EntityToBusinessUserMapper entityToBusinessUserMapper;

  @Override
  public SofiaUser loadUserByUsername(String email) throws UsernameNotFoundException {
    Site site = (Site) request.getSession().getAttribute("site");
    final UserEntity userEntity = userRepository.findByEmail(site.getId(), email);
    if (userEntity == null) {
      throw new UsernameNotFoundException(email);
    }

    SofiaUser sofiaUser = entityToBusinessUserMapper.map(userEntity);
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
