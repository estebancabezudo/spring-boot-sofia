package net.cabezudo.sofia.creator;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ContentManager {

  private final List<String> list = new ArrayList();

  public boolean ignoreRequestURI(String requestURI) {
    for (String uri : list) {
      if (requestURI.startsWith(uri)) {
        return true;
      }
    }
    return false;
  }

  public void add(String uri) {
    list.add(uri);
  }
}
