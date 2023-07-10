package net.cabezudo.sofia.users.rest;

import net.cabezudo.sofia.accounts.service.Account;
import net.cabezudo.sofia.accounts.service.AccountManager;
import net.cabezudo.sofia.accounts.service.Accounts;
import net.cabezudo.sofia.accounts.mappers.BusinessToRestAccountListMapper;
import net.cabezudo.sofia.accounts.mappers.RestToBusinessAccountsMapper;
import net.cabezudo.sofia.accounts.rest.RestAccount;
import net.cabezudo.sofia.accounts.rest.RestAccountList;
import net.cabezudo.sofia.accounts.rest.RestAccounts;
import net.cabezudo.sofia.config.mail.SendEMailException;
import net.cabezudo.sofia.core.rest.ListRestResponse;
import net.cabezudo.sofia.core.rest.SofiaRestResponse;
import net.cabezudo.sofia.emails.EMailAddressValidationException;
import net.cabezudo.sofia.emails.EMailManager;
import net.cabezudo.sofia.people.PeopleManager;
import net.cabezudo.sofia.people.Person;
import net.cabezudo.sofia.security.SofiaAuthorizedController;
import net.cabezudo.sofia.security.SofiaSecurityManager;
import net.cabezudo.sofia.sites.Site;
import net.cabezudo.sofia.userpreferences.UserPreferencesManager;
import net.cabezudo.sofia.users.mappers.BusinessToRestUserListMapper;
import net.cabezudo.sofia.users.mappers.BusinessToRestUserMapper;
import net.cabezudo.sofia.users.mappers.BusinessToRestWebUserMapper;
import net.cabezudo.sofia.users.mappers.RestToBusinessUserMapper;
import net.cabezudo.sofia.users.service.Group;
import net.cabezudo.sofia.users.service.SofiaUser;
import net.cabezudo.sofia.users.service.UserList;
import net.cabezudo.sofia.users.service.UserManager;
import net.cabezudo.sofia.web.client.WebClientDataManager;
import net.cabezudo.sofia.web.client.mappers.BusinessToRestWebClientMapper;
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

import javax.servlet.http.HttpServletRequest;
import java.io.FileNotFoundException;
import java.io.IOException;

@Controller
@ResponseBody
public class UsersController extends SofiaAuthorizedController {

  private static final Logger log = LoggerFactory.getLogger(UsersController.class);
  private @Autowired RestToBusinessUserMapper restToBusinessUserMapper;
  private @Autowired BusinessToRestUserListMapper businessToRestUserListMapper;
  private @Autowired RestToBusinessAccountsMapper restToBusinessAccountsMapper;
  private @Autowired BusinessToRestUserMapper businessToRestUserMapper;
  private @Autowired UserManager userManager;

  private @Autowired EMailManager eMailManager;
  private @Autowired SofiaSecurityManager sofiaSecurityManager;
  private @Autowired PeopleManager peopleManager;
  private @Autowired AccountManager accountManager;
  private @Autowired BusinessToRestAccountListMapper businessToRestAccountListMapper;
  private @Autowired WebClientDataManager webClientDataManager;
  private @Autowired UserPreferencesManager userPreferencesManager;
  private @Autowired BusinessToRestWebClientMapper businessToRestWebClientMapper;
  private @Autowired WebUserDataManager webUserDataManager;
  private @Autowired BusinessToRestWebUserMapper businessToRestWebUserMapper;

  @PostMapping("/v1/login")
  public ResponseEntity<?> login() {
    log.debug("Run /v1/login");
    UserRestResponse userRestResponse;
    SofiaUser user = sofiaSecurityManager.getLoggedUser();
    if (user != null) {
      // TODO implement request cache for redirection
      String referer = "";
      userRestResponse = new UserRestResponse(SofiaRestResponse.OK, referer, "User found", user.getUsername(), user.getAuthorities());
    } else {
      userRestResponse = new UserRestResponse(SofiaRestResponse.ERROR, null, "Invalid credentials", null, null);
    }
    return ResponseEntity.ok(userRestResponse);
  }

  @GetMapping("/v1/users/actual/status")
  public ResponseEntity<?> status() {
    log.debug("Run /v1/users/actual/status");
    UserRestResponse userRestResponse;
    SofiaUser user = sofiaSecurityManager.getLoggedUser();
    if (user != null) {
      // TODO implement request cache for redirection
      String referer = "";
      userRestResponse = new UserRestResponse(SofiaRestResponse.OK, referer, "User found", user.getUsername(), user.getAuthorities());
    } else {
      userRestResponse = new UserRestResponse(SofiaRestResponse.ERROR, null, "Invalid credentials", null, null);
    }
    return ResponseEntity.ok(userRestResponse);
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
  public ResponseEntity<?> getUser(HttpServletRequest request, @PathVariable Integer id) {
    log.debug("Run /v1/users/{id}");

    Account account = accountManager.getAccount(request);

    ResponseEntity<?> result;
    if ((result = super.checkPermissionFor(account, Group.ADMIN)) != null) {
      return result;
    }

    UserRestResponse userRestResponse;

    SofiaUser user = userManager.findById(account.getId(), id);

    RestUser restUser;
    if (user == null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    restUser = businessToRestUserMapper.map(user);
    restUser.setAccount(account);
    userRestResponse = new UserRestResponse(SofiaRestResponse.OK, "Retrieve user " + id, restUser);
    return ResponseEntity.ok(userRestResponse);
  }

  @GetMapping("/v1/users/{id}/exists")
  public ResponseEntity<?> exists(HttpServletRequest request, @PathVariable Integer id) {
    log.debug("Run /v1/users/{id}");

    Account account = accountManager.getAccount(request);

    ResponseEntity<?> result;
    if ((result = super.checkPermissionFor(account, Group.ADMIN)) != null) {
      return result;
    }

    UserRestResponse userRestResponse;
    SofiaUser user = userManager.findById(account.getId(), id);

    RestUser restUser;
    if (user == null) {
      restUser = null;
    } else {
      restUser = businessToRestUserMapper.map(user);
      restUser.setAccount(account);
    }
    userRestResponse = new UserRestResponse(SofiaRestResponse.OK, "Retrieve user " + id, restUser);
    return ResponseEntity.ok(userRestResponse);
  }

  @GetMapping("/v1/users/usernames/{username}/info")
  public ResponseEntity<?> validateUsername(HttpServletRequest request, @PathVariable String username) {
    log.debug("Run /v1/users/usernames/{value}/info for validate username");

    Account account = accountManager.getAccount(request);

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
    user = userManager.findById(account.getId(), username);

    if (user == null) {
      return ResponseEntity.ok(new SofiaRestResponse<>(SofiaRestResponse.OK, "validUsername"));
    }
    return ResponseEntity.ok(new SofiaRestResponse<>(SofiaRestResponse.ERROR, "usernameAlreadyExists"));
  }

  @GetMapping("/v1/users/{id}/usernames/{username}/info")
  public ResponseEntity<?> validateUsernameForEdit(HttpServletRequest request, @PathVariable Integer id, @PathVariable String username) {
    log.debug("Run /v1/users/{id}/usernames/{username}/info for validate username");

    Account account = accountManager.getAccount(request);

    ResponseEntity<?> result;
    if ((result = super.checkPermissionFor(account, Group.ADMIN)) != null) {
      return result;
    }


    SofiaUser user = userManager.findById(account.getId(), id);
    if (user == null) {
      return ResponseEntity.notFound().build();
    }

    try {
      eMailManager.info(username);
    } catch (EMailAddressValidationException e) {
      return ResponseEntity.ok(new SofiaRestResponse<>(SofiaRestResponse.ERROR, e.getMessage()));
    }

    SofiaUser userWithTheNewUsername;
    userWithTheNewUsername = userManager.findById(account.getId(), username);

    if (userWithTheNewUsername == null || userWithTheNewUsername.getId() == id) {
      return ResponseEntity.ok(new SofiaRestResponse<>(SofiaRestResponse.OK, "validUsername"));
    }
    return ResponseEntity.ok(new SofiaRestResponse<>(SofiaRestResponse.ERROR, "usernameAlreadyExists"));
  }

  @GetMapping("/v1/users")
  public ResponseEntity<?> getUsers(HttpServletRequest request) {
    log.debug("Get list of users");

    Account account = accountManager.getAccount(request);
    ResponseEntity<?> result;
    if ((result = super.checkPermissionFor(account, Group.ADMIN)) != null) {
      return result;
    }

    SofiaRestResponse<?> userRestResponse;
    UserList userList = userManager.findAll(account);
    RestUserList restList = businessToRestUserListMapper.map(userList);
    userRestResponse = new ListRestResponse<>(SofiaRestResponse.OK, "List retrieved", restList);
    return ResponseEntity.ok(userRestResponse);
  }

  @PostMapping("/v1/users")
  public ResponseEntity<?> create(HttpServletRequest request, @RequestBody RestUser restUserToSave) throws IOException {
    log.debug("Create a new user");

    Account account = accountManager.getAccount(request);

    restUserToSave.setAccount(account);

    ResponseEntity<?> result;
    if ((result = super.checkPermissionFor(account, Group.ADMIN)) != null) {
      return result;
    }

    SofiaUser newUser = restToBusinessUserMapper.map(restUserToSave);

    SofiaUser user = userManager.create(account, newUser.getUsername(), newUser.getGroups(), newUser.getLocale(), newUser.isEnabled()
    );
    RestUser restUser = businessToRestUserMapper.map(user);

    UserRestResponse userRestResponse = new UserRestResponse(SofiaRestResponse.OK, "Retrieve user " + restUser.getId(), restUser);
    return ResponseEntity.ok(userRestResponse);
  }

  @PutMapping("/v1/users/{id}")
  public ResponseEntity<?> update(HttpServletRequest request, @PathVariable Integer id, @RequestBody RestUser restUserToUpdate) throws IOException {
    log.debug("Update an existing user");

    Account account = accountManager.getAccount(request);

    ResponseEntity<?> result;
    if ((result = super.checkPermissionFor(account, Group.ADMIN)) != null) {
      return result;
    }

    restUserToUpdate.setAccount(account);

    SofiaUser userToUpdate = restToBusinessUserMapper.map(restUserToUpdate);
    SofiaUser updatedUser = userManager.update(id, userToUpdate);
    RestUser restUser = businessToRestUserMapper.map(updatedUser);
    restUser.setAccount(account);
    UserRestResponse userRestResponse = new UserRestResponse(SofiaRestResponse.OK, "Retrieve user " + id, restUser);
    return ResponseEntity.ok(userRestResponse);
  }

  @DeleteMapping("/v1/users/{id}")
  public ResponseEntity<?> delete(HttpServletRequest request, @PathVariable Integer id) throws IOException {
    log.debug("Delete a user");

    Account account = accountManager.getAccount(request);

    ResponseEntity<?> result;
    if ((result = super.checkPermissionFor(account, Group.ADMIN)) != null) {
      return result;
    }

    userManager.delete(account.getId(), id);
    UserRestResponse userRestResponse = new UserRestResponse(SofiaRestResponse.OK, "Delete user " + id, null);
    return ResponseEntity.ok(userRestResponse);
  }

  @PutMapping("/v1/users/{id}/password")
  public ResponseEntity<SofiaRestResponse<?>> info(HttpServletRequest request, @PathVariable Integer id) {
    log.debug("/v1/users/" + id);

    Account account = accountManager.getAccount(request);
    Site site = super.getSite();

    ResponseEntity<SofiaRestResponse<?>> result;
    if ((result = super.checkPermissionFor(account, Group.ADMIN)) != null) {
      return result;
    }

    try {
      userManager.updatePassword(site, account, id);
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
  public ResponseEntity<?> listAccountsForTheActualUser(HttpServletRequest request) {
    log.debug("Get list of accounts for the actual user");
    ListRestResponse<RestAccount> listRestResponse;

    Account account = accountManager.getAccount(request);
    SofiaUser user = sofiaSecurityManager.getLoggedUser();

    ResponseEntity<?> result;
    if ((result = super.isLogged()) != null) {
      return result;
    }

    Accounts accounts = accountManager.findAll(account.getSite(), user);
    RestAccountList restAccountList = businessToRestAccountListMapper.map(accounts);
    listRestResponse = new ListRestResponse<>(SofiaRestResponse.OK, "Retrieve list of account for site " + account.getSite().getName(), restAccountList);
    return ResponseEntity.ok(listRestResponse);
  }
}