package net.cabezudo.sofia.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.Assert;

import javax.annotation.Resource;

public class SofiaAuthenticationProvider implements AuthenticationProvider {

  @Resource
  CustomDetailsService customDetailsService;
  private PasswordEncoder passwordEncoder;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    final String username = (authentication.getPrincipal() == null) ? "NONE_PROVIDED" : authentication.getName();
    if (username.isEmpty()) {
      throw new BadCredentialsException("Invalid login");
    }
    // get user details using Spring security user details service
    UserDetails user;
    try {
      user = customDetailsService.loadUserByUsername(username);
    } catch (UsernameNotFoundException exception) {
      throw new BadCredentialsException("Invalid login");
    }
    if (authentication.getCredentials() != null) {
      String presentedPassword = authentication.getCredentials().toString();
      if (this.passwordEncoder.matches(presentedPassword, user.getPassword())) {
        return createSuccessfulAuthentication(authentication, user);
      }
    }
    throw new BadCredentialsException("Invalid login");
  }

  protected PasswordEncoder getPasswordEncoder() {
    return this.passwordEncoder;
  }

  public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
    Assert.notNull(passwordEncoder, "passwordEncoder cannot be null");
    this.passwordEncoder = passwordEncoder;
  }

  private Authentication createSuccessfulAuthentication(final Authentication authentication, final UserDetails user) {
    UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user, authentication.getCredentials(), user.getAuthorities());
    token.setDetails(authentication.getDetails());
    return token;
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return authentication.equals(UsernamePasswordAuthenticationToken.class);
  }
}
