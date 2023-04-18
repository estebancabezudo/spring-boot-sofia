package net.cabezudo.sofia.creator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author <a href="http://cabezudo.net">Esteban Cabezudo</a>
 * @version 0.01.00, 2022.03.16
 */
class JSCodeGroup {

  private final Logger log = LoggerFactory.getLogger(JSCodeGroup.class);

  private final List<JSSourceCode> list = new ArrayList<>();
  private final Map<String, JSSourceCode> map = new TreeMap<>();

  void add(JSSourceCode code) throws SourceAlreadyAdded {
    JSSourceCode source = map.get(code.getSourceId());
    if (source != null) {
      log.debug("The source already was added in " + source.getCaller() + " with the id " + source.getSourceId(), source);
    } else {
      map.put(code.getSourceId(), code);
      list.add(code);
    }
  }

  public String toCode() {
    StringBuilder sb = new StringBuilder();
    for (JSSourceCode code : list) {

      JSSourceCode jsSourceCode = code;
      if (jsSourceCode.getCaller() == null) {
        sb.append("// Code from ").append(jsSourceCode.getSourceId()).append(" called by system ").append("\n");
      } else {
        Caller callerForCaller = jsSourceCode.getCaller().getCaller();
        if (callerForCaller == null) {
          sb.append("// Code from ").append(jsSourceCode.getCaller()).append("\n");
        } else {
          sb.append("// Code from ").append(jsSourceCode.getCaller()).append(" called by ").append(callerForCaller).append("\n");
        }
      }
      sb.append(jsSourceCode.toCode()).append("\n");
    }
    return sb.toString();
  }
}
