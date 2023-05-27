package net.cabezudo.sofia.creator;

import net.cabezudo.json.JSON;
import net.cabezudo.json.JSONPair;
import net.cabezudo.json.exceptions.DuplicateKeyException;
import net.cabezudo.json.exceptions.JSONParseException;
import net.cabezudo.json.values.JSONObject;
import net.cabezudo.sofia.core.SofiaEnvironment;
import net.cabezudo.sofia.core.SofiaRuntimeException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Logger;

/**
 * @author <a href="http://cabezudo.net">Esteban Cabezudo</a>
 * @version 0.01.00, 2021.03.19
 */
class TextsFile {

  private static final Logger logger = Logger.getLogger("main");
  private final JSONObject jsonTexts;
  SofiaEnvironment sofiaEnvironment;

  public TextsFile(SofiaEnvironment sofiaEnvironment) {
    this.sofiaEnvironment = sofiaEnvironment;
    jsonTexts = new JSONObject();
  }

  public TextsFile(Path filePath) throws JSONParseException, IOException {
    jsonTexts = JSON.parse(filePath, sofiaEnvironment.getCharset()).toJSONObject();
  }

  void merge(Path filePath) throws IOException {
    try {
      JSONObject jsonTextsFromFile = JSON.parse(filePath, sofiaEnvironment.getCharset()).toJSONObject();
      jsonTexts.merge(jsonTextsFromFile);
    } catch (JSONParseException e) {
      throw new SofiaRuntimeException(e);
    }
  }

  public void add(JSONObject jsonTexts) {
    this.jsonTexts.merge(jsonTexts);
  }

  public void add(TextsFile textsFile) {
    this.jsonTexts.merge(textsFile.jsonTexts);
  }

  public void add(JSONObject jsonTexts, boolean acceptDuplicates) throws DuplicateKeyException {
    this.jsonTexts.replace(jsonTexts, acceptDuplicates);
  }

  void save(Path basePath, Path textsPath) throws IOException {
    Path fullTextsDirectoryPath = basePath.resolve(textsPath.toString());
    if (!Files.exists(fullTextsDirectoryPath)) {
      Files.createDirectories(fullTextsDirectoryPath);
    }

    List<String> keys = jsonTexts.getKeyList();
    Set<String> languageSet = getLanguageSet(keys);
    languageSet.remove("all");
    for (String language : languageSet) {
      JSONObject jsonTextObject = new JSONObject();
      keys.forEach(key -> {
        JSONObject allLanguages = jsonTexts.getNullObject(key);
        String text = getText(allLanguages, language, key);
        try {
          jsonTextObject.add(new JSONPair(key, text));
        } catch (DuplicateKeyException e) {
          throw new SofiaRuntimeException(e);
        }
      });
      Path languageFilePath = fullTextsDirectoryPath.resolve(language + ".json");
      logger.info(() -> "Save language file " + languageFilePath);
      Files.write(languageFilePath, jsonTextObject.toJSON().getBytes(sofiaEnvironment.getCharset()));
    }
  }

  private Set<String> getLanguageSet(List<String> keys) {
    Set<String> languageSet = new TreeSet<>();
    keys.stream().map(jsonTexts::getNullObject).map(jsonLanguages -> jsonLanguages.getKeyList()).forEachOrdered(languages -> languages.forEach(languageSet::add));
    return languageSet;
  }

  private String getText(JSONObject allLanguages, String language, String key) {
    String text = allLanguages.getNullString(language);
    if (text == null) {
      text = allLanguages.getNullString("en");
      if (text == null) {
        text = allLanguages.getNullString("all");
        if (text == null) {
          if (allLanguages.size() > 0) {
            text = allLanguages.getNullString(0);
          }
          if (text == null) {
            text = key;
          }
        }
      }
    }
    return text;
  }

  void add(String id, String text, String language) throws DuplicateKeyException {
    if (text.isBlank()) {
      return;
    }
    JSONObject jsonTextsForIdEntry = jsonTexts.getNullObject(id);
    if (jsonTextsForIdEntry == null) {
      jsonTextsForIdEntry = new JSONObject();
      jsonTextsForIdEntry.add(new JSONPair(language, text));
      JSONPair jsonLanguagePairForIdEntry = new JSONPair(id, jsonTextsForIdEntry);
      jsonTexts.add(jsonLanguagePairForIdEntry);
    } else {
      // TODO Check and warning if the language already as defined
      if (jsonTextsForIdEntry.contains(language)) {
        if (!"all".equals(language)) {
          throw new DuplicateKeyException("The text for language " + language + " is already defined", language);
        }
      } else {
        jsonTextsForIdEntry.add(new JSONPair(language, text));
      }
    }
  }

  public Set<String> getLanguages() {
    List<String> keys = jsonTexts.getKeyList();
    Set<String> languageSet = getLanguageSet(keys);
    languageSet.remove("all");
    return languageSet;
  }
}
