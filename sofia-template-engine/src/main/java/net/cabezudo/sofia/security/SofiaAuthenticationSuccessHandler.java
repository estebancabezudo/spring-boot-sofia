package net.cabezudo.sofia.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class SofiaAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

  protected final Log logger = LogFactory.getLog(this.getClass());

  private RequestCache requestCache = new HttpSessionRequestCache();

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                      Authentication authentication) throws ServletException, IOException {

    PrintWriter out = response.getWriter();
    if (!(authentication instanceof UsernamePasswordAuthenticationToken)) {
      write(out, new LoginRestResponse("fail"));
      return;
    }

    SavedRequest savedRequest = this.requestCache.getRequest(request, response);
    if (savedRequest == null) {
      write(out, new LoginRestResponse("ok", "/"));
      return;
    }
    clearAuthenticationAttributes(request);
    write(out, new LoginRestResponse("ok", savedRequest.getRedirectUrl()));
  }

  private void write(PrintWriter out, LoginRestResponse response) throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    String jsonResponse = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(response);
    out.println(jsonResponse);
    out.flush();
  }

  public void setRequestCache(RequestCache requestCache) {
    this.requestCache = requestCache;
  }
}
