# Automate work for a dance school

## Service
Monolith application:
- In-memory DB H2 with init date by liquibase
- Backend Spring Boot, JPA
- Front Vaadin 

This Dockerized SpringBoot-based service is provided:

- Dance groups
- Dance lessons
- User management

## Security
Admin user: admin/admin

## Building and publishing JAR + Docker image
This project is using the Maven dockerfile plugin.
All configuration can be found in the [Maven pom.xml](pom.xml) file 
with separate profiles for production and development.

Example scripts to build the project:
- [build-dev.cmd](build-dev.cmd) build with dev parameters and requires a separate lancher
- [build-prod.cmd](build-prod.cmd) builds and publishes it to the docker repository
