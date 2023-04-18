package net.cabezudo.sofia.core.rest;


public class ListRestResponse<T> extends SofiaRestResponse {
  private final RestList<T> data;

  public ListRestResponse(int status, String message, RestList<T> data) {
    super(status, message);
    this.data = data;
  }

  public RestList<T> getData() {
    return data;
  }
}
