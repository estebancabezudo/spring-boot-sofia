package net.cabezudo.sofia.accounts.rest;

import net.cabezudo.sofia.accounts.mappers.BusinessToRestAccountListMapper;
import net.cabezudo.sofia.accounts.service.Account;
import net.cabezudo.sofia.accounts.service.AccountManager;
import net.cabezudo.sofia.accounts.service.Accounts;
import net.cabezudo.sofia.core.SofiaBadRequest;
import net.cabezudo.sofia.core.rest.ListRestResponse;
import net.cabezudo.sofia.core.rest.SofiaRestResponse;
import net.cabezudo.sofia.security.SofiaAuthorizedController;
import net.cabezudo.sofia.sites.Site;
import net.cabezudo.sofia.sites.service.SiteManager;
import net.cabezudo.sofia.users.service.Group;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@ResponseBody
public class AccountsController extends SofiaAuthorizedController {
  private static final Logger log = LoggerFactory.getLogger(AccountsController.class);

  private @Autowired AccountManager accountManager;
  private @Autowired BusinessToRestAccountListMapper businessToRestAccountListMapper;
  private @Autowired HttpServletRequest request;
  private @Autowired SiteManager siteManager;

  @GetMapping("/v1/accounts")
  public ResponseEntity<?> listAll() {
    log.debug("Get list of accounts for all site");
    ListRestResponse<RestAccount> listRestResponse;

    Account account = accountManager.getAccount(request);

    ResponseEntity<?> result;
    if ((result = super.checkPermissionFor(account, Group.ADMIN)) != null) {
      return result;
    }

    Accounts accounts = accountManager.findAll(account.getSite());
    RestAccountList restAccountList = businessToRestAccountListMapper.map(accounts);
    listRestResponse = new ListRestResponse<>(SofiaRestResponse.OK, "Retrieve list of account for site " + account.getSite().getName(), restAccountList);
    return ResponseEntity.ok(listRestResponse);
  }

  private String getName(RestAccount restAccount) throws SofiaBadRequest {
    if (restAccount.getName() == null) {
      if (restAccount.getId() == null) {
        throw new SofiaBadRequest();
      }
      Account accountData = accountManager.get(restAccount.getId());
      return accountData.getName();
    } else {
      return restAccount.getName();
    }
  }

  @PutMapping("/v1/site/account/set")
  // Set the account only for the site. Just change the webClientData
  public ResponseEntity<?> setSiteAccount(@RequestBody RestAccount restAccount) {
    log.debug("Set the site account");
    ListRestResponse<RestAccount> listRestResponse;

    Site site = siteManager.getSite(request);

    try {
      String name = getName(restAccount);
      accountManager.setSiteAccount(request, site, name);
    } catch (SofiaBadRequest e) {
      return ResponseEntity.badRequest().build();
    }

    listRestResponse = new ListRestResponse<>(SofiaRestResponse.OK, "The account was defined correctly. ", null);
    return ResponseEntity.ok(listRestResponse);
  }

  @PutMapping("/v1/user/account/set")
  // Set the account only for the users. Just change the user preferences
  public ResponseEntity<?> setUserAccount(@RequestBody RestAccount restAccount) {
    log.debug("Set the user account");
    ListRestResponse<RestAccount> listRestResponse;

    Site site = siteManager.getSite(request);

    try {
      String name = getName(restAccount);
      accountManager.setUserAccount(request, site, name);
    } catch (SofiaBadRequest e) {
      return ResponseEntity.badRequest().build();
    }

    listRestResponse = new ListRestResponse<>(SofiaRestResponse.OK, "The account was defined correctly. ", null);
    return ResponseEntity.ok(listRestResponse);
  }

  @PutMapping("/v1/session/account/set")
  // If the client have an account, change the webClientData. If the client doesn't have account and there is a user logged change the user account
  public ResponseEntity<?> setSessionAccount(@RequestBody RestAccount restAccount) {
    log.debug("Set the session account");
    ListRestResponse<RestAccount> listRestResponse;

    Site site = siteManager.getSite(request);

    try {
      String name = getName(restAccount);
      accountManager.setSessionAccount(request, site, name);
    } catch (SofiaBadRequest e) {
      return ResponseEntity.badRequest().build();
    }

    listRestResponse = new ListRestResponse<>(SofiaRestResponse.OK, "The account was defined correctly. ", null);
    return ResponseEntity.ok(listRestResponse);
  }
}

