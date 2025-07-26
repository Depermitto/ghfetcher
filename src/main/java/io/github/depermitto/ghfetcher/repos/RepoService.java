package io.github.depermitto.ghfetcher.repos;

import io.github.depermitto.ghfetcher.exceptions.HttpStatusException;
import java.net.URI;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class RepoService {

  @Value("${api.github.com}")
  private String baseUrl;

  final RestClient restClient;

  public RepoService(RestClient.Builder restClientBuilder) {
    this.restClient = restClientBuilder.build();
  }


  public @NonNull Repo.Request[] findUserRepositories(@NonNull String username) {
    return Objects.requireNonNullElse(restClient
        .get()
        .uri(userReposUri(username))
        .retrieve()
        .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
          String message =
              response.getStatusCode() == HttpStatus.NOT_FOUND ? "User does not exist" : response.getStatusText();
          throw new HttpStatusException(response.getStatusCode(), message);
        })
        .body(Repo.Request[].class), new Repo.Request[0]);
  }

  public @NonNull Repo.Branch[] findUserRepositoryBranches(@NonNull String name, @NonNull Repo.Owner owner) {
    return Objects.requireNonNullElse(restClient.get()
        .uri(userRepoBranchesUri(owner.login(), name))
        .retrieve()
        .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
          throw new HttpStatusException(response.getStatusCode(), response.getStatusText());
        })
        .body(Repo.Branch[].class), new Repo.Branch[0]);
  }


  private URI userReposUri(String username) {
    return URI.create(baseUrl + "/users/" + username + "/repos");
  }

  private URI userRepoBranchesUri(String username, String repo) {
    return URI.create(baseUrl + "/repos/" + username + "/" + repo + "/branches");
  }
}