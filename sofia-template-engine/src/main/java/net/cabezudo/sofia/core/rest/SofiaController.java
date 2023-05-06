package net.cabezudo.sofia.core.rest;

import net.cabezudo.sofia.core.SofiaRuntimeException;
import net.cabezudo.sofia.sites.Host;
import net.cabezudo.sofia.sites.HostNotFoundException;
import net.cabezudo.sofia.sites.Site;
import net.cabezudo.sofia.sites.SiteManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;

@Controller
public abstract class SofiaController {
  private final HttpServletRequest request;
  private @Autowired SiteManager siteManager;
  private Site site;
  private Host host;

  public SofiaController(HttpServletRequest request) {
    this.request = request;
  }

  protected Site getSite() {
    if (site == null) {
      site = (Site) request.getSession().getAttribute("site");
    }
    return site;
  }

  protected Host getHost() {
    if (host == null) {
      String serverName = request.getServerName();
      try {
        host = siteManager.getHostByName(serverName);
      } catch (HostNotFoundException e) {
        throw new SofiaRuntimeException(e);
      }
    }
    return host;
  }

  protected HttpServletRequest getRequest() {
    return request;
  }
}
