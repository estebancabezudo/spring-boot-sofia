package net.cabezudo.sofia.config;

import net.cabezudo.sofia.core.SofiaTemplateEngineEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author <a href="http://cabezudo.net">Esteban Cabezudo</a>
 * @version 0.01.00, 2022.09.03
 */
@Component
public class SetSofiaProfiles {

  private static final Logger log = LoggerFactory.getLogger(SetSofiaProfiles.class);
  private @Autowired SofiaTemplateEngineEnvironment sofiaTemplateEngineEnvironment;
  private @Autowired Environment environment;

  @PostConstruct
  public void init() throws ConfigurationException {
    String[] activeProfiles = environment.getActiveProfiles();
    if (activeProfiles != null) {
      for (String profile : activeProfiles) {
        switch (profile) {
          case SofiaTemplateEngineEnvironment.DEV:
            sofiaTemplateEngineEnvironment.setName(profile);
            break;
          case SofiaTemplateEngineEnvironment.PROD:
            sofiaTemplateEngineEnvironment.setName(profile);
            break;
          default:
            // Ignore the other profiles
        }
      }
    }
    if (sofiaTemplateEngineEnvironment.getName() == null) {
      sofiaTemplateEngineEnvironment.setName(SofiaTemplateEngineEnvironment.DEV);
    }
  }
}
