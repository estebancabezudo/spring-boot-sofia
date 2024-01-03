package net.cabezudo.sofia.websocket;


import org.springframework.web.socket.WebSocketSession;

import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.function.Consumer;

public class ClientSockets implements Iterable<ClientSocket> {
  private final Set<ClientSocket> set = new ConcurrentSkipListSet<>();

  @Override
  public Iterator<ClientSocket> iterator() {
    return set.stream().iterator();
  }

  @Override
  public void forEach(Consumer<? super ClientSocket> action) {
    Iterable.super.forEach(action);
  }

  public void add(ClientSocket clientSocket) {
    set.add(clientSocket);
  }

  public void remove(WebSocketSession session) {
    ClientSocket clientSocketToRemove = new ClientSocket(null, null, session);
    set.remove(clientSocketToRemove);
  }

  public int size() {
    return set.size();
  }
}
