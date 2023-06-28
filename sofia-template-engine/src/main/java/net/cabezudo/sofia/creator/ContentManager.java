package net.cabezudo.sofia.creator;

import net.cabezudo.sofia.sites.Site;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
public class ContentManager {

  private final Map<String, List<String>> map = new TreeMap<>();

  public boolean ignoreRequestURI(Site site, String requestURI) {
    List<String> listFromMap = map.get(site.getName());
    if (listFromMap == null) {
      return false;
    } else {
      for (String uri : listFromMap) {
        if (requestURI.startsWith(uri)) {
          return true;
        }
      }
      return false;
    }
  }

  public void add(Site site, String uri) {
    List<String> listFromMap = map.get(site.getName());
    if (listFromMap == null) {
      List<String> list = new ArrayList();
      list.add(uri);
      map.put(site.getName(), list);
    } else {
      listFromMap.add(uri);
    }
  }
}
