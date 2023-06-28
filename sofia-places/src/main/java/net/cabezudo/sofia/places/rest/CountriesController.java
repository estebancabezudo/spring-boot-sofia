package net.cabezudo.sofia.places.rest;

import net.cabezudo.sofia.accounts.Account;
import net.cabezudo.sofia.core.rest.ListRestResponse;
import net.cabezudo.sofia.core.rest.RestList;
import net.cabezudo.sofia.core.rest.SofiaRestResponse;
import net.cabezudo.sofia.security.SofiaAuthorizedController;
import net.cabezudo.sofia.users.Group;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;

@Controller
@ResponseBody
public class CountriesController extends SofiaAuthorizedController {
  private static final Logger log = LoggerFactory.getLogger(CountriesController.class);

  public CountriesController(HttpServletRequest request) {
    super(request);
  }

  @GetMapping("/v1/countries/codes/{countryCode}/codes")
  public ResponseEntity<?> listAll(ServletWebRequest request, @PathVariable @NotNull String countryCode) {
    log.debug("Get list of codes for " + countryCode);
    ListRestResponse<String> listRestResponse;

    Account account = super.getAccount();

    ResponseEntity result;
    if ((result = super.checkPermissionFor(account, Group.ADMIN)) != null) {
      return result;
    }

    if (countryCode.equalsIgnoreCase("MX")) {

      RestList<String> restISOCodeList = new RestList<>(new String[]{
          "MX-CMX", "MX-AGU", "MX-BCN", "MX-BCS", "MX-CAM", "MX-COA", "MX-COL", "MX-CHP", "MX-CHH", "MX-DUR", "MX-GUA", "MX-GRO", "MX-HID",
          "MX-JAL", "MX-MEX", "MX-MIC", "MX-MOR", "MX-NAY", "MX-NLE", "MX-OAX", "MX-PUE", "MX-QUE", "MX-ROO", "MX-SLP", "MX-SIN", "MX-SON",
          "MX-TAB", "MX-TAM", "MX-TLA", "MX-VER", "MX-YUC", "MX-ZAC"
      });

      listRestResponse = new ListRestResponse(SofiaRestResponse.OK, "Retrieve list of codes for " + countryCode, restISOCodeList);
    } else {
      listRestResponse = new ListRestResponse(SofiaRestResponse.OK, "Country not found: " + countryCode, null);
    }

    return ResponseEntity.ok(listRestResponse);
  }
}
