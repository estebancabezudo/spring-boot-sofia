package net.cabezudo.sofia.config;

import net.cabezudo.sofia.core.SofiaEnvironment;
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
  private @Autowired SofiaEnvironment sofiaEnvironment;
  private @Autowired Environment environment;

  @PostConstruct
  public void init() throws ConfigurationException {
    log.debug("Set Sofia profile");
    String[] activeProfiles = environment.getActiveProfiles();
    if (activeProfiles != null) {
      for (String profile : activeProfiles) {
        switch (profile) {
          case SofiaEnvironment.DEV:
            if (sofiaEnvironment.isProduction()) {
              log.warn("Production profile already set. Don't set development profile");
            } else {
              sofiaEnvironment.setName(profile);
            }
            break;
          case SofiaEnvironment.PROD:
            sofiaEnvironment.setName(profile);
            break;
          default:
            log.debug("Ignoring profile name: " + profile);
        }
      }
    }
    if (sofiaEnvironment.getName() == null) {
      sofiaEnvironment.setName(SofiaEnvironment.PROD);
      log.debug("Profile set to " + sofiaEnvironment.getName());
    }
    log.debug("Final profile set to " + sofiaEnvironment.getName());
  }
}
