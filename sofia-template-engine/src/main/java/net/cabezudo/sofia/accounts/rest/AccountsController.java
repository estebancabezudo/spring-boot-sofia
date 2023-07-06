package net.cabezudo.sofia.accounts.rest;

import net.cabezudo.sofia.accounts.Account;
import net.cabezudo.sofia.accounts.AccountManager;
import net.cabezudo.sofia.accounts.Accounts;
import net.cabezudo.sofia.accounts.mappers.BusinessToRestAccountListMapper;
import net.cabezudo.sofia.core.rest.ListRestResponse;
import net.cabezudo.sofia.core.rest.SofiaRestResponse;
import net.cabezudo.sofia.security.SofiaAuthorizedController;
import net.cabezudo.sofia.sites.Site;
import net.cabezudo.sofia.users.service.Group;
import net.cabezudo.sofia.web.client.WebClientData;
import net.cabezudo.sofia.web.client.WebClientDataManager;
import net.cabezudo.sofia.web.user.WebUserData;
import net.cabezudo.sofia.web.user.WebUserDataManager;
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
  private @Autowired WebUserDataManager webUserDataManager;
  private @Autowired WebClientDataManager webClientDataManager;

  @GetMapping("/v1/accounts")
  public ResponseEntity<?> listAll() {
    log.debug("Get list of accounts for all site");
    ListRestResponse<RestAccount> listRestResponse;

    Site site = super.getSite();

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

  @PutMapping("/v1/site/account/set")
  public ResponseEntity<?> setSiteAccount(@RequestBody RestAccount restAccount) {
    log.debug("Set the session account");
    ListRestResponse<RestAccount> listRestResponse;

    Site site = super.getSite();
    Account account = accountManager.getByName(site, restAccount.getName());
    WebClientData webClientData = webClientDataManager.getFromCookie(request);
    webClientData.setAccount(account);

    listRestResponse = new ListRestResponse<>(SofiaRestResponse.OK, "The account was defined correctly. ", null);
    return ResponseEntity.ok(listRestResponse);
  }

  @PutMapping("/v1/session/account/set")
  public ResponseEntity<?> setSessionAccount(@RequestBody RestAccount restAccount) {
    log.debug("Set the session account");
    ListRestResponse<RestAccount> listRestResponse;

    Site site = super.getSite();
    Account account = accountManager.getByName(site, restAccount.getName());
    WebUserData webUserData = webUserDataManager.getFromSession(request);
    webUserData.setAccount(account);

    listRestResponse = new ListRestResponse<>(SofiaRestResponse.OK, "The account was defined correctly. ", null);
    return ResponseEntity.ok(listRestResponse);
  }
}

