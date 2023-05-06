package net.cabezudo.sofia.config;

import net.cabezudo.sofia.core.SofiaResourceResolver;
import net.cabezudo.sofia.core.SofiaTemplateEngineEnvironment;
import net.cabezudo.sofia.sites.PathManager;
import net.cabezudo.sofia.sites.SiteManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class TemplateEngineConfiguration implements WebMvcConfigurer {
  private @Autowired SofiaTemplateEngineEnvironment sofiaTemplateEngineEnvironment;
  private @Autowired SiteManager siteManager;
  private @Autowired PathManager pathManager;
  
  @EventListener(ApplicationReadyEvent.class)
  public void loadData() throws ConfigurationException {
    sofiaTemplateEngineEnvironment.loadConfiguration();
  }

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("/**").resourceChain(true).addResolver(new SofiaResourceResolver(siteManager, pathManager));
  }
}
