package net.cabezudo.sofia.core;

import net.cabezudo.sofia.creator.ContentManager;
import net.cabezudo.sofia.creator.SiteCreationException;
import net.cabezudo.sofia.creator.SofiaFile;
import net.cabezudo.sofia.creator.TemplateVariables;
import net.cabezudo.sofia.security.PermissionManager;
import net.cabezudo.sofia.sites.Host;
import net.cabezudo.sofia.sites.HostNotFoundException;
import net.cabezudo.sofia.sites.PathManager;
import net.cabezudo.sofia.sites.Site;
import net.cabezudo.sofia.sites.SiteManager;
import net.cabezudo.sofia.sites.SiteNotFoundException;
import net.cabezudo.sofia.sites.SourceNotFoundException;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
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
public class StaticContentCreatorFilter extends OncePerRequestFilter {
  private static final Logger log = LoggerFactory.getLogger(StaticContentCreatorFilter.class);

  TemplateVariables templateVariables;
  private @Autowired SofiaEnvironment sofiaEnvironment;
  private @Autowired ContentManager contentManager;
  private @Autowired SiteManager siteManager;
  private @Autowired PathManager pathManager;
  private @Autowired PermissionManager permissionManager;

  @Override
  protected void doFilterInternal(@NotNull HttpServletRequest req, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) throws ServletException, IOException {
    log.debug("Static content creator filter.");
    HttpServletRequest request = new HttpServletRequestWrapper(req) {
      @Override
      public String getRequestURI() {
        String requestURI = super.getRequestURI();
        if (requestURI.endsWith("/")) {
          return requestURI + sofiaEnvironment.getDirectoryIndexFile();
        }
        return requestURI;
      }
    };
    String requestURI = request.getRequestURI();

    try {
      String partialPath = requestURI.substring(1);
      Host host = siteManager.getHostByName(request.getServerName());
      request.setAttribute("host", host);

      Site site = siteManager.getByHostname(request.getServerName());

      // Ignore the URLs listed in the 'api' property.
      if (contentManager.ignoreRequestURI(site, requestURI)) {
        filterChain.doFilter(request, response);
        return;
      }

      Path targetPath = pathManager.getVersionedSitePath(site, host.getVersion()).resolve(partialPath);
      if (sofiaEnvironment.isDevelopment() || !Files.exists(targetPath)) {
        if (requestURI.endsWith(".html")) {
          log.debug("Create file using " + targetPath);
          templateVariables = new TemplateVariables(sofiaEnvironment);
          SofiaFile sofiaFile = new SofiaFile(request, sofiaEnvironment, siteManager, pathManager, templateVariables, permissionManager);
          sofiaFile.loadRootFile();
          sofiaFile.save();
        } else {
          Path sourcePath;
          try {
            sourcePath = pathManager.getVersionedSourcesPath(site, host.getVersion()).resolve(partialPath);
          } catch (SourceNotFoundException e) {
            throw new RuntimeException(e);
          }
          log.debug("Search for " + sourcePath);
          if (Files.exists(sourcePath) && !Files.isDirectory(sourcePath)) {
            log.debug("Copy " + sourcePath + " to " + targetPath);
            Files.createDirectories(targetPath.getParent());
            Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
          } else {
            // TODO Check this. Don't have any sense. If the file do not exist in source path, don't exist in target path either. And, why return a 404. It appears like an 500.
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
      if (sofiaEnvironment.isDevelopment()) {
        response.setStatus(500);
        response.getWriter().write(e.getMessage());
      } else {
        response.sendError(500);
      }
      return;
    } catch (RuntimeException e) {
      throw new SofiaRuntimeException(e);
    }
    filterChain.doFilter(request, response);
  }
}


