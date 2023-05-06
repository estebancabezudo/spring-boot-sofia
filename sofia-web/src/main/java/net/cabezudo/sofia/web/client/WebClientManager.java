package net.cabezudo.sofia.web.client;

import net.cabezudo.sofia.core.modules.ModuleManager;
import net.cabezudo.sofia.users.persistence.UserRepository;
import net.cabezudo.sofia.web.client.mappers.BusinessToEntityWebClientMapper;
import net.cabezudo.sofia.web.client.mappers.EntityToBusinessWebClientMapper;
import net.cabezudo.sofia.web.client.persistence.WebClientEntity;
import net.cabezudo.sofia.web.client.persistence.WebClientRepository;
import net.cabezudo.sofia.web.client.service.WebClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

@Service
@Transactional
public class WebClientManager {
  private static final Logger log = LoggerFactory.getLogger(WebClientManager.class);

  private @Autowired ModuleManager moduleManager;
  private @Resource UserRepository userRepository;
  private @Resource WebClientRepository webClientRepository;
  private @Autowired HttpServletRequest request;

  public WebClient loadWebClientBySessionId(Long id) throws UsernameNotFoundException {
    WebClientEntity webClientEntity = webClientRepository.findById(id);
    log.debug("Web client in database for id " + id + ": " + webClientEntity);
    EntityToBusinessWebClientMapper mapper = new EntityToBusinessWebClientMapper();
    return mapper.map(webClientEntity);
  }

  public WebClient create() {
    WebClientEntity webClientEntityData = new WebClientEntity(null, null);
    WebClientEntity webClientEntity = webClientRepository.create(webClientEntityData);
    log.debug("Web client created in database with id " + webClientEntity.getId() + ": " + webClientEntity);
    EntityToBusinessWebClientMapper mapper = new EntityToBusinessWebClientMapper();
    return mapper.map(webClientEntity);
  }

  public WebClient update(WebClient webClient) {
    BusinessToEntityWebClientMapper mapper = new BusinessToEntityWebClientMapper();
    WebClientEntity webClientEntity = mapper.map(webClient);
    WebClientEntity updatedWebClientEntity = webClientRepository.update(webClientEntity);
    EntityToBusinessWebClientMapper entityToBusinessWebClientMapper = new EntityToBusinessWebClientMapper();
    WebClient webClientUpdated = entityToBusinessWebClientMapper.map(updatedWebClientEntity);
    log.debug("Web client with id " + webClientEntity.getId() + " updates in database: " + webClientUpdated);
    return webClientUpdated;
  }
}


