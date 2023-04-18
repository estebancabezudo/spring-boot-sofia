package net.cabezudo.sofia.core.modules;

import net.cabezudo.html.nodes.Element;
import net.cabezudo.sofia.config.ConfigurationException;
import net.cabezudo.sofia.config.ConfigurationFileYAMLData;
import net.cabezudo.sofia.creator.Caller;
import net.cabezudo.sofia.sites.Site;
import net.cabezudo.sofia.sites.SiteManager;
import net.cabezudo.sofia.users.rest.RestUser;

import java.nio.file.Path;

public interface SofiaSecurityModule {
  void config(ConfigurationFileYAMLData configurationFileYAMLData, SiteManager siteManager) throws ConfigurationException;

  void processElement(Site site, Path requestFilePath, Element element, Caller caller, Path voidRootFilePath) throws ModuleException;

  RestUser getUser();
}
