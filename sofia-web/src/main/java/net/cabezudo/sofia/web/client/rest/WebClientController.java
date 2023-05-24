package net.cabezudo.sofia.web.client.rest;

import net.cabezudo.sofia.accounts.Account;
import net.cabezudo.sofia.security.SofiaAuthorizedController;
import net.cabezudo.sofia.security.SofiaSecurityManager;
import net.cabezudo.sofia.users.SofiaUser;
import net.cabezudo.sofia.users.rest.BusinessToRestUserMapper;
import net.cabezudo.sofia.users.rest.RestUser;
import net.cabezudo.sofia.web.client.Language;
import net.cabezudo.sofia.web.client.WebClientManager;
import net.cabezudo.sofia.web.client.mappers.BusinessToRestWebClientMapper;
import net.cabezudo.sofia.web.client.service.WebClient;
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
  private @Autowired WebClientManager webClientManager;
  private @Autowired BusinessToRestWebClientMapper businessToRestWebClientMapper;

  private @Autowired BusinessToRestUserMapper businessToRestUserMapper;

  public WebClientController(HttpServletRequest request) {
    super(request);
  }

  @GetMapping("/v1/web/clients/actual/details")
  public ResponseEntity<?> login(HttpServletRequest request) {
    log.debug("Run /v1/web/clients/actual/details");

    HttpSession session = super.getRequest().getSession();
    Object o = session.getAttribute(WebClient.OBJECT_NAME_IN_SESSION);
    WebClient webClient = (WebClient) o;

    RestWebClient restWebClient = businessToRestWebClientMapper.map(webClient);

    Account account = super.getAccount();

    SofiaUser sofiaUser = securityManager.getLoggedUser();
    RestUser restUser = sofiaUser == null ? null : businessToRestUserMapper.map(sofiaUser);

    restWebClient.setUser(restUser);
    log.debug("Web client data: " + restWebClient);
    return ResponseEntity.ok(restWebClient);
  }

  @PutMapping("/v1/web/clients/actual/languages/default")
  public ResponseEntity<?> setLanguage(HttpServletRequest request, @RequestBody RestLanguage restLanguage) {
    log.debug("Run /v1/web/clients/actual/languages/default");

    HttpSession session = super.getRequest().getSession();
    Object o = session.getAttribute(WebClient.OBJECT_NAME_IN_SESSION);
    WebClient webClient = (WebClient) o;

    WebClient responseWebClient;
    if (restLanguage != null && !restLanguage.equals(webClient.getLanguage())) {
      WebClient newWebClient = new WebClient(webClient.getId(), new Language(restLanguage.getCode()));
      request.getSession().setAttribute(WebClient.OBJECT_NAME_IN_SESSION, newWebClient);
      responseWebClient = webClientManager.update(newWebClient);
    } else {
      responseWebClient = webClient;
    }
    BusinessToRestWebClientMapper mapper = new BusinessToRestWebClientMapper();
    RestWebClient newRestWebClient = mapper.map(responseWebClient);
    return ResponseEntity.ok(newRestWebClient);
  }
}
