package net.cabezudo.sofia.creator;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

/**
 * @author <a href="http://cabezudo.net">Esteban Cabezudo</a>
 * @version 0.01.00, 2022.03.16
 */
class CSSCodeGroup {

  private static final Logger log = LoggerFactory.getLogger(CSSCodeGroup.class);
  private final Map<String, CSSCodeSource> map = new TreeMap<>();

  void add(CSSCodeSource code) throws SourceAlreadyAdded {
    CSSCodeSource source = map.get(code.getId());
    if (source != null) {
      Caller caller = source.getCaller();
      log.debug("The source " + code.getFilePath() + " in " + code.getCaller() + " already was added " + (caller == null ? "by the system" : caller.toString()), source);
    } else {
      map.put(code.getId(), code);
    }
  }

  String getImports() {
    StringBuilder sb = new StringBuilder();
    map.values().stream().peek(cssSourceCode -> {
      Caller caller = cssSourceCode.getCaller();
      sb.append("/* CSS import from ").append(File.separatorChar).append(cssSourceCode.getFilePath()).append(" called by ").append(caller == null ? "sofia" : caller).append(" */\n");
    }).forEachOrdered(cssSourceCode -> sb.append(cssSourceCode.getImports()).append("\n"));
    return sb.toString();
  }

  public String getCode() {
    StringBuilder sb = new StringBuilder();
    for (Entry<String, CSSCodeSource> entry : map.entrySet()) {
      CSSCodeSource cssSourceCode = entry.getValue();
      Caller caller = cssSourceCode.getCaller();
      sb.append("/* CSS code from ").append(File.separatorChar).append(cssSourceCode.getFilePath()).append(" called by ").append(caller == null ? "sofia" : caller).append(" */\n");
      sb.append(cssSourceCode.getCode()).append("\n");
    }
    return sb.toString();
  }
}
