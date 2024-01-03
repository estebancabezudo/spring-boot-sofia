package net.cabezudo.sofia.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;
import java.io.PrintWriter;

public class SofiaAuthenticationFailureHandler implements AuthenticationFailureHandler {

  protected final Log logger = LogFactory.getLog(this.getClass());

  private void write(PrintWriter out, LoginRestResponse response) throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    String jsonResponse = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(response);
    out.println(jsonResponse);
    out.flush();
  }

  @Override
  public void onAuthenticationFailure(jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
    PrintWriter out = response.getWriter();
    write(out, new LoginRestResponse("fail"));
  }
}
