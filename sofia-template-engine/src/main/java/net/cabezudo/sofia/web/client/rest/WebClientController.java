package net.cabezudo.sofia.web.client.rest;

import net.cabezudo.sofia.accounts.Account;
import net.cabezudo.sofia.security.SofiaAuthorizedController;
import net.cabezudo.sofia.security.SofiaSecurityManager;
import net.cabezudo.sofia.sites.Site;
import net.cabezudo.sofia.userpreferences.UserPreferencesManager;
import net.cabezudo.sofia.users.SofiaUser;
import net.cabezudo.sofia.users.rest.BusinessToRestUserMapper;
import net.cabezudo.sofia.web.client.Language;
import net.cabezudo.sofia.web.client.WebClientData;
import net.cabezudo.sofia.web.client.mappers.BusinessToRestWebClientMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
public class WebClientController extends SofiaAuthorizedController {

  private static final Logger log = LoggerFactory.getLogger(WebClientController.class);

  private @Autowired SofiaSecurityManager securityManager;
  private @Autowired BusinessToRestWebClientMapper businessToRestWebClientMapper;

  private @Autowired BusinessToRestUserMapper businessToRestUserMapper;
  private @Autowired UserPreferencesManager userPreferencesManager;

  public WebClientController(HttpServletRequest request) {
    super(request);
  }

  @GetMapping("/v1/web/clients/actual/details")
  public ResponseEntity<?> login(HttpServletRequest request) {
    log.debug("Run /v1/web/clients/actual/details");

    HttpSession session = super.getRequest().getSession();
    WebClientData webClientData = (WebClientData) session.getAttribute(WebClientData.OBJECT_NAME_IN_SESSION);
    RestWebClientData restWebClient = businessToRestWebClientMapper.map(webClientData);
    log.debug("Web client data: " + restWebClient);
    return ResponseEntity.ok(restWebClient);
  }

  @PutMapping("/v1/web/clients/actual/languages/default")
  public ResponseEntity<?> setLanguage(HttpServletRequest request, @RequestBody RestLanguage restLanguage) {
    log.debug("Run /v1/web/clients/actual/languages/default");

    HttpSession session = super.getRequest().getSession();

    WebClientData webClientData = (WebClientData) session.getAttribute(WebClientData.OBJECT_NAME_IN_SESSION);
    Site site = super.getSite();

    WebClientData responseWebClient;
    if (restLanguage != null && !restLanguage.equals(webClientData.getLanguage())) {
      Language languageToSet = new Language(restLanguage.getCode());
      Account account = webClientData.getAccount();
      SofiaUser user = webClientData.getUser();

      WebClientData newWebClient = new WebClientData(languageToSet, account);
      request.getSession().setAttribute(WebClientData.OBJECT_NAME_IN_SESSION, newWebClient);
      userPreferencesManager.setLanguage(site, account, user, languageToSet);
      responseWebClient = newWebClient;
    } else {
      responseWebClient = webClientData;
    }

    RestWebClientData newRestWebClient = businessToRestWebClientMapper.map(responseWebClient);
    return ResponseEntity.ok(newRestWebClient);
  }
}
