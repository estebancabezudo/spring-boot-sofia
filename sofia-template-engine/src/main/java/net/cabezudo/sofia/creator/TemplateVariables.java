package net.cabezudo.sofia.creator;

import net.cabezudo.html.nodes.Position;
import net.cabezudo.json.JSONPair;
import net.cabezudo.json.exceptions.DuplicateKeyException;
import net.cabezudo.json.exceptions.JSONParseException;
import net.cabezudo.json.exceptions.PropertyNotExistException;
import net.cabezudo.json.values.JSONObject;
import net.cabezudo.sofia.core.SofiaRuntimeException;
import net.cabezudo.sofia.core.SofiaTemplateEngineEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * @author <a href="http://cabezudo.net">Esteban Cabezudo</a>
 * @version 0.01.00, 2019.05.30
 */
@Component
public class TemplateVariables {
  private static final Logger log = LoggerFactory.getLogger(TemplateVariables.class);
  
  private final JSONObject jsonObject;
  private @Autowired SofiaTemplateEngineEnvironment sofiaTemplateEngineEnvironment;

  public TemplateVariables() {
    jsonObject = new JSONObject();
  }

  public void add(Path basePath, Path filePath) throws IOException, JSONParseException, UndefinedLiteralException, DuplicateKeyException {
    add(basePath, filePath, null);
  }

  public void add(Path basePath, Path fileNamePath, String id) throws IOException, JSONParseException, UndefinedLiteralException, DuplicateKeyException {
    log.debug("basePath: " + basePath);
    log.debug("fileNamePath: " + fileNamePath);
    log.debug("id: " + id);
    Path fullPath = basePath.resolve(fileNamePath);
    if (Files.exists(fullPath)) {
      log.info("Template literals file FOUND: " + fullPath);
      List<String> lines = Files.readAllLines(fullPath);
      StringBuilder sb = new StringBuilder();
      int lineNumber = 0;
      for (String line : lines) {
        lineNumber++;
        sb.append(replace(id, lineNumber, line));
      }
      if (id != null) {
        JSONObject newJSONObject = new JSONObject(fileNamePath.toString(), sb.toString());
        JSONObject idObject = new JSONObject();
        JSONPair idPair = new JSONPair(id, newJSONObject);
        idObject.add(idPair);
        merge(idObject);
      } else {
        JSONObject newJSONObject = new JSONObject(basePath.toString(), sb.toString());
        merge(newJSONObject);
      }
    } else {
      log.info("Template literals file to add to template variables NOT FOUND: " + fullPath);
    }
  }

  String replace(String id, int lineNumber, String code) throws UndefinedLiteralException {
    StringBuilder sb = new StringBuilder();
    int i;
    int lastIndexFound = 0;

    while ((i = code.indexOf("#{", lastIndexFound)) != -1) {
      sb.append(code, lastIndexFound, i);
      lastIndexFound = code.indexOf('}', i);
      String name;
      if (id == null) {
        name = code.substring(i + 2, lastIndexFound);
      } else {
        name = id + "." + code.substring(i + 2, lastIndexFound);
      }
      lastIndexFound++;
      String value;
      try {
        value = digString(name);
      } catch (PropertyNotExistException e) {
        if (sofiaTemplateEngineEnvironment.isDevelopment()) {
          log.info(jsonObject.toString());
        }
        throw new UndefinedLiteralException(name, new Position(lineNumber, i + 3), e);
      }
      log.debug("Replace template variable " + name + " with " + value);
      sb.append(value);
    }
    sb.append(code.substring(lastIndexFound));
    return sb.toString();
  }

  public String toJSON() {
    return jsonObject.toJSON();
  }

  @Override
  public String toString() {
    return this.toJSON();
  }

  void merge(JSONObject jsonData) {
    if (jsonData == null) {
      throw new SofiaRuntimeException("The parameter for merge is null.");
    }
    log.debug("Merge template variables to " + jsonObject.toJSON());
    jsonObject.merge(jsonData);
  }

  void merge(JSONObject jsonData, String id) throws DuplicateKeyException {
    if (id != null) {
      JSONPair configurationPair = new JSONPair(id, jsonData);
      JSONObject jsonConfigurationWithId = new JSONObject();
      jsonConfigurationWithId.add(configurationPair);
      jsonObject.merge(jsonConfigurationWithId);
    } else {
      jsonObject.merge(jsonData);
    }
  }

  public String get(String themeName) {
    return jsonObject.getNullString(themeName);
  }

  String digString(String name) throws PropertyNotExistException {
    return jsonObject.digString(name);
  }
}
