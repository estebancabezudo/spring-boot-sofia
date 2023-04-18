package net.cabezudo.sofia.core;

import net.cabezudo.sofia.core.modules.ModuleManager;
import net.cabezudo.sofia.creator.ContentManager;
import net.cabezudo.sofia.creator.IdStorage;
import net.cabezudo.sofia.creator.SiteCreationException;
import net.cabezudo.sofia.creator.SofiaFile;
import net.cabezudo.sofia.creator.TemplateVariables;
import net.cabezudo.sofia.sites.Host;
import net.cabezudo.sofia.sites.HostNotFoundException;
import net.cabezudo.sofia.sites.PathManager;
import net.cabezudo.sofia.sites.Site;
import net.cabezudo.sofia.sites.SiteManager;
import net.cabezudo.sofia.sites.SiteNotFoundException;
import net.cabezudo.sofia.sites.SourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Component
@Order(SecurityProperties.DEFAULT_FILTER_ORDER - 1)
public class StaticContentCreatorFilter implements Filter {
  private final Logger log = LoggerFactory.getLogger(StaticContentCreatorFilter.class);

  @Autowired
  SofiaTemplateEngineEnvironment sofiaTemplateEngineEnvironment;
  @Autowired
  ContentManager contentManager;
  @Autowired
  SiteManager siteManager;
  @Autowired
  PathManager pathManager;
  @Autowired
  IdStorage idStorage;
  TemplateVariables templateVariables;
  @Autowired
  ModuleManager moduleManager;

  @Override
  public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
    log.debug("Static content creator filter.");
    if (req instanceof HttpServletRequest) {
      HttpServletRequest request = new HttpServletRequestWrapper((HttpServletRequest) req) {
        @Override
        public String getRequestURI() {
          String requestURI = super.getRequestURI();
          if (requestURI.endsWith("/")) {
            return requestURI + sofiaTemplateEngineEnvironment.getDirectoryIndexFile();
          }
          return requestURI;
        }
      };
      String requestURI = request.getRequestURI();

      HttpServletResponse response = (HttpServletResponse) res;
      try {
        String partialPath = requestURI.substring(1);
        Host host = siteManager.getHostByName(request.getServerName());
        request.setAttribute("host", host);

        Site site = siteManager.getByHostname(request.getServerName());
        request.getSession().setAttribute("site", site);
        log.debug("Set the request session attribute site to " + site);

        if (contentManager.ignoreRequestURI(requestURI)) {
          chain.doFilter(request, res);
          return;
        }

        Path targetPath = pathManager.getVersionedSitePath(site, host.getVersion()).resolve(partialPath);
        if (sofiaTemplateEngineEnvironment.isDevelopment() || !Files.exists(targetPath)) {
          if (requestURI.endsWith(".html")) {
            log.debug("Create file using " + targetPath);
            templateVariables = new TemplateVariables();
            SofiaFile sofiaFile = new SofiaFile(request, sofiaTemplateEngineEnvironment, siteManager, pathManager, templateVariables, moduleManager);
            sofiaFile.loadRootFile();
            sofiaFile.save();
          } else {
            Path sourcePath = null;
            try {
              sourcePath = pathManager.getVersionedSourcesPath(site, host.getVersion()).resolve(partialPath);
            } catch (SourceNotFoundException e) {
              throw new RuntimeException(e);
            }
            log.debug("Search for " + sourcePath);
            if (Files.exists(sourcePath)) {
              log.debug("Copy " + sourcePath + " to " + targetPath);
              Files.createDirectories(targetPath.getParent());
              Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
            } else {
              if (!Files.exists(targetPath)) {
                response.sendError(404, "File not found: " + requestURI);
                return;
              }
            }
          }
        } else {
          log.debug("Nothing to do with " + targetPath);
        }
      } catch (FileNotFoundException e) {
        response.sendError(404, e.getMessage());
        return;
      } catch (SiteCreationException | SiteNotFoundException | HostNotFoundException e) {
        e.printStackTrace();
        log.warn(e.getMessage());
        response.sendError(500, e.getMessage());
        return;
      } catch (RuntimeException e) {
        throw new SofiaRuntimeException(e);
      }
      chain.doFilter(request, response);
    } else {
      chain.doFilter(req, res);
    }
  }
}


