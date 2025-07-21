package io.github.depermitto.ghfetcher.exceptions;

import java.util.HashMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GHFetcherExceptionHandler {

  @ExceptionHandler(HttpStatusException.class)

  public ResponseEntity<Object> handleException(HttpStatusException e) {
    var body = new HashMap<>();
    body.put("status", e.getStatusCode().value());
    body.put("message", e.getMessage());
    return new ResponseEntity<>(body, e.getStatusCode());
  }
}