package net.cabezudo.sofia.creator;

import net.cabezudo.json.JSON;
import net.cabezudo.json.exceptions.JSONParseException;
import net.cabezudo.json.values.JSONObject;
import net.cabezudo.sofia.core.SofiaRuntimeException;
import net.cabezudo.sofia.core.SofiaTemplateEngineEnvironment;
import net.cabezudo.sofia.sites.PathManager;
import net.cabezudo.sofia.sites.Site;
import net.cabezudo.sofia.sites.SourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * @author <a href="http://cabezudo.net">Esteban Cabezudo</a>
 * @version 0.01.00, 2020.06.07
 */
@Component
class JSONConfigurationFile {

  private final StringBuilder sb = new StringBuilder();
  private final Logger log = LoggerFactory.getLogger(JSONConfigurationFile.class);
  SofiaTemplateEngineEnvironment sofiaTemplateEngineEnvironment;
  PathManager pathManager;

  private JSONObject jsonObject;

  JSONObject load(Site site, Path filePath, String id, TemplateVariables templateVariables, SofiaTemplateEngineEnvironment sofiaTemplateEngineEnvironment, PathManager pathManager) throws IOException, SiteCreationException {
    this.sofiaTemplateEngineEnvironment = sofiaTemplateEngineEnvironment;
    this.pathManager = pathManager;
    log.info("Load configuration using the id: " + id);
    if (Files.isRegularFile(filePath)) {
      log.info("FOUND configuration file " + getSourcePathRelative(site, filePath) + ".");

      // Read the JSON file into a buffer and apply template variables before parse.
      List<String> jsonLines = Files.readAllLines(filePath);

      try {
        replace(jsonLines, null, templateVariables, sb);

        // Parse the configuration file
        jsonObject = JSON.parse(filePath.toString(), sb.toString()).toJSONObject();
      } catch (JSONParseException | UndefinedLiteralException e) {
        System.out.println(sb);
        throw new SiteCreationException("Can't parse " + filePath + ". " + e.getLocalizedMessage());
      }
    } else {
      log.info("Configuration file " + getSourcePathRelative(site, filePath) + " NOT FOUND.");
    }
    return jsonObject;
  }

  private String getSourcePathRelative(Site site, Path fullPath) {
    try {
      return pathManager.getSourcesPath(site).relativize(fullPath).toString();
    } catch (SourceNotFoundException e) {
      throw new SofiaRuntimeException(e);
    }
  }

  private void replace(List<String> jsonLines, String id, TemplateVariables templateVariables, StringBuilder sb) throws UndefinedLiteralException {
    int lineNumber = 1;
    for (String line : jsonLines) {
      sb.append(templateVariables.replace(id, lineNumber, line));
      sb.append('\n');
      lineNumber++;
    }
  }
}
