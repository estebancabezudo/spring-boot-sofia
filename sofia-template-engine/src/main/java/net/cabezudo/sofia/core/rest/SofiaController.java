package net.cabezudo.sofia.core.rest;

import jakarta.servlet.http.HttpServletRequest;
import net.cabezudo.sofia.accounts.service.Account;
import net.cabezudo.sofia.accounts.service.AccountManager;
import net.cabezudo.sofia.accounts.service.InvalidAccountException;
import net.cabezudo.sofia.core.SofiaRuntimeException;
import net.cabezudo.sofia.sites.Host;
import net.cabezudo.sofia.sites.HostNotFoundException;
import net.cabezudo.sofia.sites.service.SiteManager;
import net.cabezudo.sofia.web.client.WebClientDataManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public abstract class SofiaController {
  protected @Autowired HttpServletRequest request;
  private Host host;
  private Account account;
  private @Autowired SiteManager siteManager;
  private @Autowired WebClientDataManager webClientDataManager;
  private @Autowired AccountManager accountManager;

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

  protected Account getAccount() throws BadRequestException {
    if (account == null) {
      try {
        account = accountManager.getAccount(request);
      } catch (InvalidAccountException e) {
        throw new BadRequestException(e);
      }
    }
    return account;
  }


  protected HttpServletRequest getRequest() {
    return request;
  }
}
