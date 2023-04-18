package net.cabezudo.sofia.core.modules;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class ModuleManager {
  private final Logger log = LoggerFactory.getLogger(ModuleManager.class);
  private SofiaSecurityModule securityModule;
  @Autowired
  private ApplicationContext applicationContext;

  @PostConstruct
  public void init() {
    if (applicationContext.containsBeanDefinition("sofiaSecurityModuleImplementation")) {
      log.debug("Sofia security module found.");
      this.securityModule = (SofiaSecurityModule) applicationContext.getBean("sofiaSecurityModuleImplementation");
    } else {
      log.debug("Sofia security module not found.");
    }
  }

  public SofiaSecurityModule getSecurityModule() {
    return this.securityModule;
  }
}
