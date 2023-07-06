package net.cabezudo.sofia.core.rest;

import net.cabezudo.sofia.core.SofiaRuntimeException;
import net.cabezudo.sofia.sites.Host;
import net.cabezudo.sofia.sites.HostNotFoundException;
import net.cabezudo.sofia.sites.Site;
import net.cabezudo.sofia.sites.SiteManager;
import net.cabezudo.sofia.sites.SiteNotFoundException;
import net.cabezudo.sofia.web.client.WebClientDataManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;

@Controller
public abstract class SofiaController {
  private @Autowired SiteManager siteManager;
  private @Autowired WebClientDataManager webClientDataManager;
  private @Autowired HttpServletRequest request;

  protected Site getSite() {
    try {
      String serverName = request.getServerName();
      return siteManager.getByHostname(serverName);
    } catch (SiteNotFoundException e) {
      throw new SofiaRuntimeException(e);
    }
  }

  protected Host getHost() {
    String serverName = request.getServerName();
    try {
      return siteManager.getHostByName(serverName);
    } catch (HostNotFoundException e) {
      throw new SofiaRuntimeException(e);
    }
  }
}
