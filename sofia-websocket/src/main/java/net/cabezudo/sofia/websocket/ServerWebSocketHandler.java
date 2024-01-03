package net.cabezudo.sofia.websocket;

import net.cabezudo.json.JSONPair;
import net.cabezudo.json.exceptions.DuplicateKeyException;
import net.cabezudo.json.values.JSONObject;
import net.cabezudo.sofia.accounts.service.Account;
import net.cabezudo.sofia.core.SofiaRuntimeException;
import net.cabezudo.sofia.sites.Site;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.SubProtocolCapable;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Component
public class ServerWebSocketHandler extends TextWebSocketHandler implements SubProtocolCapable {
  private static final Logger log = LoggerFactory.getLogger(ServerWebSocketHandler.class);
  private static final ClientSockets clientSockets = new ClientSockets();
  private static final String MESSAGE_TYPE_INFO = "info";
  private static final String MESSAGE_TYPE_NOTIFICATION = "notification";
  private Site site;
  private Account account;

  private void broadcast(WebSocketSession session, TextMessage message) {
    clientSockets.forEach(socket -> {
      if (!session.isOpen()) {
        return;
      }
      if (site == null || site.getId() != socket.getSite().getId()) {
        return;
      }
      if (account == null || account.getId() != socket.getAccount().getId()) {
        return;
      }
      if (session.getId().equals(socket.getSession().getId())) {
        return;
      }
      try {
        socket.send(message.getPayload());
      } catch (IOException e) {
        // TODO Do something meaningful with this
        e.printStackTrace();
      }
    });
  }

  public void afterConnectionEstablished(WebSocketSession session) throws Exception {
    site = (Site) session.getAttributes().get("websocket.site");
    account = (Account) session.getAttributes().get("websocket.account");

    ClientSocket clientSocket = new ClientSocket(site, account, session);
    clientSockets.add(clientSocket);

    clientSocket.send(prepareMessage(MESSAGE_TYPE_INFO, "Connected..."));
  }

  private String prepareMessage(String type, String message) {
    JSONObject jsonMessage = new JSONObject();
    try {
      jsonMessage.add(new JSONPair("type", type));
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
  public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
    clientSockets.remove(session);
    String message = prepareMessage(MESSAGE_TYPE_INFO, "Disconnected! Users: " + clientSockets.size());
    broadcast(session, new TextMessage(message));
  }

  @Override
  public void handleTransportError(WebSocketSession session, Throwable cause) {
    cause.printStackTrace();
  }

  @Override
  public List<String> getSubProtocols() {
    return Collections.singletonList("subprotocol.demo.websocket");
  }
}