package net.cabezudo.sofia.core;

import org.springframework.http.ResponseEntity;

public class GlobalExceptionHandler {

  private final SofiaEnvironment sofiaEnvironment;

  GlobalExceptionHandler(SofiaEnvironment sofiaEnvironment) {
    this.sofiaEnvironment = sofiaEnvironment;
  }

  public ResponseEntity<String> handleException(Exception e) {
    String errorMessage;
    if (sofiaEnvironment.isDevelopment()) {
      errorMessage = "Ha ocurrido un error. " + e.getMessage();
      e.printStackTrace();
    } else {
      errorMessage = "Ha ocurrido un error.";
    }
    return ResponseEntity.internalServerError().body(errorMessage);
  }
}
