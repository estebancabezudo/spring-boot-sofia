package net.cabezudo.sofia.websocket;

import net.cabezudo.json.JSONPair;
import net.cabezudo.json.exceptions.DuplicateKeyException;
import net.cabezudo.json.values.JSONObject;
import net.cabezudo.sofia.accounts.service.Account;
import net.cabezudo.sofia.core.SofiaRuntimeException;
import net.cabezudo.sofia.sites.Site;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.SubProtocolCapable;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.websocket.EncodeException;
import java.io.IOException;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

@Component
public class ServerWebSocketHandler extends TextWebSocketHandler implements SubProtocolCapable {
  private static final Logger log = LoggerFactory.getLogger(ServerWebSocketHandler.class);
  private static final ClientSockets clientSockets = new ClientSockets();

  private Site site;
  private Account account;

  private void broadcast(WebSocketSession session, TextMessage message) throws IOException, EncodeException {
    clientSockets.forEach(socket -> {
      if (!session.isOpen()) {
        return;
      }
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
        socket.send(message.getPayload());
      } catch (IOException | EncodeException e) {
        e.printStackTrace();
      }
    });
  }

  public void afterConnectionEstablished(WebSocketSession session) throws Exception {
    site = (Site) session.getAttributes().get("site");
    account = (Account) session.getAttributes().get("account");
    ClientSocket clientSocket = new ClientSocket(site, account, session);
    clientSockets.add(clientSocket);

    clientSocket.send(prepareMessage("Connected..."));
  }

  private String prepareMessage(String message) {
    JSONObject jsonMessage = new JSONObject();
    try {
      jsonMessage.add(new JSONPair("message", message));
    } catch (DuplicateKeyException e) {
      throw new SofiaRuntimeException(e);
    }
    return jsonMessage.toString();
  }

  @Override
  public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
    broadcast(session, message);
  }

  @Override
  public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws EncodeException, IOException, DuplicateKeyException {
    clientSockets.remove(session);
    JSONObject message = new JSONObject(new JSONPair("message", "Disconnected! Users: " + clientSockets.size()));
    broadcast(session, new TextMessage(message.toString()));
  }

  @Override
  public void handleTransportError(WebSocketSession session, Throwable cause) {
    cause.printStackTrace();
  }

  @Scheduled(fixedRate = 10000)
  void sendPeriodicMessages() throws IOException, EncodeException, DuplicateKeyException {
    JSONObject message = new JSONObject(new JSONPair("message", "server periodic message " + LocalTime.now()));
    broadcast(null, new TextMessage(message.toString()));
  }

  @Override
  public List<String> getSubProtocols() {
    return Collections.singletonList("subprotocol.demo.websocket");
  }
}