package io.github.depermitto.ghfetcher.exceptions;

import org.springframework.http.HttpStatusCode;

public class HttpStatusException extends RuntimeException {

  private final HttpStatusCode statusCode;
  private final String message;

  public HttpStatusException(HttpStatusCode statusCode, String message) {
    this.statusCode = statusCode;
    this.message = message;
  }

  public HttpStatusCode getStatusCode() {
    return statusCode;
  }

  @Override
  public String getMessage() {
    return message;
  }
}
