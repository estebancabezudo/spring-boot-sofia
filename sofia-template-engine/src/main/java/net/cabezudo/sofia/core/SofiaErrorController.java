package net.cabezudo.sofia.core;

import net.cabezudo.sofia.web.client.WebClientDataManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class SofiaErrorController implements ErrorController {

  private @Autowired WebClientDataManager webClientDataManager;
  private @Autowired WebMessageManager webMessageManager;

  @RequestMapping("/error")
  public ResponseEntity<?> redirectToError(WebRequest webRequest, HttpServletRequest request, HttpServletResponse response) {
    Integer statusCode = (Integer) webRequest.getAttribute("javax.servlet.error.status_code", RequestAttributes.SCOPE_REQUEST);
    String errorMessage = (String) webRequest.getAttribute("javax.servlet.error.message", RequestAttributes.SCOPE_REQUEST);

    if (statusCode == 500) {
      return ResponseEntity.internalServerError().build();
    }

    String redirectUrl = "/index.html";
    webMessageManager.setMessage("{ \"type\": \"error\", \"data\": \"" + statusCode + ": " + (errorMessage == null ? "" : errorMessage) + "\"}");
    return ResponseEntity.status(HttpStatus.FOUND)
        .header("Location", redirectUrl)
        .build();
  }
}