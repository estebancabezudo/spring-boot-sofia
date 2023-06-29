package net.cabezudo.sofia.people;

import net.cabezudo.sofia.core.SofiaRuntimeException;
import net.cabezudo.sofia.sites.PathManager;
import net.cabezudo.sofia.sites.Site;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Date;
import java.util.Locale;

@Component
public class OAuth2PersonAdapter {
  private static final Logger log = LoggerFactory.getLogger(OAuth2PersonAdapter.class);
  private @Autowired PeopleManager peopleManager;
  private @Autowired PathManager pathManager;

  public OAuthPersonData build(Site site, OAuth2AuthenticationToken oAuth2User) {
    String registrationId = oAuth2User.getAuthorizedClientRegistrationId();
    OAuth2User principal = oAuth2User.getPrincipal();
    switch (registrationId) {
      case "google":
        String name = principal.getAttribute("given_name");
        String secondName = null;
        String lastName = principal.getAttribute("family_name");
        String secondLlastName = null;
        String email = principal.getAttribute("email");
        // TODO Save picture on disk using a name from the email. Protect the image from read using the user access in the controller.
        String imageURLFromAttribute = principal.getAttribute("picture");
        if (imageURLFromAttribute != null) {
          try {
            URL imageURL = new URL(imageURLFromAttribute);
            log.debug("Reading url: " + imageURL);
            BufferedImage image = ImageIO.read(imageURL);
            Path destinationPath = pathManager.getProtectedPersonImagesPath(site);
            File destinationFile = destinationPath.resolve(email).toFile();
            ImageIO.write(image, "jpg", destinationFile);
          } catch (IOException e) {
            log.warn("I can't read the image URL " + imageURLFromAttribute);
          }
        }
        String localeFromPrincipal = principal.getAttribute("locale");
        Locale locale = localeFromPrincipal == null ? null : new Locale(localeFromPrincipal);
        Date dateOfBirth = null;
        return new OAuthPersonData(email, name, secondName, lastName, secondLlastName, dateOfBirth, locale);
      case "facebook":
//        return new Person();
      default:
        throw new SofiaRuntimeException("Invalid oauth client registrator id: " + registrationId);
    }
  }
}
