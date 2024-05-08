package net.cabezudo.sofia.places.rest;

import net.cabezudo.sofia.accounts.service.Account;
import net.cabezudo.sofia.accounts.service.AccountManager;
import net.cabezudo.sofia.accounts.service.InvalidAccountException;
import net.cabezudo.sofia.core.rest.BadRequestException;
import net.cabezudo.sofia.core.rest.ListRestResponse;
import net.cabezudo.sofia.core.rest.SofiaRestResponse;
import net.cabezudo.sofia.places.Place;
import net.cabezudo.sofia.places.PlaceList;
import net.cabezudo.sofia.places.PlacesManager;
import net.cabezudo.sofia.places.mappers.BusinessToRestPlaceListMapper;
import net.cabezudo.sofia.places.mappers.BusinessToRestPlaceMapper;
import net.cabezudo.sofia.places.mappers.RestToBusinessPlaceMapper;
import net.cabezudo.sofia.security.SofiaAuthorizedController;
import net.cabezudo.sofia.users.service.Group;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
public class PlacesController extends SofiaAuthorizedController {

  private static final Logger log = LoggerFactory.getLogger(PlacesController.class);
  private @Autowired BusinessToRestPlaceListMapper businessToRestPlaceListMapper;
  private @Autowired BusinessToRestPlaceMapper businessToRestPlaceMapper;
  private @Autowired PlacesManager placesManager;
  private @Autowired RestToBusinessPlaceMapper restToBusinessPlaceMapper;
  private @Autowired AccountManager accountManager;

  @GetMapping("/v1/places")
  public ResponseEntity<?> listAll(@RequestParam(defaultValue = "false") Boolean includeAdministrativeDivisionList) throws BadRequestException {
    log.debug("Get list of places");
    ListRestResponse<RestPlace> listRestResponse;

    Account account = super.getAccount();

    ResponseEntity<?> result;
    if ((result = super.checkPermissionFor(account, Group.ADMIN)) != null) {
      return result;
    }

    PlaceList placeList = placesManager.findAll(account, includeAdministrativeDivisionList);
    RestPlaceList restPlaceList = businessToRestPlaceListMapper.map(placeList);
    listRestResponse = new ListRestResponse(SofiaRestResponse.OK, "Retrieve list of places", restPlaceList);
    return ResponseEntity.ok(listRestResponse);
  }

  @PostMapping("/v1/places")
  public ResponseEntity<?> create(@RequestBody RestPlace restPlaceToSave) throws BadRequestException {
    log.debug("Create a new place");

    Account account = super.getAccount();

    ResponseEntity<?> result;
    if ((result = super.checkPermissionFor(account, Group.ADMIN)) != null) {
      return result;
    }

    Place newPlace = restToBusinessPlaceMapper.map(restPlaceToSave);

    // TODO If the place exists throw a exception
    Place place = placesManager.create(account, newPlace);
    RestPlace restPlace = businessToRestPlaceMapper.map(place);

    return ResponseEntity.ok(restPlace);
  }

  @GetMapping("/v1/places/{id}")
  public ResponseEntity<?> get(@PathVariable Integer id) throws BadRequestException {
    log.debug("Get the place with id=" + id);
    PlacesRestResponse placesRestResponse;

    Account account = super.getAccount();

    ResponseEntity<?> result;
    if ((result = super.checkPermissionFor(account, Group.ADMIN)) != null) {
      return result;
    }

    Place place = placesManager.get(id);
    RestPlace restPlace;
    if (place == null) {
      restPlace = null;
    } else {
      restPlace = businessToRestPlaceMapper.map(place);
    }
    placesRestResponse = new PlacesRestResponse(SofiaRestResponse.OK, "Retrieve place " + id, restPlace);
    return ResponseEntity.ok(placesRestResponse);
  }

  @PutMapping("/v1/places/{id}")
  public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody RestPlace restPlaceToSave) throws InvalidAccountException, BadRequestException {
    log.debug("Update an existing place");

    Account account = super.getAccount();

    ResponseEntity<?> result;
    if ((result = super.checkPermissionFor(account, Group.ADMIN)) != null) {
      return result;
    }
    Place newPlace = restToBusinessPlaceMapper.map(restPlaceToSave);
    Place place = placesManager.update(account, id, newPlace);
    RestPlace restPlace = businessToRestPlaceMapper.map(place);
    return ResponseEntity.ok(restPlace);
  }

  @DeleteMapping("/v1/places/{id}")
  public ResponseEntity<?> delete(@PathVariable Integer id) throws InvalidAccountException, BadRequestException {
    log.debug("Delete an existing place");

    Account account = super.getAccount();

    ResponseEntity<?> result;
    if ((result = super.checkPermissionFor(account, Group.ADMIN)) != null) {
      return result;
    }
    placesManager.delete(account, id);
    return ResponseEntity.ok(id);
  }

}

