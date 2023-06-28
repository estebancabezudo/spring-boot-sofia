package net.cabezudo.sofia.creator;

import net.cabezudo.html.nodes.Element;
import net.cabezudo.html.nodes.FilePosition;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

/**
 * @author <a href="http://cabezudo.net">Esteban Cabezudo</a>
 * @version 0.01.00, 2022.03.16
 */
class CSSCodeSource extends CodeSource {

  private static final Logger logger = Logger.getLogger("main");

  private final StringBuilder imports = new StringBuilder();
  private final StringBuilder code = new StringBuilder();
  private final String sourceId;
  private final Path filePath;
  private final Caller caller;

  CSSCodeSource(Path basePath, Path filePath, String id, TemplateVariables templateVariables, Caller caller) throws IOException, UndefinedLiteralException {
    Path fullFilePath = basePath.resolve(filePath).normalize();
    this.sourceId = fullFilePath + (id == null ? "" : id);
    this.filePath = filePath;
    this.caller = caller;
    if (!Files.exists(fullFilePath)) {
      throw new FileNotFoundException(fullFilePath.toString());
    }

    logger.info(() -> "Read CSS code from file " + filePath + " and " + (id == null ? "no id" : "id: " + id));
    List<String> linesFromFile = Files.readAllLines(fullFilePath);
    int lineNumber = 0;
    for (String line : linesFromFile) {
      lineNumber++;
      try {
        if (line.startsWith("@import url")) {
          imports.append(templateVariables.replace(id, lineNumber, line)).append('\n');
        } else {
          code.append(templateVariables.replace(id, lineNumber, line)).append('\n');
        }
      } catch (UndefinedLiteralException e) {
        throw new UndefinedLiteralException(e.getUndefinedLiteral(), new FilePosition(filePath, e.getPosition()), e.getCause());
      }
    }
  }

  CSSCodeSource(Path filePath, Element element, String configurationPrefix, String code, TemplateVariables templateVariables, Caller caller) throws SiteCreationException {
    this.sourceId = element.getPosition().toString();
    this.filePath = filePath;
    this.caller = caller;
    logger.info(() -> "Add code with code id " + sourceId);

    int lineNumber = 0;
    try (Scanner scanner = new Scanner(code)) {
      while (scanner.hasNextLine()) {
        String line = scanner.nextLine();
        lineNumber++;
        try {
          if (line.startsWith("@import url")) {
            imports.append(templateVariables.replace(configurationPrefix, lineNumber, line)).append("\n");
          } else {
            this.code.append(templateVariables.replace(configurationPrefix, lineNumber, line)).append("\n");
          }
        } catch (UndefinedLiteralException e) {
          throw new SiteCreationException(e.getMessage() + " called in " + caller, e);
        }
      }
    }
  }

  public String getId() {
    return sourceId;
  }

  public StringBuilder getImports() {
    return imports;
  }

  public StringBuilder getCode() {
    return code;
  }

  public Path getFilePath() {
    return filePath;
  }

  public Caller getCaller() {
    return caller;
  }
}
