package io.github.depermitto.ghfetcher.repos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Repo(String name, Owner owner, Branch[] branches) {

  @JsonIgnoreProperties(ignoreUnknown = true)
  public record Owner(String login) {

  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  public record Branch(String name, Commit commit) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Commit(String sha) {

    }
  }
}
