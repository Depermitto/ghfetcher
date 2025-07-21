package io.github.depermitto.ghfetcher.repos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

@SpringBootTest
class RepoControllerIntegrationTest {

  @Autowired
  private RestClient.Builder restClientBuilder;

  @Test
  public void testGetUserRepositories() {
    RestClient restClient = restClientBuilder.build();
    String username = "Depermitto";
    String uri = "http://localhost:8080/repos/" + username;

    ResponseEntity<Repo[]> response = restClient
        .get()
        .uri(uri)
        .retrieve()
        .toEntity(Repo[].class);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    Repo[] repos = response.getBody();
    for (Repo repo : repos) {
      assertNotNull(repo.name());
      assert !repo.name().isEmpty();

      assertNotNull(repo.owner());
      assertEquals(username, repo.owner().login());

      assertNotNull(repo.branches());
      for (Repo.Branch branch : repo.branches()) {
        assertNotNull(branch.name());
        assert !branch.name().isEmpty();
      }
    }
  }
}
