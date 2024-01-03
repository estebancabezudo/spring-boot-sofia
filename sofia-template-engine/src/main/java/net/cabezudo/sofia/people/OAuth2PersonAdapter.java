package net.cabezudo.sofia.people;

import net.cabezudo.sofia.core.SofiaRuntimeException;
import net.cabezudo.sofia.people.service.PeopleManager;
import net.cabezudo.sofia.sites.Site;
import net.cabezudo.sofia.sites.service.PathManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import java.util.Locale;

@Component
public class OAuth2PersonAdapter {
  private static final Logger log = LoggerFactory.getLogger(OAuth2PersonAdapter.class);
  private @Autowired PeopleManager peopleManager;
  private @Autowired PathManager pathManager;
  private @Autowired OAuth2AuthorizedClientService authorizedClientService;

  public OAuthPersonData build(Site site, String email, OAuth2AuthenticationToken oAuth2User) {
    String registrationId = oAuth2User.getAuthorizedClientRegistrationId();
    OAuth2User principal = oAuth2User.getPrincipal();
    String name;
    String secondName;
    String lastName;
    String secondLastName;
    Locale locale;
    Date dateOfBirth;
    switch (registrationId) {
      case "google":
        authorizedClientService.loadAuthorizedClient("google", email);
        name = principal.getAttribute("given_name");
        secondName = null;
        lastName = principal.getAttribute("family_name");
        secondLastName = null;
        String imageURLFromAttribute = principal.getAttribute("picture");
        if (imageURLFromAttribute != null) {
          try {
            URL imageURL = new URL(imageURLFromAttribute);
            log.debug("Reading url: " + imageURL);
            BufferedImage image = ImageIO.read(imageURL);
            Path destinationPath = pathManager.getProtectedPersonImagesPath(site);
            File destinationFile = destinationPath.resolve(email + ".jpg").toFile();
            if (!Files.exists(destinationPath)) {
              // TODO Create when start the application and read the sites to avoid this condition
              Files.createDirectories(destinationPath);
            }
            ImageIO.write(image, "jpg", destinationFile);
          } catch (IOException e) {
            log.warn("I can't read the image URL " + imageURLFromAttribute);
          }
        }
        String localeFromPrincipal = principal.getAttribute("locale");
        locale = localeFromPrincipal == null ? null : new Locale(localeFromPrincipal);
        dateOfBirth = null;
        return new OAuthPersonData(email, name, secondName, lastName, secondLastName, dateOfBirth, locale);
      case "facebook":
        authorizedClientService.loadAuthorizedClient("facebook", email);
        String nameFromFacebook = principal.getAttribute("name");
        if (nameFromFacebook == null) {
          name = null;
          secondName = null;
          lastName = null;
          secondLastName = null;
        } else {
          String[] names = nameFromFacebook.split(" ");
          switch (names.length) {
            case 1:
              name = names[0];
              secondName = null;
              lastName = null;
              secondLastName = null;
              break;
            case 2:
              name = names[0];
              secondName = null;
              lastName = names[1];
              secondLastName = null;
              break;
            case 3:
              name = names[0];
              secondName = null;
              lastName = names[1];
              secondLastName = names[2];
              break;
            default:
              name = names[0];
              secondName = names[1];
              lastName = names[2];
              secondLastName = names[3];
              break;
          }
        }
        dateOfBirth = null;
        locale = null;
        return new OAuthPersonData(email, name, secondName, lastName, secondLastName, dateOfBirth, locale);
      default:
        throw new SofiaRuntimeException("Invalid oauth client registrator id: " + registrationId);
    }
  }
}
