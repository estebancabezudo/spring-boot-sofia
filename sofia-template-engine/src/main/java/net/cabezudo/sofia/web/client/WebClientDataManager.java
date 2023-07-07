package net.cabezudo.sofia.web.client;

import net.cabezudo.sofia.web.CookieManager;
import net.cabezudo.sofia.web.client.mappers.BusinessToEntityWebClientDataMapper;
import net.cabezudo.sofia.web.client.mappers.EntityToBusinessWebClientDataMapper;
import net.cabezudo.sofia.web.client.persistence.WebClientDataEntity;
import net.cabezudo.sofia.web.client.persistence.WebClientDataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class WebClientDataManager {
  public static final String WEB_CLIENT_COOKIE_DATA_NAME = "webClientData";
  private static final Logger log = LoggerFactory.getLogger(WebClientDataManager.class);
  private @Autowired CookieManager cookieManager;
  private @Autowired WebClientDataRepository webClientDataRepository;
  private @Autowired BusinessToEntityWebClientDataMapper businessToEntityWebClientDataMapper;
  private @Autowired EntityToBusinessWebClientDataMapper entityToBusinessWebClientDataMapper;

  public WebClientData getFromCookie(HttpServletRequest request) {
    WebClientData webClientDataFromAttribute = (WebClientData) request.getAttribute(WEB_CLIENT_COOKIE_DATA_NAME);
    WebClientData webClientData;
    if (webClientDataFromAttribute == null) {
      Integer id = cookieManager.getIntegerCookie(request, WEB_CLIENT_COOKIE_DATA_NAME);
      if (id == null) {
        return null;
      }
      // TODO implement cache here
      WebClientDataEntity webClientDataEntity = webClientDataRepository.get(id);
      webClientData = entityToBusinessWebClientDataMapper.map(webClientDataEntity);
    } else {
      webClientData = webClientDataFromAttribute;
    }
    return webClientData;
  }

  public void update(int id, WebClientData webClientData) {
    WebClientDataEntity webClientDataEntity = businessToEntityWebClientDataMapper.map(webClientData);
    webClientDataRepository.update(id, webClientDataEntity);
  }

  public WebClientData create(WebClientData webClientDataToCreate) {
    WebClientDataEntity webClientDataEntityToCreate = businessToEntityWebClientDataMapper.map(webClientDataToCreate);
    WebClientDataEntity webClientDataEntity = webClientDataRepository.create(webClientDataEntityToCreate);
    WebClientData webClientData = entityToBusinessWebClientDataMapper.map(webClientDataEntity);
    return webClientData;
  }

  public void setCookie(HttpServletResponse response, WebClientData newWebClientData) {
    Cookie cookie = new Cookie(WEB_CLIENT_COOKIE_DATA_NAME, Integer.toString(newWebClientData.getId()));
    cookie.setMaxAge(10368000); // 120 days
    cookie.setPath("/");
    response.addCookie(cookie);
  }
}
