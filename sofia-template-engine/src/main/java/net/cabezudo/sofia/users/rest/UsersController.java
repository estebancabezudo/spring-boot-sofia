package net.cabezudo.sofia.users.rest;

import net.cabezudo.sofia.accounts.Account;
import net.cabezudo.sofia.core.rest.ListRestResponse;
import net.cabezudo.sofia.core.rest.SofiaRestResponse;
import net.cabezudo.sofia.security.SofiaAuthorizedController;
import net.cabezudo.sofia.sites.Site;
import net.cabezudo.sofia.users.Group;
import net.cabezudo.sofia.users.SofiaUser;
import net.cabezudo.sofia.users.mappers.BusinessToRestUserListMapper;
import net.cabezudo.sofia.users.service.UserList;
import net.cabezudo.sofia.users.service.UserManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@ResponseBody
public class UsersController extends SofiaAuthorizedController {

  private final Logger log = LoggerFactory.getLogger(UsersController.class);
  @Autowired
  RestToBusinessUserMapper restToBusinessUserMapper;
  @Autowired
  BusinessToRestUserListMapper businessToRestUserListMapper;
  @Autowired
  BusinessToRestUserMapper businessToRestUserMapper;
  @Autowired
  private UserManager userManager;

  public UsersController(HttpServletRequest request) {
    super(request);
  }

  @PostMapping("/v1/login")
  public ResponseEntity<?> login(ServletWebRequest request) {
    log.debug("Run /v1/login");
    UserRestResponse userRestResponse;
    SofiaUser sofiaUser = super.getSofiaUser();
    if (sofiaUser != null) {
      // TODO implement request cache for redirection
      String referer = "";
      userRestResponse = new UserRestResponse(SofiaRestResponse.OK, referer, "User found", sofiaUser.getUsername(), sofiaUser.getAuthorities());
    } else {
      userRestResponse = new UserRestResponse(SofiaRestResponse.ERROR, null, "Invalid credentials", null, null);
    }
    return ResponseEntity.ok(userRestResponse);
  }

  @GetMapping("/v1/users/actual/status")
  public ResponseEntity<?> status(ServletWebRequest request) {
    log.debug("Run /v1/users/actual/status");
    UserRestResponse userRestResponse;
    SofiaUser sofiaUser = super.getSofiaUser();
    if (sofiaUser != null) {
      // TODO implement request cache for redirection
      String referer = "";
      userRestResponse = new UserRestResponse(SofiaRestResponse.OK, referer, "User found", sofiaUser.getUsername(), sofiaUser.getAuthorities());
    } else {
      userRestResponse = new UserRestResponse(SofiaRestResponse.ERROR, null, "Invalid credentials", null, null);
    }
    return ResponseEntity.ok(userRestResponse);
  }

  @GetMapping("/v1/users/{id}")
  public ResponseEntity<?> getUser(ServletWebRequest request, @PathVariable Integer id) {
    log.debug("Run /v1/users/{id}");

    Account account = super.getAccount();

    ResponseEntity result;
    if ((result = super.checkPermissionFor(account, Group.ADMIN)) != null) {
      return result;
    }

    UserRestResponse userRestResponse;

    SofiaUser user = userManager.get(account, id);
    RestUser restUser;
    if (user == null) {
      restUser = null;
    } else {
      restUser = businessToRestUserMapper.map(user);
    }
    userRestResponse = new UserRestResponse(SofiaRestResponse.OK, "Retrieve place " + id, restUser);
    return ResponseEntity.ok(userRestResponse);
  }

  @GetMapping("/v1/users")
  public ResponseEntity<?> getUsers(HttpServletRequest request, HttpServletResponse response) throws IOException {
    log.debug("Get list of users");

    Site site = super.getSite();
    Account account = super.getAccount();
    ResponseEntity result;
    if ((result = super.checkPermissionFor(account, Group.ADMIN)) != null) {
      return result;
    }

    SofiaRestResponse userRestResponse;
    UserList userList = userManager.findAll(site, account);
    RestUserList restList = businessToRestUserListMapper.map(userList);
    userRestResponse = new ListRestResponse(SofiaRestResponse.OK, "List retrieved", restList);
    return ResponseEntity.ok(userRestResponse);
  }

  @PostMapping("/v1/users")
  public ResponseEntity<?> create(HttpServletRequest request, HttpServletResponse response, @RequestBody RestUser restUserToSave) throws IOException {
    log.debug("Create a new user");

    Account account = super.getAccount();

    ResponseEntity result;
    if ((result = super.checkPermissionFor(account, Group.ADMIN)) != null) {
      return result;
    }

    SofiaUser newUser = restToBusinessUserMapper.map(restUserToSave);

    SofiaUser user = userManager.create(
        account, newUser.getSite().getId(), newUser.getUsername(), newUser.getPassword(), newUser.getGroups(), newUser.isEnabled()
    );
    RestUser restUser = businessToRestUserMapper.map(user);

    return ResponseEntity.ok(restUser);
  }
}

