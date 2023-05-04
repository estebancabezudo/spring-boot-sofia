package net.cabezudo.sofia.security;

import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class SofiaGroupDecisionVoter implements AccessDecisionVoter<Object> { // TODO Delete this class if it is not used
  @Override
  public boolean supports(ConfigAttribute attribute) {
    return true;
  }

  @Override
  public boolean supports(Class<?> clazz) {
    return true;
  }

  @Override
  public int vote(Authentication authentication, Object object, Collection<ConfigAttribute> attributes) {
    return AccessDecisionVoter.ACCESS_GRANTED;
//    if (authentication == null) {
//      return ACCESS_DENIED;
//    }
//    int result = ACCESS_ABSTAIN;
//    Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
//    for (ConfigAttribute attribute : attributes) {
//      if (this.supports(attribute)) {
//        result = ACCESS_DENIED;
//        // Attempt to find a matching granted authority
//        for (GrantedAuthority authority : authorities) {
//          if (attribute.getAttribute().equals(authority.getAuthority())) {
//            return ACCESS_GRANTED;
//          }
//        }
//      }
//    }
//    return ACCESS_GRANTED;
  }
}
