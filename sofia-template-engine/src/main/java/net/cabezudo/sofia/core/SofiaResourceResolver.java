package net.cabezudo.sofia.core;

import jakarta.servlet.http.HttpServletRequest;
import net.cabezudo.sofia.sites.Host;
import net.cabezudo.sofia.sites.HostNotFoundException;
import net.cabezudo.sofia.sites.Site;
import net.cabezudo.sofia.sites.SiteNotFoundException;
import net.cabezudo.sofia.sites.service.PathManager;
import net.cabezudo.sofia.sites.service.SiteManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.resource.ResourceResolver;
import org.springframework.web.servlet.resource.ResourceResolverChain;

import java.nio.file.Path;
import java.util.List;

public class SofiaResourceResolver implements ResourceResolver {
  private final Logger logger = LoggerFactory.getLogger(SofiaResourceResolver.class);

  SiteManager siteManager;
  PathManager pathManager;

  public SofiaResourceResolver(SiteManager siteManager, PathManager pathManager) {
    this.siteManager = siteManager;
    this.pathManager = pathManager;
  }

  @Override
  public Resource resolveResource(HttpServletRequest request, String requestPath, List<? extends Resource> locations, ResourceResolverChain chain) {
    try {
      if (request == null) {
        throw new SofiaRuntimeException("null request");
      }
      Host host = siteManager.getHostByName(request.getServerName());
      Site site = siteManager.get(request);
      Path path = pathManager.getVersionedSitePath(site, host.getVersion()).resolve(request.getRequestURI().substring(1));
      logger.info("Path to created file: " + path);
      return new FileSystemResource(path);
    } catch (SiteNotFoundException | HostNotFoundException e) {
      throw new SofiaRuntimeException(e);
    }
  }

  @Override
  public String resolveUrlPath(String resourcePath, List<? extends Resource> locations, ResourceResolverChain chain) {
    return null;
  }
}
