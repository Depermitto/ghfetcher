# GitHub Repository Fetcher

Spring Boot application provides a REST API to fetch GitHub user repositories (excluding forks)
along with their branches. It acts as a wrapper around the GitHub API with additional filtering and
error handling.

Contains a single integration test of the happy path.

Configuration of the GitHub API endpoint
via [application.properties](src/main/resources/application.properties).

## Get started locally

Maven wrapper is included, so getting the application running is as simple as:

```shell
git clone https://github.com/Depermitto/ghfetcher
cd ghfetcher
./mvnw spring-boot:run  # use mvnw.cmd on Windows
```

# License

Licensed under Apache 2.0