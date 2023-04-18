package net.cabezudo.sofia.sites;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Consumer;

public class Hosts implements Iterable<Host> {
  private final Map<String, Host> map = new TreeMap<>();

  public Hosts() {
    // Nothing to do
  }

  public Hosts(List<String> hosts) {
    for (String hostNameAndVersion : hosts) {
      Host host = new Host(hostNameAndVersion);
      map.put(host.getName(), host);
    }
  }

  public void add(Host host) {
    map.put(host.getName(), host);
  }

  public Host get(String hostname) {
    return map.get(hostname);
  }

  @Override
  public Iterator<Host> iterator() {
    return map.values().iterator();
  }

  @Override
  public void forEach(Consumer<? super Host> action) {
    Iterable.super.forEach(action);
  }
}
