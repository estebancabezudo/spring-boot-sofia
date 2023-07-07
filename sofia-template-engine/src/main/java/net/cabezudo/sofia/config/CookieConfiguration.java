package net.cabezudo.sofia.config;

import org.springframework.boot.web.servlet.server.CookieSameSiteSupplier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author <a href="http://cabezudo.net">Esteban Cabezudo</a>
 * @version 0.01.00, 2022.07.30
 */
@Configuration
public class CookieConfiguration {
  @Bean
  public CookieSameSiteSupplier applicationCookieSameSiteSupplier() {
    return CookieSameSiteSupplier.ofLax();
  }
}
