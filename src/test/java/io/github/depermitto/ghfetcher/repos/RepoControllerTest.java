package io.github.depermitto.ghfetcher.repos;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.depermitto.ghfetcher.repos.Repo.Owner;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;

@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class RepoControllerTest {

  @MockitoBean
  RepoService repoService;

  @Test
  public void shouldFindUserRepositories(@Autowired WebTestClient webTestClient, @Autowired ObjectMapper objectMapper)
      throws Exception {
    // Given
    Owner owner = new Owner("Depermitto");
    String name = "klaster";

    var repos = new Repo.Request[]{new Repo.Request(name, owner, false)};
    Mockito.when(repoService.findUserRepositories(Mockito.anyString())).thenReturn(repos);

    var branches = new Repo.Branch[]{new Repo.Branch("master", new Repo.Branch.Commit("639c67c"))};
    Mockito.when(repoService.findUserRepositoryBranches(name, owner)).thenReturn(branches);

    var expected = new Repo[]{new Repo(name, owner, branches)};

    // When
    webTestClient
        .get()
        .uri("/repos/Depermitto")
        .exchange()
        // Then
        .expectStatus()
        .isOk()
        .expectBody()
        .json(objectMapper.writeValueAsString(expected));
  }
}
