package net.cabezudo.sofia.users.rest;

import net.cabezudo.sofia.accounts.Account;
import net.cabezudo.sofia.core.rest.ListRestResponse;
import net.cabezudo.sofia.core.rest.SofiaRestResponse;
import net.cabezudo.sofia.emails.EMailAddressValidationException;
import net.cabezudo.sofia.emails.EMailManager;
import net.cabezudo.sofia.math.Numbers;
import net.cabezudo.sofia.security.SofiaAuthorizedController;
import net.cabezudo.sofia.security.SofiaSecurityManager;
import net.cabezudo.sofia.sites.Site;
import net.cabezudo.sofia.users.Group;
import net.cabezudo.sofia.users.SofiaUser;
import net.cabezudo.sofia.users.mappers.BusinessToRestUserListMapper;
import net.cabezudo.sofia.users.service.UserList;
import net.cabezudo.sofia.users.service.UserManager;
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
import java.io.IOException;

@Controller
@ResponseBody
public class UsersController extends SofiaAuthorizedController {

  private static final Logger log = LoggerFactory.getLogger(UsersController.class);
  private @Autowired RestToBusinessUserMapper restToBusinessUserMapper;
  private @Autowired BusinessToRestUserListMapper businessToRestUserListMapper;
  private @Autowired BusinessToRestUserMapper businessToRestUserMapper;
  private @Autowired UserManager userManager;

  private @Autowired EMailManager eMailManager;
  private @Autowired SofiaSecurityManager sofiaSecurityManager;

  public UsersController(HttpServletRequest request) {
    super(request);
  }

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

  @GetMapping("/v1/users/{id}")
  public ResponseEntity<?> getUser(@PathVariable String id) {
    log.debug("Run /v1/users/{id}");

    Site site = super.getSite();
    Account account = super.getAccount();

    ResponseEntity result;
    if ((result = super.checkPermissionFor(account, Group.ADMIN)) != null) {
      return result;
    }

    UserRestResponse userRestResponse;

    SofiaUser user;
    if (Numbers.isInteger(id)) {
      user = userManager.findByAccountId(Integer.parseInt(id));
    } else {
      user = userManager.findByAccountId(account.id(), id);
    }

    RestUser restUser;
    if (user == null) {
      return new ResponseEntity(HttpStatus.NOT_FOUND);
    }
    restUser = businessToRestUserMapper.map(user);
    restUser.setAccount(account);
    userRestResponse = new UserRestResponse(SofiaRestResponse.OK, "Retrieve user " + id, restUser);
    return ResponseEntity.ok(userRestResponse);
  }

  @GetMapping("/v1/users/{id}/exists")
  public ResponseEntity<?> exists(@PathVariable String id) {
    log.debug("Run /v1/users/{id}");

    Site site = super.getSite();
    Account account = super.getAccount();

    ResponseEntity result;
    if ((result = super.checkPermissionFor(account, Group.ADMIN)) != null) {
      return result;
    }

    UserRestResponse userRestResponse;

    SofiaUser user;
    if (Numbers.isInteger(id)) {
      user = userManager.findByAccountId(Integer.parseInt(id));
    } else {
      user = userManager.findByAccountId(account.id(), id);
    }

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
  public ResponseEntity<?> validateUsername(@PathVariable String username) {
    log.debug("Run /v1/users/usernames/{value}/info for validate username");

    Site site = super.getSite();
    Account account = super.getAccount();

    ResponseEntity result;
    if ((result = super.checkPermissionFor(account, Group.ADMIN)) != null) {
      return result;
    }

    try {
      eMailManager.info(username);
    } catch (EMailAddressValidationException e) {
      return ResponseEntity.ok(new SofiaRestResponse(SofiaRestResponse.ERROR, e.getMessage()));
    }

    SofiaUser user;
    user = userManager.findByAccountId(account.id(), username);

    if (user == null) {
      return ResponseEntity.ok(new SofiaRestResponse(SofiaRestResponse.OK, "validUsername"));
    }
    return ResponseEntity.ok(new SofiaRestResponse(SofiaRestResponse.ERROR, "usernameAlreadyExists"));
  }

  @GetMapping("/v1/users/{id}/usernames/{username}/info")
  public ResponseEntity<?> validateUsernameForEdit(@PathVariable Integer id, @PathVariable String username) {
    log.debug("Run /v1/users/{id}/usernames/{username}/info for validate username");

    Account account = super.getAccount();

    ResponseEntity result;
    if ((result = super.checkPermissionFor(account, Group.ADMIN)) != null) {
      return result;
    }


    SofiaUser user = userManager.findByAccountId(id);
    if (user == null) {
      return ResponseEntity.notFound().build();
    }

    try {
      eMailManager.info(username);
    } catch (EMailAddressValidationException e) {
      return ResponseEntity.ok(new SofiaRestResponse(SofiaRestResponse.ERROR, e.getMessage()));
    }

    SofiaUser userWithTheNewUsername;
    userWithTheNewUsername = userManager.findByAccountId(account.id(), username);

    if (userWithTheNewUsername == null || userWithTheNewUsername.getId() == id) {
      return ResponseEntity.ok(new SofiaRestResponse(SofiaRestResponse.OK, "validUsername"));
    }
    return ResponseEntity.ok(new SofiaRestResponse(SofiaRestResponse.ERROR, "usernameAlreadyExists"));
  }

  @GetMapping("/v1/users")
  public ResponseEntity<?> getUsers() throws IOException {
    log.debug("Get list of users");

    Site site = super.getSite();
    Account account = super.getAccount();
    ResponseEntity result;
    if ((result = super.checkPermissionFor(account, Group.ADMIN)) != null) {
      return result;
    }

    SofiaRestResponse userRestResponse;
    UserList userList = userManager.findAll(account);
    RestUserList restList = businessToRestUserListMapper.map(userList);
    userRestResponse = new ListRestResponse(SofiaRestResponse.OK, "List retrieved", restList);
    return ResponseEntity.ok(userRestResponse);
  }

  @PostMapping("/v1/users")
  public ResponseEntity<?> create(@RequestBody RestUser restUserToSave) throws IOException {
    log.debug("Create a new user");

    Account account = super.getAccount();
    restUserToSave.setAccount(super.getAccount());

    ResponseEntity result;
    if ((result = super.checkPermissionFor(account, Group.ADMIN)) != null) {
      return result;
    }

    SofiaUser newUser = restToBusinessUserMapper.map(restUserToSave);

    SofiaUser user = userManager.create(account, newUser.getUsername(), newUser.getGroups(), newUser.isEnabled()
    );
    RestUser restUser = businessToRestUserMapper.map(user);

    UserRestResponse userRestResponse = new UserRestResponse(SofiaRestResponse.OK, "Retrieve user " + restUser.getId(), restUser);
    return ResponseEntity.ok(userRestResponse);
  }

  @PutMapping("/v1/users/{id}")
  public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody RestUser restUserToUpdate) throws IOException {
    log.debug("Update an existing user");

    Account account = super.getAccount();

    ResponseEntity result;
    if ((result = super.checkPermissionFor(account, Group.ADMIN)) != null) {
      return result;
    }

    restUserToUpdate.setAccount(account);

    SofiaUser user = restToBusinessUserMapper.map(restUserToUpdate);
    SofiaUser updatedUser = userManager.update(id, user);
    RestUser restUser = businessToRestUserMapper.map(updatedUser);
    restUser.setAccount(account);
    UserRestResponse userRestResponse = new UserRestResponse(SofiaRestResponse.OK, "Retrieve user " + id, restUser);
    return ResponseEntity.ok(userRestResponse);
  }

  @DeleteMapping("/v1/users/{id}")
  public ResponseEntity<?> delete(@PathVariable Integer id) throws IOException {
    log.debug("Delete a user");

    Account account = super.getAccount();

    ResponseEntity result;
    if ((result = super.checkPermissionFor(account, Group.ADMIN)) != null) {
      return result;
    }

    userManager.delete(account.id(), id);
    UserRestResponse userRestResponse = new UserRestResponse(SofiaRestResponse.OK, "Delete user " + id, null);
    return ResponseEntity.ok(userRestResponse);
  }
}