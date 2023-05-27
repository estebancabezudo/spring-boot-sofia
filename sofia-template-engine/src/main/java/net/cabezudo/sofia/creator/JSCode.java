package net.cabezudo.sofia.creator;

import net.cabezudo.html.nodes.Element;
import net.cabezudo.sofia.core.SofiaEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Scanner;

/**
 * @author <a href="http://cabezudo.net">Esteban Cabezudo</a>
 * @version 0.01.00, 2022.03.12
 */
class JSCode {

  private static final Logger log = LoggerFactory.getLogger(JSCode.class);

  SofiaEnvironment sofiaEnvironment;

  JSCodeGroup group = new JSCodeGroup();

  public JSCode(SofiaEnvironment sofiaEnvironment) {
    this.sofiaEnvironment = sofiaEnvironment;
  }

  void add(Path filePath, Element element, String configurationPrefix, TemplateVariables templateVariables, Caller caller) throws IOException, SiteCreationException {
    log.info("Add JS code from " + element.getPosition());

    String code = element.getInnerText();
    JSSourceCode jsSourceCode = new JSSourceCode(filePath, element, configurationPrefix, code, templateVariables, caller);

    try {
      group.add(jsSourceCode);
    } catch (SourceAlreadyAdded e) {
      log.warn(e.getMessage());
    }
  }

  void add(Path basePath, Path filePath, String id, TemplateVariables templateVariables, Caller caller) throws IOException, SourceAlreadyAdded, UndefinedLiteralException {
    log.info("Add JS code from file " + filePath);
    JSSourceCode code = new JSSourceCode(basePath, filePath, id, templateVariables, caller);
    group.add(code);
  }

  public String toCode() {
    String code = group.toCode();
    if (sofiaEnvironment.isProduction()) {
      StringBuilder sb = new StringBuilder();
      try (Scanner scanner = new Scanner(code)) {
        while (scanner.hasNextLine()) {
          String line = scanner.nextLine();
          if (line.isBlank() || line.trim().startsWith("console.log(") || line.startsWith("//")) {
            continue;
          }
          sb.append(line).append('\n');
        }
      }
      return sb.toString();
    } else {
      return code;
    }
  }
}
