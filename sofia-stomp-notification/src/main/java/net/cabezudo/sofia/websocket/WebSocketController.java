package net.cabezudo.sofia.websocket;

import net.cabezudo.sofia.accounts.service.Account;
import net.cabezudo.sofia.accounts.service.AccountManager;
import net.cabezudo.sofia.sites.Site;
import net.cabezudo.sofia.sites.service.SiteManager;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.EncodeException;
import javax.websocket.Session;
import java.io.IOException;

//@Controller
//@ServerEndpoint(value = "/v1/websocket", decoders = MessageDecoder.class, encoders = MessageEncoder.class)
public class WebSocketController {

  private static final ClientSockets clientSockets = new ClientSockets();
  private @Autowired AccountManager accountManager;
  private @Autowired HttpServletRequest request;
  private @Autowired SiteManager siteManager;

  private static void broadcast(Site site, Account account, Session session, String message) throws IOException, EncodeException {
    clientSockets.forEach(socket -> {
      if (site != null && site.getId() != socket.getSite().getId()) {
        return;
      }
      if (account != null && account.getId() != socket.getAccount().getId()) {
        return;
      }
      if (session.getId().equals(socket.getSession().getId())) {
        return;
      }
      try {
        socket.send(message);
      } catch (IOException | EncodeException e) {
        e.printStackTrace();
      }
    });
  }

//  @OnOpen
//  public void onOpen(Session session) throws IOException, EncodeException {
//    Account account = accountManager.getAccount(request);
//    Site site = siteManager.getSite(request);
//    ClientSocket clientSocket = new ClientSocket(site, account, session);
//    clientSockets.add(clientSocket);
//    clientSocket.send("Connected...");
//  }
//
//  @OnMessage
//  public void onMessage(Session session, String s) throws IOException, EncodeException {
//    Account account = accountManager.getAccount(request);
//    Site site = siteManager.getSite(request);
//    broadcast(site, account, session, s);
//  }
//
//  @OnClose
//  public void onClose(Session session) throws IOException, EncodeException {
//    clientSockets.remove(session);
//    Account account = accountManager.getAccount(request);
//    Site site = siteManager.getSite(request);
//    broadcast(site, account, session, "Disconnected! Users: " + clientSockets.size());
//  }
//
//  @OnError
//  public void onError(Session session, Throwable throwable) {
//    throwable.printStackTrace();
//  }
}