package net.cabezudo.sofia.core.rest;

public class RestErrorResponse {
  public static final RestErrorResponse UNAUTHORIZED = new RestErrorResponse(40100, "The client request has not been completed because it lacks valid authentication credentials for the requested resource.", 401);
  public static final RestErrorResponse FORBIDDEN = new RestErrorResponse(40300, "The server understands the request but refuses to authorize it. Insufficient rights for the resource", 403);
  private final int code;
  private final String message;
  private final int status;

  private RestErrorResponse(int code, String message, int status) {
    this.code = code;
    this.message = message;
    this.status = status;
  }

  public int getCode() {
    return code;
  }

  public String getMessage() {
    return message;
  }

  public int getStatus() {
    return status;
  }
}
