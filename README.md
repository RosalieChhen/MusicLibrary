# Music Library

*RESTful API* of a basic Music Library application implemented with Spring Boot 3 and Spring Security 6.

## Description

In the music library, any users can search for songs and artists. 
Any user can register on the platform with the *artist* role. 
A song is uploaded by a user who must have the *artist* role.
Some users have the *admin* role, allowing them to delete registered users.

The list of endpoints available is described in the Swagger documentation.

## Development (local environment)
### Tools
- IntelliJ IDEA 2023.1.1 (Community Edition)
- Gradle 8.3
- Java 17
- Spring Boot 3
- Spring Security 6
- H2 database

### Installation
- Clone this repository, open it with IntelliJ

### Run project
- *Clean*, *build* and *bootRun* the application
- The base URL of the api is : http://localhost:8080/api/v1
- To access the endpoints, check the swagger documentation available at : http://localhost:8080/swagger-ui/index.html 
- The in-memory database is available at : http://localhost:8080/h2-console with the following credentials used for development purposes:
  - Username : sa
  - Password : password

