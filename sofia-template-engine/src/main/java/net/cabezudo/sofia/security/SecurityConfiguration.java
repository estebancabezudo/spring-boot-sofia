package net.cabezudo.sofia.security;

import net.cabezudo.sofia.core.SofiaEnvironment;
import net.cabezudo.sofia.security.oauth2.SofiaOAuth2AuthenticationFailureHandler;
import net.cabezudo.sofia.security.oauth2.SofiaOAuth2AuthenticationSuccessHandler;
import net.cabezudo.sofia.sites.service.SiteManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;

/**
 * @author <a href="http://cabezudo.net">Esteban Cabezudo</a>
 * @version 0.01.00, 2022.07.30
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
@ComponentScan
public class SecurityConfiguration {
  private static final Logger log = LoggerFactory.getLogger(SecurityConfiguration.class);

  private @Autowired UrlAuthenticationFilter urlAuthenticationFilter;
  private @Autowired SofiaEnvironment sofiaEnvironment;
  private @Autowired SiteManager siteManager;
  private @Autowired SofiaAuthorizationManager sofiaAuthorizationManager;

  @Bean
  public SofiaAuthenticationProvider authProvider() {
    SofiaAuthenticationProvider sofiaAuthenticationProvider = new SofiaAuthenticationProvider();
    sofiaAuthenticationProvider.setPasswordEncoder(passwordEncoder());
    return sofiaAuthenticationProvider;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }

  public HttpSessionRequestCache getHttpSessionRequestCache() {
    HttpSessionRequestCache httpSessionRequestCache = new HttpSessionRequestCache();
    httpSessionRequestCache.setCreateSessionAllowed(false);
    return httpSessionRequestCache;
  }

  @Bean
  SecurityFilterChain web(HttpSecurity http) throws Exception {
    log.debug("Run security configuration security web filter chain");

    if (sofiaEnvironment.isDevelopment()) {
      log.debug("The URL authenticator filter is active");
      http.addFilterBefore(urlAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }

    http
        .requestCache().requestCache(getHttpSessionRequestCache())
        .and()
        .formLogin()
        .successHandler(new SofiaAuthenticationSuccessHandler(siteManager))
        .failureHandler(new SofiaAuthenticationFailureHandler())
        .loginPage(SofiaSecurityConfig.DEFAULT_LOGIN_PAGE)
        .loginProcessingUrl(SofiaSecurityConfig.DEFAULT_LOGIN_WEB_SERVICE).and()
        .logout()
        .logoutSuccessUrl(SofiaSecurityConfig.DEFAULT_LOGOUT_SUCCESS_URL)
        .and()
        .oauth2Login()
        .successHandler(new SofiaOAuth2AuthenticationSuccessHandler(siteManager))
        .failureHandler(new SofiaOAuth2AuthenticationFailureHandler())
        .and()
        .authorizeHttpRequests((authorize) -> {
              try {
                authorize
                    .anyRequest().access(sofiaAuthorizationManager)
                    .and().csrf().disable();
              } catch (Exception e) {
                log.warn(e.getMessage());
              }
            }
        )
    ;
    return http.build();
  }
}
