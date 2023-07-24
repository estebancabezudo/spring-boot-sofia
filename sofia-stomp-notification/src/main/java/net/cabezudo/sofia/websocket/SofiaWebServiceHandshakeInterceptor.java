package net.cabezudo.sofia.websocket;

import net.cabezudo.sofia.accounts.service.Account;
import net.cabezudo.sofia.accounts.service.AccountManager;
import net.cabezudo.sofia.sites.Site;
import net.cabezudo.sofia.sites.service.SiteManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Component
public class SofiaWebServiceHandshakeInterceptor extends HttpSessionHandshakeInterceptor {
  private @Autowired AccountManager accountManager;
  private @Autowired SiteManager siteManager;

  @Override
  public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
    if (request instanceof HttpServletRequest) {
      HttpServletRequest httpServletRequest = (HttpServletRequest) request;
      Account account = accountManager.getAccount(httpServletRequest);
      Site site = siteManager.getSite(httpServletRequest);
      attributes.put("account", account);
      attributes.put("site", site);
    }
    return super.beforeHandshake(request, response, wsHandler, attributes);
  }
}
