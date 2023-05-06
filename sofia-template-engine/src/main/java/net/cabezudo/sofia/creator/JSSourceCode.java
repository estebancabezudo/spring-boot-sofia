package net.cabezudo.sofia.creator;

import net.cabezudo.html.nodes.Attribute;
import net.cabezudo.html.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

/**
 * @author <a href="http://cabezudo.net">Esteban Cabezudo</a>
 * @version 0.01.00, 2022.03.16
 */
class JSSourceCode extends CodeSource {
  private static final Logger log = LoggerFactory.getLogger(JSSourceCode.class);
  
  private final StringBuilder code = new StringBuilder();
  private final String sourceId;
  private final Caller caller;

  JSSourceCode(Path basePath, Path filePath, String id, TemplateVariables templateVariables, Caller caller) throws IOException, UndefinedLiteralException {
    Path fullFilePath = getPath(basePath, filePath, caller);

    log.debug("Resolved to " + fullFilePath);

    this.sourceId = File.pathSeparator + filePath + (id == null ? ":" : id);
    this.caller = caller;
    log.debug("Check for JavaScript file: " + fullFilePath);
    if (!Files.exists(fullFilePath)) {
      throw new FileNotFoundException(fullFilePath.toString());
    }

    log.info("Read JS code from file " + fullFilePath + " and id " + id);
    List<String> linesFromFile = Files.readAllLines(fullFilePath);
    int lineNumber = 0;
    for (String line : linesFromFile) {
      lineNumber++;
      code.append(templateVariables.replace(id, lineNumber, line)).append('\n');
    }
  }

  JSSourceCode(Path filePath, Element element, String configurationPrefix, String code, TemplateVariables templateVariables, Caller caller) throws SiteCreationException {
    this.sourceId = element.getPosition().toString();
    this.caller = caller;
    Attribute attributeId = element.getAttribute("id");
    log.info("Add code with code id " + sourceId);

    int lineNumber = 0;
    try (Scanner scanner = new Scanner(code)) {
      while (scanner.hasNextLine()) {
        String line = scanner.nextLine();
        lineNumber++;
        try {
          this.code.append(templateVariables.replace(configurationPrefix, lineNumber, line)).append("\n");
        } catch (UndefinedLiteralException e) {
          throw new SiteCreationException(e.getMessage() + " called in " + caller, e);
        }
      }
    }
  }

  private Path getPath(Path basePath, Path filePath, Caller caller) {
    log.debug("Get path for file " + filePath);
    if (filePath.isAbsolute()) {
      log.debug("The path " + filePath + " is absolute.");
      return basePath.resolve(filePath.toString().substring(1));
    }
    if (filePath.toString().startsWith(".")) {
      log.debug("The path " + filePath + " start with dot.");
      if (caller == null) {
        return basePath.resolve(filePath).normalize();
      }
      Path fullCallerPath = basePath.resolve(caller.getPath().getParent());
      return fullCallerPath.resolve(filePath).normalize();
    }
    log.debug("Resolve " + filePath + " using base path.");
    return basePath.resolve(filePath);
  }

  public String getSourceId() {
    return sourceId;
  }

  public StringBuilder getCode() {
    return code;
  }

  public Caller getCaller() {
    return caller;
  }

  public String toCode() {
    return code.toString();
  }
}
