package net.cabezudo.sofia.core.rest;

import jakarta.servlet.http.HttpServletRequest;
import net.cabezudo.sofia.core.SofiaRuntimeException;
import net.cabezudo.sofia.sites.Host;
import net.cabezudo.sofia.sites.HostNotFoundException;
import net.cabezudo.sofia.sites.service.SiteManager;
import net.cabezudo.sofia.web.client.WebClientDataManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public abstract class SofiaController {
  private @Autowired SiteManager siteManager;
  private @Autowired WebClientDataManager webClientDataManager;
  private @Autowired HttpServletRequest request;

  protected Host getHost() {
    String serverName = request.getServerName();
    try {
      return siteManager.getHostByName(serverName);
    } catch (HostNotFoundException e) {
      throw new SofiaRuntimeException(e);
    }
  }
}
