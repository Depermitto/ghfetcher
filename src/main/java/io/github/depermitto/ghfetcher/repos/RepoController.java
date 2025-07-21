package io.github.depermitto.ghfetcher.repos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

import java.net.URI;
import java.util.Arrays;
import java.util.Objects;

import io.github.depermitto.ghfetcher.exceptions.HttpStatusException;
import io.github.depermitto.ghfetcher.repos.Repo.Branch;

@RestController
public class RepoController {

  private final RestClient restClient;
  @Value("${api.github.com}")
  private String baseUrl;

  public RepoController(RestClient.Builder restClientBuilder) {
    this.restClient = restClientBuilder.build();
  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  private record RepoRequest(String name, Repo.Owner owner, boolean fork) {

  }

  @GetMapping("/repos/{username}")
  public Repo[] getUserRepositories(@PathVariable String username) {
    RepoRequest[] repos = restClient.get()
        .uri(userReposUri(username))
        .retrieve()
        .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
          String message =
              response.getStatusCode() == HttpStatus.NOT_FOUND ? "User does not exist" : response.getStatusText();
          throw new HttpStatusException(response.getStatusCode(), message);
        })
        .body(RepoRequest[].class);

    return Arrays.stream(Objects.requireNonNullElse(repos, new RepoRequest[0]))
        .filter(repo -> !repo.fork())
        .map(repo -> {
          Repo.Branch[] branches = restClient.get()
              .uri(userRepoBranchesUri(repo.owner().login(), repo.name()))
              .retrieve()
              .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                throw new HttpStatusException(response.getStatusCode(), response.getStatusText());
              })
              .body(Repo.Branch[].class);
          return new Repo(repo.name(), repo.owner(), Objects.requireNonNullElse(branches, new Branch[0]));
        })
        .toArray(Repo[]::new);
  }

  private URI userReposUri(String username) {
    return URI.create(baseUrl + "/users/" + username + "/repos");
  }

  private URI userRepoBranchesUri(String username, String repo) {
    return URI.create(baseUrl + "/repos/" + username + "/" + repo + "/branches");
  }
}
