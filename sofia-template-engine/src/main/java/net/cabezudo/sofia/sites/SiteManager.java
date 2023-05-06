package net.cabezudo.sofia.sites;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
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
  private final Map<String, Site> siteByName = new TreeMap<>();
  private final Map<String, Site> siteByHostname = new TreeMap<>();
  private final Map<String, Host> hostByHostname = new TreeMap<>();
  private Boolean databaseManaged;
  private @Autowired ApplicationContext applicationContext;
  private @Autowired SiteRepository siteRepository;

  public void add(String siteName, @NotNull Host host) {
    Site siteFound = siteByName.get(siteName);
    Site site;
    if (siteFound == null) {
      int id = 0;
      if (isDatabaseManaged()) {
        SiteEntity siteEntity = siteRepository.findByName(siteName);
        id = siteEntity.getId();
      }
      site = new Site(id, siteName);
      siteByName.put(siteName, site);
    } else {
      site = siteFound;
    }
    hostByHostname.put(host.getName(), host);
    siteByHostname.put(host.getName(), site);
  }

  private boolean isDatabaseManaged() {
    if (databaseManaged == null) {
      databaseManaged = applicationContext.containsBeanDefinition("jdbcTemplate");
    }
    return databaseManaged;
  }

  public Site get(HttpServletRequest request) throws SiteNotFoundException {
    return getByHostname(request.getServerName());
  }

  public Site get(String name) throws SiteNotFoundException {
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
}
