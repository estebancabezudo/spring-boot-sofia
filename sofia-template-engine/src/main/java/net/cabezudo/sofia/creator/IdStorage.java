package net.cabezudo.sofia.creator;

import net.cabezudo.html.nodes.Element;
import net.cabezudo.html.nodes.FilePosition;
import net.cabezudo.sofia.core.SofiaRuntimeException;
import net.cabezudo.sofia.sites.Site;
import net.cabezudo.sofia.sites.SourceNotFoundException;
import net.cabezudo.sofia.sites.service.PathManager;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author <a href="http://cabezudo.net">Esteban Cabezudo</a>
 * @version 0.01.00, 2022.04.09
 */
@Component
@Scope("prototype")
public class IdStorage {

  private final Map<String, Element> map = new TreeMap<>();

  private final PathManager pathManager;
  private int id = 0;

  public IdStorage(PathManager pathManager) {
    this.pathManager = pathManager;
  }

  void add(Site site, Element element) throws DuplicateIdException {
    String key = element.getAttribute("id").getValue();
    Element foundElement;
    if ((foundElement = map.get(key)) != null) {
      Path sourcesPath;
      try {
        sourcesPath = pathManager.getSourcesPath(site);
      } catch (SourceNotFoundException e) {
        throw new SofiaRuntimeException(e);
      }
      FilePosition foundElementPosition = foundElement.getPosition();
      Path relativePath = sourcesPath.relativize(foundElementPosition.getPath());
      FilePosition position = new FilePosition(relativePath, foundElementPosition.getLine(), foundElementPosition.getRow());

      throw new DuplicateIdException("The id '" + key + "' already exist in element in " + position, element);
    }
    map.put(key, element);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    map.entrySet().forEach(entry -> {
      sb.append(entry.getKey()).append(entry.getValue().getPosition()).append("\n");
    });
    return sb.toString();
  }

  String nextId() {
    while (true) {
      String newId = "s" + (++id);
      Element element = map.get(newId);
      if (element == null) {
        return newId;
      }
    }
  }
}
