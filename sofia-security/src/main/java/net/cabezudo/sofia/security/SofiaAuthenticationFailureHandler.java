package net.cabezudo.sofia.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class SofiaAuthenticationFailureHandler implements AuthenticationFailureHandler {

  protected final Log logger = LogFactory.getLog(this.getClass());

  private final RequestCache requestCache = new HttpSessionRequestCache();

  private void write(PrintWriter out, LoginRestResponse response) throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    String jsonResponse = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(response);
    out.println(jsonResponse);
    out.flush();
  }

  @Override
  public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
    PrintWriter out = response.getWriter();
    write(out, new LoginRestResponse("fail"));
  }
}
