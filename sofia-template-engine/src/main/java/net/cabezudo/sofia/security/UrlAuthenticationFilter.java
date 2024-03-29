package net.cabezudo.sofia.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.cabezudo.sofia.accounts.persistence.AccountEntity;
import net.cabezudo.sofia.accounts.persistence.AccountRepository;
import net.cabezudo.sofia.core.SofiaEnvironment;
import net.cabezudo.sofia.math.Numbers;
import net.cabezudo.sofia.sites.Site;
import net.cabezudo.sofia.sites.SiteNotFoundException;
import net.cabezudo.sofia.sites.service.SiteManager;
import net.cabezudo.sofia.users.service.SofiaUser;
import net.cabezudo.sofia.users.service.SofiaUserManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class UrlAuthenticationFilter extends OncePerRequestFilter {

  private static final String ACCOUNT_PARAMETER = "account";
  private static final String USER_PARAMETER = "user";

  private @Autowired SofiaUserManager sofiaUserManager;
  private @Autowired AccountRepository accountRepository;
  private @Autowired SiteManager siteManager;
  private @Autowired SofiaEnvironment sofiaEnvironment;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    String email = request.getParameter(USER_PARAMETER);

    if (email != null && sofiaEnvironment.isDevelopment() && !email.isEmpty()) {
      String accountParameter = request.getParameter(ACCOUNT_PARAMETER);

      SofiaUser user;
      if (Numbers.isInteger(accountParameter)) {
        int accountId = Integer.parseInt(accountParameter);
        user = sofiaUserManager.findById(accountId, email);
      } else {
        Site site = null;
        try {
          site = siteManager.getByName(request.getServerName());
        } catch (SiteNotFoundException e) {
          response.sendError(400, "Invalid host: " + request.getServerName());
        }
        // TODO create a find by email for user without search first the account for the email
        AccountEntity accountEntity = accountRepository.getAccountOwnedByEMail(site.getId(), email);
        user = sofiaUserManager.findById(accountEntity.getId(), email);
      }

      Authentication auth = new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());
      SecurityContextHolder.getContext().setAuthentication(auth);
    }

    filterChain.doFilter(request, response);
  }
}
