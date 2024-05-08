package net.cabezudo.sofia.people.rest;

import net.cabezudo.sofia.accounts.service.Account;
import net.cabezudo.sofia.core.rest.BadRequestException;
import net.cabezudo.sofia.core.rest.ListRestResponse;
import net.cabezudo.sofia.core.rest.SofiaRestResponse;
import net.cabezudo.sofia.people.PeopleList;
import net.cabezudo.sofia.people.Person;
import net.cabezudo.sofia.people.mappers.BusinessToRestPersonListMapper;
import net.cabezudo.sofia.people.mappers.BusinessToRestPersonMapper;
import net.cabezudo.sofia.people.service.PeopleManager;
import net.cabezudo.sofia.security.SofiaAuthorizedController;
import net.cabezudo.sofia.users.service.Group;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
public class PeopleController extends SofiaAuthorizedController {
  private static final Logger log = LoggerFactory.getLogger(PeopleController.class);

  private @Autowired PeopleManager peopleManager;

  @GetMapping("/v1/people")
  public ResponseEntity<?> listAll() throws BadRequestException {
    log.debug("Get list of people");
    ListRestResponse<RestPerson> listRestResponse;

    Account account = super.getAccount();

    ResponseEntity<?> result;
    if ((result = super.checkPermissionFor(account, Group.ADMIN)) != null) {
      return result;
    }

    PeopleList peopleList = peopleManager.findAll(account);
    BusinessToRestPersonListMapper mapper = new BusinessToRestPersonListMapper();
    RestPersonList restPersonList = mapper.map(peopleList);
    listRestResponse = new ListRestResponse<>(SofiaRestResponse.OK, "Retrieve list of people", restPersonList);
    return ResponseEntity.ok(listRestResponse);
  }

  @GetMapping("/v1/people/{id}")
  public ResponseEntity<?> login(@PathVariable Integer id) throws BadRequestException {
    log.debug("/v1/people/{id}");
    PeopleRestResponse peopleRestResponse;

    Account account = super.getAccount();

    ResponseEntity<?> result;
    if ((result = super.checkPermissionFor(account, Group.ADMIN)) != null) {
      return result;
    }

    Person person = peopleManager.get(account, id);
    BusinessToRestPersonMapper mapper = new BusinessToRestPersonMapper();
    RestPerson restPerson = mapper.map(person);
    peopleRestResponse = new PeopleRestResponse(SofiaRestResponse.OK, "Retrieve list of people", restPerson);
    return ResponseEntity.ok(peopleRestResponse);
  }
}

