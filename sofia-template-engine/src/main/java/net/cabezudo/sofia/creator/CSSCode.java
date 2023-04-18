package net.cabezudo.sofia.creator;

import net.cabezudo.html.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;

/**
 * @author <a href="http://cabezudo.net">Esteban Cabezudo</a>
 * @version 0.01.00, 2022.03.12
 */
class CSSCode {

  private final Logger log = LoggerFactory.getLogger(TemplateVariables.class);

  CSSCodeGroup group = new CSSCodeGroup();

  void add(Path filePath, Element element, String configurationPrefix, TemplateVariables templateVariables, Caller caller) throws SiteCreationException {
    log.info("Add CSS code from " + caller);

    String code = element.getInnerText();
    CSSCodeSource cssCodeSource = new CSSCodeSource(filePath, element, configurationPrefix, code, templateVariables, caller);

    try {
      group.add(cssCodeSource);
    } catch (SourceAlreadyAdded e) {
      log.warn(e.getMessage());
    }
  }

  void add(Path basePath, Path filePath, String id, TemplateVariables templateVariables, Caller caller) throws IOException, SourceAlreadyAdded, UndefinedLiteralException {
    log.info("Add file to CSS code: " + filePath);
    CSSCodeSource code = new CSSCodeSource(basePath, filePath, id, templateVariables, caller);
    group.add(code);
  }

  String getImports() {
    return group.getImports();
  }

  public String getCode() {
    return group.getCode();
  }

}
