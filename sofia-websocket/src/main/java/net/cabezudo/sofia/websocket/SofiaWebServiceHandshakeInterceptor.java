package net.cabezudo.sofia.websocket;

import net.cabezudo.sofia.accounts.service.Account;
import net.cabezudo.sofia.accounts.service.AccountManager;
import net.cabezudo.sofia.sites.Site;
import net.cabezudo.sofia.sites.service.SiteManager;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

public class SofiaWebServiceHandshakeInterceptor extends HttpSessionHandshakeInterceptor {

  private final SiteManager siteManager;
  private final AccountManager accountManager;

  public SofiaWebServiceHandshakeInterceptor(SiteManager siteManager, AccountManager accountManager) {
    this.siteManager = siteManager;
    this.accountManager = accountManager;
  }

  @Override
  public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
    if (request instanceof ServletServerHttpRequest) {
      ServletServerHttpRequest servletServerHttpRequest = (ServletServerHttpRequest) request;
      HttpServletRequest servletRequest = servletServerHttpRequest.getServletRequest();
      HttpSession session = servletRequest.getSession();

      Site site = siteManager.getSite(servletRequest);
      session.setAttribute("websocket.site", site);

      Account account = accountManager.getAccount(servletRequest);
      session.setAttribute("websocket.account", account);
    }
    return super.beforeHandshake(request, response, wsHandler, attributes);
  }
}
