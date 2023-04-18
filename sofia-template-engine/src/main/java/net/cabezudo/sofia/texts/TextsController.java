package net.cabezudo.sofia.texts;

import net.cabezudo.sofia.core.rest.SofiaController;
import net.cabezudo.sofia.core.rest.SofiaRestResponse;
import net.cabezudo.sofia.files.FileHelper;
import net.cabezudo.sofia.sites.Host;
import net.cabezudo.sofia.sites.HostNotFoundException;
import net.cabezudo.sofia.sites.PathManager;
import net.cabezudo.sofia.sites.Site;
import net.cabezudo.sofia.sites.SiteNotFoundException;
import net.cabezudo.sofia.web.client.Language;
import net.cabezudo.sofia.web.client.service.WebClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Controller
@ResponseBody
public class TextsController extends SofiaController {

  private final Logger log = LoggerFactory.getLogger(TextsController.class);

  @Autowired
  PathManager pathManager;

  public TextsController(HttpServletRequest request) {
    super(request);
  }

  @RequestMapping(
      value = "/v1/pages/actual/texts",
      method = RequestMethod.GET,
      produces = "application/json"
  )
  public ResponseEntity<? extends SofiaRestResponse> texts(@RequestParam String language, @RequestParam String page) {
    log.debug("Run /v1/pages/actual/texts, language=" + language + ", page=" + page);
    Site site = super.getSite();
    Host host = super.getHost();
    String version = host.getVersion();
    try {
      String pageWithoutStart;
      if (page.startsWith("/")) {
        pageWithoutStart = page.substring(1);
      } else {
        pageWithoutStart = page;
      }
      String pageWithFile;
      if (page.endsWith("/")) {
        pageWithFile = pageWithoutStart + "index.html";
      } else {
        pageWithFile = pageWithoutStart;
      }
      String pageWithoutExtension = FileHelper.removeExtension(pageWithFile).toString();
      Path path = pathManager.getVersionedSiteTextsPath(site, version).resolve(pageWithoutExtension).resolve(language + ".json");
      log.debug("Path to text file: " + path);
      if (!Files.exists(path)) {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(new SofiaRestResponse(
                SofiaRestResponse.ERROR,
                "File with texts for page " + page + " for the language " + language + " not found."
            ));
      }
      String textFromFile = Files.readString(path);

      WebClient webClient = (WebClient) super.getRequest().getSession().getAttribute(WebClient.OBJECT_NAME_IN_SESSION);
      log.debug("Web client: " + webClient);
      if (webClient != null) {
        Language languageFromClient = webClient.getLanguage();
        log.debug("Language request: " + language);
        log.debug("Language in web client: " + languageFromClient);
        if (languageFromClient == null || !languageFromClient.getValue().equals(language)) {
          WebClient newWebClient = new WebClient(webClient.getId(), new Language(language));
          log.debug("Change new web client for: " + newWebClient);
          HttpSession session = super.getRequest().getSession();
          session.setAttribute(WebClient.OBJECT_NAME_IN_SESSION, newWebClient);
          String sessionId = session.getId();
          log.debug("Set web client object in session " + sessionId + " for language change to " + language);
        }
      }

      return ResponseEntity.ok(new TextsRestResponse(textFromFile));
    } catch (SiteNotFoundException | HostNotFoundException e) {
      return ResponseEntity
          .status(HttpStatus.NOT_FOUND)
          .body(new SofiaRestResponse(SofiaRestResponse.ERROR, e.getMessage()));
    } catch (IOException e) {
      return ResponseEntity
          .status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(new SofiaRestResponse(SofiaRestResponse.ERROR, e.getMessage()));
    }
  }
}

