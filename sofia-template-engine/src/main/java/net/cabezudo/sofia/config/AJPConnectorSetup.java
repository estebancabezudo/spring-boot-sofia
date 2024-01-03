package net.cabezudo.sofia.config;

import org.apache.catalina.connector.Connector;
import org.apache.coyote.ajp.AjpNioProtocol;

import org.jspecify.nullness.Nullable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AJPConnectorSetup {

  @Value("${server.ajp.port:#{null}}")
  @Nullable
  private Integer port;

  @Value("${server.ajp.redirectPort:#{null}}")
  @Nullable
  private Integer redirectPort;

  @Bean
  public ServletWebServerFactory servletContainer() {
    TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();

    if (port != null && redirectPort != null) {
      Connector ajpConnector = new Connector("AJP/1.3");
      ajpConnector.setPort(port);
      ajpConnector.setRedirectPort(redirectPort);
      ajpConnector.setSecure(true);
      ajpConnector.setAllowTrace(false);
      ajpConnector.setScheme("http");

      AjpNioProtocol protocol = (AjpNioProtocol) ajpConnector.getProtocolHandler();
      protocol.setSecretRequired(false);

      tomcat.addAdditionalTomcatConnectors(ajpConnector);
    }

    return tomcat;
  }
}

