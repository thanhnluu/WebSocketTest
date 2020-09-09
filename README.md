# WebSocketTest

This is an example application that integrates Spring Boot web sockets with Redis Cache and Redis Keyspace Notifications to notify clients on changing traffic light signals. For testing purposes, the application points to a Redis Cache that is running in a Docker container.

## Prerequisites: 
- Install Maven (https://maven.apache.org/download.cgi) and Docker (https://www.docker.com/products/docker-desktop).Note, Maven does not need to be installed if using the Maven wrapper.

- `git clone https://github.com/D2P-APPS/WebSocketTest.git`

- `cd WebSocketTest`

- `docker pull redis`

- `docker run -d -p 6379:6379 redis`

- `docker exec -it [insert redis container id here] redis-cli`

- `config set notify-keyspace-events KEA`

- Ctrl+D to exit the container

- `chmod +x mvnw`

- `./mvnw clean package`

- `./mvnw spring-boot:run`

- Go to: `http://localhost:8080/websocket-api/swagger-ui.html#/` to view the Swagger page

- As of right now, the Angular app that uses this application is looking for a traffic light with id 'test123'. Please create a traffic light with that id prior to running the Angular app.

## Features:
- Traffic lights are stored in a Redis Cache

- Application offers create, read, and delete operations for Traffic lights as well as a web socket endpoint to keep track of a specific traffic light's changing colors.

- Makes use of Redis Keyspace Notifications which fire off events whenever an operation on a key is performed. In this case, the app sends updates on the traffic light's color to the client whenever a change in Redis is detected. 
