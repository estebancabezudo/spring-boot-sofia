package net.cabezudo.sofia.creator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author <a href="http://cabezudo.net">Esteban Cabezudo</a>
 * @version 0.01.00, 2022.07.29
 */
@SpringBootApplication(scanBasePackages = "net.cabezudo.sofia")
public class Application {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
}
