package net.cabezudo.sofia.websocket;

import net.cabezudo.sofia.accounts.service.Account;
import net.cabezudo.sofia.sites.Site;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.websocket.EncodeException;
import java.io.IOException;
import java.util.Objects;

public class ClientSocket implements Comparable<ClientSocket> {
  private final Site site;
  private final Account account;
  private final WebSocketSession session;

  public ClientSocket(Site site, Account account, WebSocketSession session) {
    this.site = site;
    this.account = account;
    this.session = session;
  }

  public void send(String message) throws EncodeException, IOException {
    session.sendMessage(new TextMessage(message));
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof ClientSocket)) return false;
    ClientSocket that = (ClientSocket) o;
    return session.getId().equals(that.session.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(session.getId());
  }

  @Override
  public int compareTo(ClientSocket o) {
    return this.session.getId().compareTo(o.session.getId());
  }

  public Site getSite() {
    return site;
  }

  public Account getAccount() {
    return account;
  }

  public WebSocketSession getSession() {
    return session;
  }

}
