package net.cabezudo.sofia.core.exceptions;

import net.cabezudo.sofia.core.SofiaEnvironment;
import net.cabezudo.sofia.core.rest.SofiaRestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

  private @Autowired SofiaEnvironment sofiaEnvironment;

  @ExceptionHandler(HttpMessageNotReadableException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public ResponseEntity<SofiaRestResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
    String errorMessage;
    if (sofiaEnvironment.isDevelopment()) {
      errorMessage = "Ha ocurrido un error. " + e.getMessage();
      e.printStackTrace();
    } else {
      errorMessage = "Ha ocurrido un error.";
    }
    SofiaRestResponse errorResponse = new SofiaRestResponse<>(SofiaRestResponse.ERROR, errorMessage);
    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }
}