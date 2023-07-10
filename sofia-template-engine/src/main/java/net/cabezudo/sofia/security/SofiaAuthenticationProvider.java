package net.cabezudo.sofia.security;

import net.cabezudo.sofia.accounts.service.Account;
import net.cabezudo.sofia.web.client.WebClientData;
import net.cabezudo.sofia.web.client.WebClientDataManager;
import net.cabezudo.sofia.web.user.WebUserData;
import net.cabezudo.sofia.web.user.WebUserDataManager;
import org.springframework.beans.factory.annotation.Autowired;
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
import javax.servlet.http.HttpServletRequest;

public class SofiaAuthenticationProvider implements AuthenticationProvider {

  @Resource
  CustomDetailsService customDetailsService;
  private PasswordEncoder passwordEncoder;
  private @Autowired WebClientDataManager webClientDataManager;
  private @Autowired WebUserDataManager webUserDataManager;
  private @Autowired HttpServletRequest request;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    final String username = (authentication.getPrincipal() == null) ? "NONE_PROVIDED" : authentication.getName();
    if (username.isEmpty()) {
      throw new BadCredentialsException("Invalid login");
    }
    // get user details using Spring security user details service
    UserDetails userDetails;
    try {
      userDetails = customDetailsService.loadUserByUsername(username);
    } catch (UsernameNotFoundException exception) {
      throw new BadCredentialsException("Invalid login");
    }
    if (authentication.getCredentials() != null) {
      String presentedPassword = authentication.getCredentials().toString();
      if (this.passwordEncoder.matches(presentedPassword, userDetails.getPassword())) {
        WebClientData webClientData = webClientDataManager.getFromCookie(request);
        Account account = webClientData.getAccount();
        if (account != null) {
          WebUserData webUserData = webUserDataManager.getFromSession(request);
          webUserData.setAccount(account);
          webUserDataManager.set(request, webUserData);
        }
        return createSuccessfulAuthentication(authentication, userDetails);
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
