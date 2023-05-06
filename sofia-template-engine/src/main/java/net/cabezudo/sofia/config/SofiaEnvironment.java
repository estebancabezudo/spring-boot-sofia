package net.cabezudo.sofia.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

@Component
public class SofiaEnvironment {
  private boolean securityActive;
  private Boolean development;
  
  private @Autowired Environment environment;

  public boolean isSecurityActive() {
    return securityActive;
  }

  public void setSecurityActive(boolean securityActive) {
    this.securityActive = securityActive;
  }

  public boolean isSecurityNotActive() {
    return !isSecurityActive();
  }

  public boolean isDevelopment() {
    if (development == null) {
      Set<String> set = new TreeSet<>(Arrays.asList(this.environment.getActiveProfiles()));
      development = set.contains("dev");
    }
    return development;
  }
}
