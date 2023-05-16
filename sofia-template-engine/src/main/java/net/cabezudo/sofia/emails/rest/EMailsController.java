package net.cabezudo.sofia.emails.rest;

import net.cabezudo.json.exceptions.DuplicateKeyException;
import net.cabezudo.sofia.core.rest.SofiaRestResponse;
import net.cabezudo.sofia.emails.EMailAddressValidationException;
import net.cabezudo.sofia.emails.EMailManager;
import net.cabezudo.sofia.security.SofiaAuthorizedController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@ResponseBody
public class EMailsController extends SofiaAuthorizedController {
  private static final Logger log = LoggerFactory.getLogger(EMailsController.class);

  private @Autowired EMailManager eMailManager;

  public EMailsController(HttpServletRequest request) {
    super(request);
  }

  @GetMapping("/v1/emails/{address}/info")
  public ResponseEntity<?> info(@PathVariable String address) throws DuplicateKeyException {
    log.debug("Run /v1/emails/" + address + "/info");

    EMailRestResponseData eMailRestResponseData = new EMailRestResponseData(address);

    try {
      eMailManager.info(address);
    } catch (EMailAddressValidationException e) {
      return ResponseEntity.ok(new SofiaRestResponse(SofiaRestResponse.ERROR, e.getMessage(), eMailRestResponseData));
    }

    return ResponseEntity.ok(new SofiaRestResponse(SofiaRestResponse.OK, "validHostname", eMailRestResponseData));
  }
}

