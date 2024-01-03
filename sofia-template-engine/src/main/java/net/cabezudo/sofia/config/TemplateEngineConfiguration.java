package net.cabezudo.sofia.config;

import net.cabezudo.sofia.core.SofiaEnvironment;
import net.cabezudo.sofia.core.SofiaResourceResolver;
import net.cabezudo.sofia.sites.service.PathManager;
import net.cabezudo.sofia.sites.service.SiteManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.concurrent.TimeUnit;

@Configuration
public class TemplateEngineConfiguration implements WebMvcConfigurer {
  private @Autowired SofiaEnvironment sofiaEnvironment;
  private @Autowired SiteManager siteManager;
  private @Autowired PathManager pathManager;

  @EventListener(ApplicationReadyEvent.class)
  public void loadData() throws ConfigurationException {
    sofiaEnvironment.loadConfiguration();
  }

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry
        .addResourceHandler("/images/**", "/fonts/**")
        .setCacheControl(CacheControl.maxAge(20, TimeUnit.DAYS)
            .noTransform()
            .mustRevalidate())
        .resourceChain(sofiaEnvironment.isProduction()).addResolver(new SofiaResourceResolver(siteManager, pathManager));
    registry
        .addResourceHandler("/**")
        .resourceChain(false).addResolver(new SofiaResourceResolver(siteManager, pathManager));
  }
}
