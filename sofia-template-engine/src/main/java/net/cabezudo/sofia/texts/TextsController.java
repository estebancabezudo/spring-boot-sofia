package net.cabezudo.sofia.texts;

import net.cabezudo.sofia.core.SofiaRuntimeException;
import net.cabezudo.sofia.core.rest.SofiaController;
import net.cabezudo.sofia.core.rest.SofiaRestResponse;
import net.cabezudo.sofia.files.FileHelper;
import net.cabezudo.sofia.sites.Host;
import net.cabezudo.sofia.sites.HostNotFoundException;
import net.cabezudo.sofia.sites.Site;
import net.cabezudo.sofia.sites.SiteNotFoundException;
import net.cabezudo.sofia.sites.service.PathManager;
import net.cabezudo.sofia.sites.service.SiteManager;
import net.cabezudo.sofia.userpreferences.UserPreferencesManager;
import net.cabezudo.sofia.users.service.SofiaUser;
import net.cabezudo.sofia.web.client.Language;
import net.cabezudo.sofia.web.client.WebClientData;
import net.cabezudo.sofia.web.client.WebClientDataManager;
import net.cabezudo.sofia.web.user.WebUserData;
import net.cabezudo.sofia.web.user.WebUserDataManager;
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
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Controller
@ResponseBody
public class TextsController extends SofiaController {
  private static final Logger log = LoggerFactory.getLogger(TextsController.class);

  private @Autowired PathManager pathManager;
  private @Autowired WebClientDataManager webClientDataManager;
  private @Autowired UserPreferencesManager userPreferencesManager;
  private @Autowired WebUserDataManager webUserDataManager;
  private @Autowired SiteManager siteManager;

  @RequestMapping(
      value = "/v1/pages/actual/texts",
      method = RequestMethod.GET,
      produces = "application/json"
  )
  public ResponseEntity<? extends SofiaRestResponse> texts(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "language") String requestedLanguageCode, @RequestParam String page) {
    log.debug("Run /v1/pages/actual/texts, language=" + requestedLanguageCode + ", page=" + page);
    Site site = siteManager.getSite(request);
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
      Path path = pathManager.getVersionedSiteTextsPath(site, version).resolve(pageWithoutExtension).resolve(requestedLanguageCode + ".json");
      log.debug("Path to text file: " + path);
      if (!Files.exists(path)) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new SofiaRestResponse(SofiaRestResponse.ERROR, "File with texts for page " + page + " for the language " + requestedLanguageCode + " not found."
        ));
      }
      String textFromFile = Files.readString(path);

      WebClientData webClientData = webClientDataManager.getFromCookie(request);
      log.debug("Client data: " + webClientData);
      if (webClientData == null) {
        throw new SofiaRuntimeException("The client data is null");
      }
      Language languageFromClient = webClientData.getLanguage();
      log.debug("Language request code: " + requestedLanguageCode);
      log.debug("Language in web client: " + languageFromClient);

      if (languageFromClient == null || !languageFromClient.getCode().equals(requestedLanguageCode)) {
        Language requestedLanguage = new Language(requestedLanguageCode);
        WebUserData webUserData = webUserDataManager.getFromSession(request);
        SofiaUser user = webUserData == null ? null : webUserData.getUser();
        if (user == null) {
          WebClientData newWebClientData = new WebClientData(webClientData.getId(), requestedLanguage, webClientData.getAccount(), webClientData.getLastUpdate());
          webClientDataManager.update(webClientData.getId(), newWebClientData);
          webClientDataManager.setCookie(response, newWebClientData);
        } else {
          webUserData.setLanguage(requestedLanguage);
          webUserDataManager.set(request, webUserData);
          userPreferencesManager.setLanguage(user, requestedLanguage);
        }
      }
      return ResponseEntity.ok(new TextsRestResponse(textFromFile));
    } catch (SiteNotFoundException | HostNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new SofiaRestResponse(SofiaRestResponse.ERROR, e.getMessage()));
    } catch (IOException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new SofiaRestResponse(SofiaRestResponse.ERROR, e.getMessage()));
    }
  }
}

