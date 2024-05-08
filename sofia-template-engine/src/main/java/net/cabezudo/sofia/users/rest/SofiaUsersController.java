package net.cabezudo.sofia.users.rest;

import jakarta.servlet.http.HttpServletRequest;
import net.cabezudo.sofia.accounts.mappers.BusinessToRestAccountListMapper;
import net.cabezudo.sofia.accounts.mappers.RestToBusinessAccountsMapper;
import net.cabezudo.sofia.accounts.rest.RestAccount;
import net.cabezudo.sofia.accounts.rest.RestAccountList;
import net.cabezudo.sofia.accounts.rest.RestAccounts;
import net.cabezudo.sofia.accounts.service.Account;
import net.cabezudo.sofia.accounts.service.AccountManager;
import net.cabezudo.sofia.accounts.service.Accounts;
import net.cabezudo.sofia.config.mail.SendEMailException;
import net.cabezudo.sofia.core.rest.BadRequestException;
import net.cabezudo.sofia.core.rest.ListRestResponse;
import net.cabezudo.sofia.core.rest.SofiaRestResponse;
import net.cabezudo.sofia.emails.EMailAddressValidationException;
import net.cabezudo.sofia.emails.EMailManager;
import net.cabezudo.sofia.people.Person;
import net.cabezudo.sofia.people.service.PeopleManager;
import net.cabezudo.sofia.security.SofiaAuthorizedController;
import net.cabezudo.sofia.security.SofiaSecurityManager;
import net.cabezudo.sofia.sites.Site;
import net.cabezudo.sofia.sites.service.SiteManager;
import net.cabezudo.sofia.users.mappers.BusinessToRestUserListMapper;
import net.cabezudo.sofia.users.mappers.BusinessToRestUserMapper;
import net.cabezudo.sofia.users.mappers.RestToBusinessUserMapper;
import net.cabezudo.sofia.users.service.Group;
import net.cabezudo.sofia.users.service.SofiaUser;
import net.cabezudo.sofia.users.service.SofiaUserList;
import net.cabezudo.sofia.users.service.SofiaUserManager;
import net.cabezudo.sofia.web.user.WebUserDataManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.FileNotFoundException;
import java.io.IOException;

@Controller
@ResponseBody
public class SofiaUsersController extends SofiaAuthorizedController {

  private static final Logger log = LoggerFactory.getLogger(SofiaUsersController.class);
  private @Autowired RestToBusinessUserMapper restToBusinessUserMapper;
  private @Autowired BusinessToRestUserListMapper businessToRestUserListMapper;
  private @Autowired RestToBusinessAccountsMapper restToBusinessAccountsMapper;
  private @Autowired BusinessToRestUserMapper businessToRestUserMapper;
  private @Autowired SofiaUserManager sofiaUserManager;
  private @Autowired EMailManager eMailManager;
  private @Autowired SofiaSecurityManager sofiaSecurityManager;
  private @Autowired PeopleManager peopleManager;
  private @Autowired AccountManager accountManager;
  private @Autowired BusinessToRestAccountListMapper businessToRestAccountListMapper;
  private @Autowired WebUserDataManager webUserDataManager;
  private @Autowired SiteManager siteManager;

  @PostMapping("/v1/login")
  public ResponseEntity<?> login() {
    log.debug("Run /v1/login");
    SofiaUserRestResponse sofiaUserRestResponse;
    SofiaUser user = sofiaSecurityManager.getLoggedUser();
    if (user != null) {
      // TODO implement request cache for redirection
      String referer = "";
      sofiaUserRestResponse = new SofiaUserRestResponse(SofiaRestResponse.OK, referer, "User found", user.getUsername(), user.getAuthorities());
    } else {
      sofiaUserRestResponse = new SofiaUserRestResponse(SofiaRestResponse.ERROR, null, "Invalid credentials", null, null);
    }
    return ResponseEntity.ok(sofiaUserRestResponse);
  }

  @GetMapping("/v1/users/actual/status")
  public ResponseEntity<?> status() {
    log.debug("Run /v1/users/actual/status");
    SofiaUserRestResponse sofiaUserRestResponse;
    SofiaUser user = sofiaSecurityManager.getLoggedUser();
    if (user != null) {
      // TODO implement request cache for redirection
      String referer = "";
      sofiaUserRestResponse = new SofiaUserRestResponse(SofiaRestResponse.OK, referer, "User found", user.getUsername(), user.getAuthorities());
    } else {
      sofiaUserRestResponse = new SofiaUserRestResponse(SofiaRestResponse.ERROR, null, "Invalid credentials", null, null);
    }
    return ResponseEntity.ok(sofiaUserRestResponse);
  }

  @GetMapping("/v1/users/actual/profile")
  public ResponseEntity<?> getProfile(HttpServletRequest request) {
    log.debug("Run /v1/users/actual/profile");

    SofiaUser user = webUserDataManager.getFromSession(request).getUser();
    Account account = user.getAccount();

    ResponseEntity<?> result;
    if ((result = super.checkPermissionFor(account, Group.USER)) != null) {
      return result;
    }

    RestProfile restProfile = RestProfile.getBuilder();
    Person person = peopleManager.getByUser(user);
    String personName = person == null ? null : person.getName();
    String personLastName = person == null ? null : person.getLastName();

    restProfile
        .setUsername((user.getUsername()))
        .setAccountName(account.getName())
        .setName(personName)
        .setLastName(personLastName);

    Accounts accounts = accountManager.getAll(user);
    RestAccounts restAccounts = restToBusinessAccountsMapper.map(accounts);
    restProfile.set(restAccounts);

    SofiaRestResponse<RestProfile> profileRestResponse = new SofiaRestResponse<>(SofiaRestResponse.OK, "Retrieve profile", restProfile.build());
    return ResponseEntity.ok(profileRestResponse);
  }

  @GetMapping("/v1/users/{id}")
  public ResponseEntity<?> getUser(@PathVariable Integer id) throws BadRequestException {
    log.debug("Run /v1/users/{id}");

    Account account = super.getAccount();

    ResponseEntity<?> result;
    if ((result = super.checkPermissionFor(account, Group.ADMIN)) != null) {
      return result;
    }

    SofiaUserRestResponse sofiaUserRestResponse;

    SofiaUser user = sofiaUserManager.findById(account.getId(), id);

    RestSofiaUser restSofiaUser;
    if (user == null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    restSofiaUser = businessToRestUserMapper.map(user);
    restSofiaUser.setAccount(account);
    sofiaUserRestResponse = new SofiaUserRestResponse(SofiaRestResponse.OK, "Retrieve user " + id, restSofiaUser);
    return ResponseEntity.ok(sofiaUserRestResponse);
  }

  @GetMapping("/v1/users/{id}/exists")
  public ResponseEntity<?> exists(@PathVariable Integer id) throws BadRequestException {
    log.debug("Run /v1/users/{id}");

    Account account = super.getAccount();

    ResponseEntity<?> result;
    if ((result = super.checkPermissionFor(account, Group.ADMIN)) != null) {
      return result;
    }

    SofiaUserRestResponse sofiaUserRestResponse;
    SofiaUser user = sofiaUserManager.findById(account.getId(), id);

    RestSofiaUser restSofiaUser;
    if (user == null) {
      restSofiaUser = null;
    } else {
      restSofiaUser = businessToRestUserMapper.map(user);
      restSofiaUser.setAccount(account);
    }
    sofiaUserRestResponse = new SofiaUserRestResponse(SofiaRestResponse.OK, "Retrieve user " + id, restSofiaUser);
    return ResponseEntity.ok(sofiaUserRestResponse);
  }

  @GetMapping("/v1/users/usernames/{username}/info")
  public ResponseEntity<?> validateUsername(@PathVariable String username) throws BadRequestException {
    log.debug("Run /v1/users/usernames/{value}/info for validate username");

    Account account = super.getAccount();

    ResponseEntity<?> result;
    if ((result = super.checkPermissionFor(account, Group.ADMIN)) != null) {
      return result;
    }

    try {
      eMailManager.info(username);
    } catch (EMailAddressValidationException e) {
      return ResponseEntity.ok(new SofiaRestResponse<>(SofiaRestResponse.ERROR, e.getMessage()));
    }

    SofiaUser user;
    user = sofiaUserManager.findById(account.getId(), username);

    if (user == null) {
      return ResponseEntity.ok(new SofiaRestResponse<>(SofiaRestResponse.OK, "validUsername"));
    }
    return ResponseEntity.ok(new SofiaRestResponse<>(SofiaRestResponse.ERROR, "usernameAlreadyExists"));
  }

  @GetMapping("/v1/users/{id}/usernames/{username}/info")
  public ResponseEntity<?> validateUsernameForEdit(@PathVariable Integer id, @PathVariable String username) throws BadRequestException {
    log.debug("Run /v1/users/{id}/usernames/{username}/info for validate username");

    Account account = super.getAccount();

    ResponseEntity<?> result;
    if ((result = super.checkPermissionFor(account, Group.ADMIN)) != null) {
      return result;
    }


    SofiaUser user = sofiaUserManager.findById(account.getId(), id);
    if (user == null) {
      return ResponseEntity.notFound().build();
    }

    try {
      eMailManager.info(username);
    } catch (EMailAddressValidationException e) {
      return ResponseEntity.ok(new SofiaRestResponse<>(SofiaRestResponse.ERROR, e.getMessage()));
    }

    SofiaUser userWithTheNewUsername;
    userWithTheNewUsername = sofiaUserManager.findById(account.getId(), username);

    if (userWithTheNewUsername == null || userWithTheNewUsername.getId() == id) {
      return ResponseEntity.ok(new SofiaRestResponse<>(SofiaRestResponse.OK, "validUsername"));
    }
    return ResponseEntity.ok(new SofiaRestResponse<>(SofiaRestResponse.ERROR, "usernameAlreadyExists"));
  }

  @GetMapping("/v1/users")
  public ResponseEntity<?> getUsers() throws BadRequestException {
    log.debug("Get list of users");

    Account account = super.getAccount();

    ResponseEntity<?> result;
    if ((result = super.checkPermissionFor(account, Group.ADMIN)) != null) {
      return result;
    }

    SofiaRestResponse<?> userRestResponse;
    SofiaUserList sofiaUserList = sofiaUserManager.findAll(account);
    RestSofiaUserList restList = businessToRestUserListMapper.map(sofiaUserList);
    userRestResponse = new ListRestResponse<>(SofiaRestResponse.OK, "List retrieved", restList);
    return ResponseEntity.ok(userRestResponse);
  }

  @PostMapping("/v1/users")
  public ResponseEntity<?> create(@RequestBody RestSofiaUser restSofiaUserToSave) throws IOException, BadRequestException {
    log.debug("Create a new user");

    Account account = super.getAccount();

    restSofiaUserToSave.setAccount(account);

    ResponseEntity<?> result;
    if ((result = super.checkPermissionFor(account, Group.ADMIN)) != null) {
      return result;
    }

    SofiaUser newUser = restToBusinessUserMapper.map(restSofiaUserToSave);

    SofiaUser user = sofiaUserManager.create(account, newUser.getUsername(), newUser.getGroups(), newUser.getLocale(), newUser.isEnabled()
    );
    RestSofiaUser restSofiaUser = businessToRestUserMapper.map(user);

    SofiaUserRestResponse sofiaUserRestResponse = new SofiaUserRestResponse(SofiaRestResponse.OK, "Retrieve user " + restSofiaUser.getId(), restSofiaUser);
    return ResponseEntity.ok(sofiaUserRestResponse);
  }

  @PutMapping("/v1/users/{id}")
  public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody RestSofiaUser restSofiaUserToUpdate) throws IOException, BadRequestException {
    log.debug("Update an existing user");

    Account account = super.getAccount();

    ResponseEntity<?> result;
    if ((result = super.checkPermissionFor(account, Group.ADMIN)) != null) {
      return result;
    }

    restSofiaUserToUpdate.setAccount(account);

    SofiaUser userToUpdate = restToBusinessUserMapper.map(restSofiaUserToUpdate);
    SofiaUser updatedUser = sofiaUserManager.update(id, userToUpdate);
    RestSofiaUser restSofiaUser = businessToRestUserMapper.map(updatedUser);
    restSofiaUser.setAccount(account);
    SofiaUserRestResponse sofiaUserRestResponse = new SofiaUserRestResponse(SofiaRestResponse.OK, "Retrieve user " + id, restSofiaUser);
    return ResponseEntity.ok(sofiaUserRestResponse);
  }

  @DeleteMapping("/v1/users/{id}")
  public ResponseEntity<?> delete(@PathVariable Integer id) throws IOException, BadRequestException {
    log.debug("Delete a user");

    Account account = super.getAccount();

    ResponseEntity<?> result;
    if ((result = super.checkPermissionFor(account, Group.ADMIN)) != null) {
      return result;
    }

    sofiaUserManager.delete(account.getId(), id);
    SofiaUserRestResponse sofiaUserRestResponse = new SofiaUserRestResponse(SofiaRestResponse.OK, "Delete user " + id, null);
    return ResponseEntity.ok(sofiaUserRestResponse);
  }

  @PutMapping("/v1/users/{id}/password")
  public ResponseEntity<SofiaRestResponse<?>> info(@PathVariable Integer id) throws BadRequestException {
    log.debug("/v1/users/" + id);

    Account account = super.getAccount();

    Site site = siteManager.getSite(request);

    ResponseEntity<SofiaRestResponse<?>> result;
    if ((result = super.checkPermissionFor(account, Group.ADMIN)) != null) {
      return result;
    }

    try {
      sofiaUserManager.updatePassword(site, account, id);
    } catch (SendEMailException e) {
      return ResponseEntity.ok(new SofiaRestResponse<>(SofiaRestResponse.ERROR, e.getMessage(), null));
    } catch (FileNotFoundException e) {
      // TODO Call the error report system
      return ResponseEntity.ok(new SofiaRestResponse<>(SofiaRestResponse.ERROR, "fileNotFound", null));
    } catch (Throwable e) {
      // TODO Call the error report system
      return ResponseEntity.ok(new SofiaRestResponse<>(SofiaRestResponse.ERROR, "serverError", null));
    }

    return ResponseEntity.ok(new SofiaRestResponse<>(SofiaRestResponse.OK, "passwordUpdated", null));
  }

  @GetMapping("/v1/users/actual/accounts")
  public ResponseEntity<?> listAccountsForTheActualUser() throws BadRequestException {
    log.debug("Get list of accounts for the actual user");
    ListRestResponse<RestAccount> listRestResponse;

    Account account = super.getAccount();

    SofiaUser user = sofiaSecurityManager.getLoggedUser();

    ResponseEntity<?> result;
    if (super.getLoggedUser() == null) {
      return super.notLoggedResponse();
    }

    Accounts accounts = accountManager.findAll(account.getSite(), user);
    RestAccountList restAccountList = businessToRestAccountListMapper.map(accounts);
    listRestResponse = new ListRestResponse<>(SofiaRestResponse.OK, "Retrieve list of account for site " + account.getSite().getName(), restAccountList);
    return ResponseEntity.ok(listRestResponse);
  }
}