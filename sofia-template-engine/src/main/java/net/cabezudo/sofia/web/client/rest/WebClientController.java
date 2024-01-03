package net.cabezudo.sofia.web.client.rest;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.cabezudo.sofia.accounts.service.Account;
import net.cabezudo.sofia.accounts.service.AccountManager;
import net.cabezudo.sofia.core.WebMessageManager;
import net.cabezudo.sofia.security.SofiaAuthorizedController;
import net.cabezudo.sofia.userpreferences.UserPreferencesManager;
import net.cabezudo.sofia.users.service.SofiaUser;
import net.cabezudo.sofia.web.client.Language;
import net.cabezudo.sofia.web.client.WebClientData;
import net.cabezudo.sofia.web.client.WebClientDataManager;
import net.cabezudo.sofia.web.client.mappers.BusinessToRestWebClientMapper;
import net.cabezudo.sofia.web.user.WebUserData;
import net.cabezudo.sofia.web.user.WebUserDataManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebClientController extends SofiaAuthorizedController {

  private static final Logger log = LoggerFactory.getLogger(WebClientController.class);

  private @Autowired BusinessToRestWebClientMapper businessToRestWebClientMapper;

  private @Autowired UserPreferencesManager userPreferencesManager;
  private @Autowired WebClientDataManager webClientDataManager;
  private @Autowired WebUserDataManager webUserDataManager;
  private @Autowired WebMessageManager webMessageManager;
  private @Autowired AccountManager accountManager;

  @RequestMapping(value = "/v1/web/clients/actual/details", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<FullRestWebClientData> clientDetails(HttpServletRequest request) {
    log.debug("Run /v1/web/clients/actual/details");
    WebClientData webClientData = webClientDataManager.getFromCookie(request);
    WebUserData webUserData = webUserDataManager.getFromSession(request);

    Account account = accountManager.getAccount(request);
    FullRestWebClientData fullRestWebClientData = new FullRestWebClientData(account, webMessageManager, webClientData, webUserData);
    log.debug("Full web client data: " + fullRestWebClientData);
    return ResponseEntity.ok(fullRestWebClientData);
  }

  // Cambia el lenguaje por defecto del sitio
  @PutMapping("/v1/web/clients/actual/languages/default")
  public ResponseEntity<FullRestWebClientData> setLanguage(HttpServletRequest request, HttpServletResponse response, @RequestBody RestLanguage restLanguage) {
    log.debug("Run /v1/web/clients/actual/languages/default");

    FullRestWebClientData fullRestWebClientData;

    WebUserData webUserData = webUserDataManager.getFromSession(request);
    WebClientData webClientDataFromCookie = webClientDataManager.getFromCookie(request);
    if (webUserData.getUser() == null) {
      if (restLanguage != null && !restLanguage.getCode().equals(webClientDataFromCookie.getLanguage().getCode())) {
        int id = webClientDataFromCookie.getId();
        Language language = new Language(restLanguage.getCode());
        Account account = webClientDataFromCookie.getAccount();
        WebClientData webClientData = new WebClientData(id, language, account, null);
        webClientDataManager.setCookie(response, webClientData);
        fullRestWebClientData = new FullRestWebClientData(accountManager.getAccount(request), webMessageManager, webClientData, webUserData);
      } else {
        fullRestWebClientData = new FullRestWebClientData(accountManager.getAccount(request), webMessageManager, webClientDataFromCookie, webUserData);
      }
    } else {
      if (restLanguage != null && !restLanguage.getCode().equals(webUserData.getLanguage().getCode())) {
        Language language = new Language(restLanguage.getCode());
        webUserData.setLanguage(language);
        webUserDataManager.set(request, webUserData);
        SofiaUser user = webUserData.getUser();
        userPreferencesManager.setLanguage(user, language);
      }
      fullRestWebClientData = new FullRestWebClientData(accountManager.getAccount(request), webMessageManager, webClientDataFromCookie, webUserData);
    }
    return ResponseEntity.ok(fullRestWebClientData);
  }
}
