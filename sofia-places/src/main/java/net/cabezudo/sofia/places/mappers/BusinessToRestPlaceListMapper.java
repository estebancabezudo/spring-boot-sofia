package net.cabezudo.sofia.places.mappers;

import net.cabezudo.sofia.places.Place;
import net.cabezudo.sofia.places.PlaceList;
import net.cabezudo.sofia.places.rest.RestPlaceList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BusinessToRestPlaceListMapper {
  private @Autowired BusinessToRestPlaceMapper businessToRestPlaceMapper;

  public RestPlaceList map(PlaceList placeList) {
    int total = placeList.getTotal();
    int start = placeList.getStart();
    int size = placeList.getSize();
    RestPlaceList list = new RestPlaceList();

    for (Place place : placeList) {
      list.add(businessToRestPlaceMapper.map(place));
    }
    return list;
  }
}