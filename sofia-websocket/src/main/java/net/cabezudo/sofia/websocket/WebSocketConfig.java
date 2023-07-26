package net.cabezudo.sofia.websocket;

import net.cabezudo.sofia.accounts.service.AccountManager;
import net.cabezudo.sofia.sites.service.SiteManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
  private @Autowired SiteManager siteManager;
  private @Autowired AccountManager accountManager;

  @Override
  public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
    registry.addHandler(webSocketHandler(), "/v1/websocket").setAllowedOrigins("*").addInterceptors(new SofiaWebServiceHandshakeInterceptor(siteManager, accountManager));
  }

  @Bean
  public WebSocketHandler webSocketHandler() {
    return new ServerWebSocketHandler();
  }
}