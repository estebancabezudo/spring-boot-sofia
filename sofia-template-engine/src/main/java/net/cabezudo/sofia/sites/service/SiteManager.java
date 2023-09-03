package net.cabezudo.sofia.sites.service;

import net.cabezudo.sofia.config.ConfigurationFileYAMLMailData;
import net.cabezudo.sofia.config.ConfigurationFileYAMLSiteData;
import net.cabezudo.sofia.core.SofiaRuntimeException;
import net.cabezudo.sofia.sites.Host;
import net.cabezudo.sofia.sites.HostNotFoundException;
import net.cabezudo.sofia.sites.Site;
import net.cabezudo.sofia.sites.SiteEntity;
import net.cabezudo.sofia.sites.SiteNotFoundException;
import net.cabezudo.sofia.sites.persistence.SiteRepository;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author <a href="http://cabezudo.net">Esteban Cabezudo</a>
 * @version 0.01.00, 2022.09.05
 */
@Service
public class SiteManager {
  private static final Logger log = LoggerFactory.getLogger(SiteManager.class);
  private final Map<String, Site> siteByName = new TreeMap<>();
  private final Map<String, Site> siteByHostname = new TreeMap<>();
  private final Map<String, Host> hostByHostname = new TreeMap<>();
  private final Map<Integer, Site> siteById = new TreeMap<>();
  private @Autowired SiteRepository siteRepository;

  public void add(ConfigurationFileYAMLSiteData configurationFileYAMLSiteData, @NotNull Host host, boolean toDatabase) {
    String siteName = configurationFileYAMLSiteData.getName();
    ConfigurationFileYAMLMailData mail = configurationFileYAMLSiteData.getMail();
    Site siteFound = siteByName.get(siteName);
    Site site;
    if (siteFound == null) {
      int id;
      if (toDatabase) {
        SiteEntity siteEntity = siteRepository.findByName(siteName);
        if (siteEntity == null) {
          log.warn("The site " + siteName + " is not in database and was not added.");
          return;
        }
        id = siteEntity.id();
      } else {
        id = siteByName.size();
      }
      site = new Site(id, siteName, mail.getReplyAddress());
      siteByName.put(siteName, site);
    } else {
      site = siteFound;
    }
    hostByHostname.put(host.getName(), host);
    siteByHostname.put(host.getName(), site);
    siteById.put(site.getId(), site);
  }

  public Site get(HttpServletRequest request) throws SiteNotFoundException {
    return getByHostname(request.getServerName());
  }

  public Site getByName(String name) throws SiteNotFoundException {
    Site site = siteByName.get(name);
    if (site == null) {
      throw new SiteNotFoundException("Site not found: " + name);
    }
    return site;
  }

  public Site getByHostname(String hostname) throws SiteNotFoundException {
    Site site = siteByHostname.get(hostname);
    if (site == null) {
      throw new SiteNotFoundException("Site not found: " + hostname);
    }
    return site;
  }

  public Host getHostByName(String hostname) throws HostNotFoundException {
    Host host = hostByHostname.get(hostname);
    if (host == null) {
      throw new HostNotFoundException("Host not found: " + hostname);
    }
    return host;
  }

  public Site getById(int id) {
    return siteById.get(id);
  }

  public Site getSite(HttpServletRequest request) {
    try {
      String serverName = request.getServerName();
      return getByHostname(serverName);
    } catch (SiteNotFoundException e) {
      throw new SofiaRuntimeException(e);
    }
  }
}
