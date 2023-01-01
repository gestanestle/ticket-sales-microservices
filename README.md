# Ticket Sales Microservices API

Create events, purchase tickets, and send event updates from different microservices. 

This project utilizes the following technologies:
* Spring Boot
* Spring Cloud
* Spring 5 WebFlux
* Kakfa
* PostgreSQL
* MongoDB
* Gmail SMTP
* Test Containers, JUnit 5, Mockito, MockMVC
* Docker

The entire project looks like this:

![TSM architecture](https://user-images.githubusercontent.com/83026862/210166134-3fdafbd4-bfd5-48c7-a20d-229076b68fab.jpg)
 
 ## Running the applications
 
To run the microservices, build the docker images from each module with the image names indicated in the docker compose file. For the API Gateway, it will be something like this:
 ```
docker build -t gestanestle/api-gateway:1.0 .
```

Upon building the five modules, run the containers from the root directory.
```
docker compose up
```

## Swagger

The EVENT and TICKET microservices have their own corresponding API documentations. 

![event-swagger](https://user-images.githubusercontent.com/83026862/210166939-63b65417-0205-43b7-84a0-b572753181ec.png)

![ticket-swagger](https://user-images.githubusercontent.com/83026862/210166951-aef906bf-8f41-4297-87cb-27422558cf04.png)



To launch the Swagger UIs above, go to the following URLs:
```
http://localhost:9000/api/v1/event/swagger/ui
```
```
http://localhost:9000/api/v1/ticket/swagger/ui
```


## Project Structure

```bash
.
├── docker-compose.yml
├── pom.xml
├── api-gateway
│   ├── Dockerfile
│   ├── pom.xml
│   ├── src
│   │   ├── main
│   │   │   ├── java
│   │   │   │   └── com
│   │   │   │       └── krimo
│   │   │   │           └── gateway
│   │   │   │               └── ApiGatewayApplication.java
│   │   │   └── resources
│   │   │       ├── application.yml
│   │   │       └── banner.txt
│   │   └── test
│   │       └── java
│   └── target
│       ├── api-gateway.jar
│       ├── api-gateway.jar.original
│       ├── classes
├── email
│   ├── Dockerfile
│   ├── pom.xml
│   ├── src
│   │   ├── main
│   │   │   ├── java
│   │   │   │   └── com
│   │   │   │       └── krimo
│   │   │   │           └── email
│   │   │   │               ├── bean
│   │   │   │               │   └── EmailSenderService.java
│   │   │   │               ├── config
│   │   │   │               │   └── WebClientConfig.java
│   │   │   │               ├── dto
│   │   │   │               │   ├── Event.java
│   │   │   │               │   ├── Section.java
│   │   │   │               │   ├── Ticket.java
│   │   │   │               │   └── TicketList.java
│   │   │   │               ├── EmailApplication.java
│   │   │   │               └── service
│   │   │   │                   ├── EmailFormatter.java
│   │   │   │                   └── EmailService.java
│   │   │   └── resources
│   │   │       ├── application.yml
│   │   │       └── banner.txt
│   │   └── test
│   │       └── java
│   └── target
│       ├── classes
│       ├── email.jar
│       ├── email.jar.original
├── eureka
│   ├── Dockerfile
│   ├── pom.xml
│   ├── src
│   │   ├── main
│   │   │   ├── java
│   │   │   │   └── com
│   │   │   │       └── krimo
│   │   │   │           └── eureka
│   │   │   │               └── EurekaApplication.java
│   │   │   └── resources
│   │   │       ├── application.yml
│   │   │       └── banner.txt
│   │   └── test
│   │       └── java
│   └── target
│       ├── classes
│       ├── eureka.jar
│       ├── eureka.jar.original
├── event
│   ├── Dockerfile
│   ├── pom.xml
│   ├── src
│   │   ├── main
│   │   │   ├── java
│   │   │   │   └── com
│   │   │   │       └── krimo
│   │   │   │           └── event
│   │   │   │               ├── config
│   │   │   │               │   └── EventAppConfig.java
│   │   │   │               ├── controller
│   │   │   │               │   └── EventController.java
│   │   │   │               ├── data
│   │   │   │               │   ├── Event.java
│   │   │   │               │   └── Section.java
│   │   │   │               ├── dto
│   │   │   │               │   └── EventDTO.java
│   │   │   │               ├── EventApplication.java
│   │   │   │               ├── exception
│   │   │   │               │   ├── ApiExceptionHandler.java
│   │   │   │               │   ├── ApiException.java
│   │   │   │               │   └── ApiRequestException.java
│   │   │   │               ├── repository
│   │   │   │               │   └── EventRepository.java
│   │   │   │               └── service
│   │   │   │                   └── EventService.java
│   │   │   └── resources
│   │   │       ├── application-test.yml
│   │   │       ├── application.yml
│   │   │       ├── banner.txt
│   │   │       └── static
│   │   │           └── index.html
│   │   └── test
│   │       └── java
│   │           └── com
│   │               └── krimo
│   │                   └── event
│   │                       ├── config
│   │                       │   └── PostgresContainerEnv.java
│   │                       ├── container
│   │                       │   └── PostgresTestContainer.java
│   │                       ├── controller
│   │                       │   └── EventControllerTest.java
│   │                       ├── data
│   │                       │   ├── Section.java
│   │                       │   └── TestEntityBuilder.java
│   │                       ├── repository
│   │                       │   └── EventRepositoryTest.java
│   │                       └── service
│   │                           └── EventServiceTest.java
│   └── target
│       ├── classes
│       ├── event.jar
│       ├── event.jar.original
└── ticket
    ├── Dockerfile
    ├── pom.xml
    ├── src
    │   ├── main
    │   │   ├── java
    │   │   │   └── com
    │   │   │       └── krimo
    │   │   │           └── ticket
    │   │   │               ├── client
    │   │   │               │   └── EventClient.java
    │   │   │               ├── config
    │   │   │               │   └── TicketAppConfig.java
    │   │   │               ├── controller
    │   │   │               │   └── TicketController.java
    │   │   │               ├── data
    │   │   │               │   ├── Event.java
    │   │   │               │   ├── Section.java
    │   │   │               │   └── Ticket.java
    │   │   │               ├── dto
    │   │   │               │   ├── CustomerDTO.java
    │   │   │               │   ├── EventList.java
    │   │   │               │   ├── ReturnObject.java
    │   │   │               │   └── TicketList.java
    │   │   │               ├── exception
    │   │   │               │   ├── ApiExceptionHandler.java
    │   │   │               │   ├── ApiException.java
    │   │   │               │   └── ApiRequestException.java
    │   │   │               ├── repository
    │   │   │               │   └── TicketRepository.java
    │   │   │               ├── service
    │   │   │               │   └── TicketService.java
    │   │   │               └── TicketApplication.java
    │   │   └── resources
    │   │       ├── application-test.yml
    │   │       ├── application.yml
    │   │       ├── banner.txt
    │   │       └── static
    │   │           └── index.html
    │   └── test
    │       └── java
    │           └── com
    │               └── krimo
    │                   └── ticket
    │                       ├── config
    │                       │   └── MongoContainerEnv.java
    │                       ├── container
    │                       │   └── MongoTestContainer.java
    │                       ├── controller
    │                       │   └── TicketControllerTest.java
    │                       ├── data
    │                       │   ├── Section.java
    │                       │   └── TestEntityBuilder.java
    │                       ├── repository
    │                       │   └── TicketRepositoryTest.java
    │                       └── service
    │                           └── TicketServiceTest.java
    └── target
        ├── classes
        ├── ticket.jar
        └── ticket.jar.original

```
