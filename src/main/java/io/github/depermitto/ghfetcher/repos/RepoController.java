package io.github.depermitto.ghfetcher.repos;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
@RequestMapping("/repos/")
public class RepoController {

  final RepoService repoService;

  public RepoController(RepoService repoService) {
    this.repoService = repoService;
  }

  @GetMapping("{username}")
  public Repo[] findUserRepositories(@PathVariable String username) {
    return Arrays.stream(repoService.findUserRepositories(username))
        .filter(repo -> !repo.fork())
        .map(repo -> {
          var branches = repoService.findUserRepositoryBranches(repo.name(), repo.owner());
          return new Repo(repo.name(), repo.owner(), branches);
        })
        .toArray(Repo[]::new);
  }
}
