package net.cabezudo.sofia.web.client.rest;

import net.cabezudo.sofia.accounts.Account;
import net.cabezudo.sofia.security.SofiaAuthorizedController;
import net.cabezudo.sofia.userpreferences.UserPreferencesManager;
import net.cabezudo.sofia.users.SofiaUser;
import net.cabezudo.sofia.web.client.Language;
import net.cabezudo.sofia.web.client.WebClientData;
import net.cabezudo.sofia.web.client.WebClientDataManager;
import net.cabezudo.sofia.web.client.mappers.BusinessToRestWebClientMapper;
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

import javax.servlet.http.HttpServletRequest;

@RestController
public class WebClientController extends SofiaAuthorizedController {

  private static final Logger log = LoggerFactory.getLogger(WebClientController.class);

  private @Autowired BusinessToRestWebClientMapper businessToRestWebClientMapper;

  private @Autowired UserPreferencesManager userPreferencesManager;
  private @Autowired WebClientDataManager webClientDataManager;

  public WebClientController(HttpServletRequest request) {
    super(request);
  }

  @RequestMapping(value = "/v1/web/clients/actual/details", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<RestWebClientData> clientDetails() {
    log.debug("Run /v1/web/clients/actual/details");
    WebClientData webClientData = super.getWebClientData();
    RestWebClientData restWebClient = businessToRestWebClientMapper.map(webClientData);
    webClientData.clearMessage();
    log.debug("Web client data: " + restWebClient);
    return ResponseEntity.ok(restWebClient);
  }

  // Cambia el lenguaje por defecto del sitio
  @PutMapping("/v1/web/clients/actual/languages/default")
  public ResponseEntity<RestWebClientData> setLanguage(HttpServletRequest request, @RequestBody RestLanguage restLanguage) {
    log.debug("Run /v1/web/clients/actual/languages/default");

    WebClientData webClientData = super.getWebClientData();

    WebClientData responseWebClient;
    if (restLanguage != null && !restLanguage.getCode().equals(webClientData.getLanguage().getCode())) {
      Language languageToSet = new Language(restLanguage.getCode());
      Account account = webClientData.getAccount();
      SofiaUser user = webClientData.getUser();

      WebClientData newWebClientData = new WebClientData(languageToSet, account);
      webClientDataManager.set(request, newWebClientData);
      userPreferencesManager.setLanguage(account, user, languageToSet);
      responseWebClient = newWebClientData;
    } else {
      responseWebClient = webClientData;
    }

    RestWebClientData newRestWebClient = businessToRestWebClientMapper.map(responseWebClient);
    return ResponseEntity.ok(newRestWebClient);
  }
}
