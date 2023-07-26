![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white) ![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white) ![Apache Kafka](https://img.shields.io/badge/Apache%20Kafka-000?style=for-the-badge&logo=apachekafka) ![Postgres](https://img.shields.io/badge/postgres-%23316192.svg?style=for-the-badge&logo=postgresql&logoColor=white) ![Redis](https://img.shields.io/badge/redis-%23DD0031.svg?style=for-the-badge&logo=redis&logoColor=white) ![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white) ![Prometheus](https://img.shields.io/badge/Prometheus-E6522C?style=for-the-badge&logo=Prometheus&logoColor=white) ![Grafana](https://img.shields.io/badge/grafana-%23F46800.svg?style=for-the-badge&logo=grafana&logoColor=white)

# TICKET SALES MICROSERVICES
_Organize events and purchase tickets from different services._

| Links | Description | 
|-------|-------------|
| [![General Badge](https://img.shields.io/badge/version-2.0-COLOR.svg?logo=LOGO")](https://github.com/gestanestle/ticket-sales-microservices/tree/v2.0) | Backend - v2.0 |
| [![General Badge](https://img.shields.io/badge/version-1.0-COLOR.svg?logo=LOGO")](https://github.com/gestanestle/ticket-sales-microservices/tree/v1.0) | Backend - v1.0 |

### Technologies
Ticket Sales Microservices uses a number of tools and frameworks to work properly:

- Java, PostgreSQL, Redis
- Spring Boot - Web, Actuator
- Spring Cloud - Feign Client, Eureka Server/Client, API Gateway
- Spring Data JPA, Spring JDBC, Flyway
- Spring Data Redis, Jedis
- JUnit, Mockito, MockMVC, Test Containers
- Java Mail Sender, Gmail SMTP
- Apache Kafka, Zookeper
- Debezium
- Docker, Docker Compose
- Prometheus
- Grafana
- Swagger

## THE ARCHITECTURE

![image](https://github.com/gestanestle/ticket-sales-microservices/assets/83026862/58a24391-a4e8-4aff-ab39-3aa170578234)

### Event-Driven and CDC
This ticket sales system is based on event-driven architecture. It implements the Outbox Pattern with CDC, through the use of Debezium, to capture data change from PostgreSQL and publish  the records to Apache Kafka. On the other hand, the consumer subscribes to the topic and caches the broker message to Redis. For our use-case, the producer is the Ticket Service, while the consumer is the Notification Service. The event is "ticket purchase"; the post-event is the confirmation email. </br>

This is the sequence diagram of such event: 

![image](https://github.com/gestanestle/ticket-sales-microservices/assets/83026862/0c7d3625-ed11-4928-940e-9ff789100c88)

### Shared Database
Event Management and Ticket Sales are the two main contexts of this application. These services share some data needed to be able to operate successfully. It follows a microservices pattern called 'shared database', with whose objects are set with fine-grained access privileges accordingly.

### Database Migration
While the API layer used for Hibernate are Spring Data JPA and Spring JDBC, the DDL, unlike the previous version, is set to 'update'. The schema creation, versioning, and migrations are all managed by Flyway.

### Testing
The services Event, Ticket , and Notification are unit-tested with JUnit, Mockito, and MockMVC, and DB-integration tested with Test Containers.

### Monitoring

![graf](https://github.com/gestanestle/ticket-sales-microservices/assets/83026862/2a63aedb-28d3-468e-95cc-e4285e6ddacc)

Spring Boot Actuator exposes the metrics of each services, including the API Gateway and Discovery Server, while a third-party exporter exposes the metrics of the database. Prometheus scrapes these, serving as datasources to Grafana, which then visualizes the data in the dashboards. The endpoints are accessible through ports 9090 and 3000, respectively.

## RUNNING THE APPLICATIONS

Navigate to the root directory and start the containers.
```
docker compose up -d
```
Before anything else, Debezium has to be configured properly, otherwise no payload is going to be published to the message broker. The JSON file is already provided in the directory [./connectors] and must only be sent to the service itself through POST request. Copy the command below and paste it in the terminal.
```
curl -X POST localhost:8083/connectors -H "Content-Type: application/json" -d @./connectors/debezium.json
```

### Service dependency to be considered
The properties [ created by ] in Event Service and [ purchased by ] in Ticket Service refer to the auto-generated User ID in User Profile. To be able to organize events and purchase tickets, the user has to be present in the database first. Create a profile with the necessary fields in the URL given below. To learn more, see API Documentation.
```
http://localhost:9000/api-v2-user-profiles
```
> _Due to the absence of a proper Auth Service, this application assumes that any email addresses provided by the users are verified. When creating events and purchasing tickets, **proceed with caution**._

### API Documentation
The services User Profile, Event, and Ticket have their own OpenAPI specifications. To access the API Docs, go to the following URLs:
```
http://localhost:9000/api/v2/user-profiles/swagger/ui
http://localhost:9000/api/v2/events/swagger/ui
http://localhost:9000/api/v2/tickets/swagger/ui
```
For Ticket Service, it will be as follows: </br>

![tckt](https://github.com/gestanestle/ticket-sales-microservices/assets/83026862/1c378372-a311-4d58-a772-908676222ef9)

### Managing the records
You can check the roles and schema present in the database with the command provided below. The password is [ postgres ].
```
sudo psql -h localhost -p 5432 -U postgres
```
Upon ticket purchase, you can listen to the kafka topic and check the messages by entering these in the terminal:
```
docker exec -ti kafka bash
kafka-console-consumer --topic outbox.event.ticket_purchase --bootstrap-server kafka:29092 --from-beginning
```
Check if the notification service properly saves the messages in Redis with:
```
docker exec -ti redis redis-cli
KEYS *
```

## Project Tree
```bash
.
├── api-gateway
│   ├── Dockerfile
│   ├── pom.xml
│   ├── src
│   │   └── main
│   │       ├── java
│   │       │   └── com
│   │       │       └── krimo
│   │       │           └── gateway
│   │       │               └── ApiGatewayApplication.java
│   │       └── resources
│   │           ├── application.yml
│   │           └── banner.txt
│   └── target
│       ├── api-gateway.jar
│       ├── api-gateway.jar.original
├── connectors
│   └── debezium.json
├── db-init-scripts
│   └── schema.sql
├── docker-compose.yml
├── eureka
│   ├── Dockerfile
│   ├── pom.xml
│   ├── src
│   │   └── main
│   │       ├── java
│   │       │   └── com
│   │       │       └── krimo
│   │       │           └── eureka
│   │       │               └── EurekaApplication.java
│   │       └── resources
│   │           ├── application.yml
│   │           └── banner.txt
│   └── target
│       ├── eureka.jar
│       ├── eureka.jar.original
├── event
│   ├── Dockerfile
│   ├── pom.xml
│   ├── src
│   │   ├── main
│   │   │   ├── java
│   │   │   │   └── com
│   │   │   │       └── krimo
│   │   │   │           └── event
│   │   │   │               ├── config
│   │   │   │               │   └── EventAppConfig.java
│   │   │   │               ├── controller
│   │   │   │               │   └── EventController.java
│   │   │   │               ├── data
│   │   │   │               │   ├── Event.java
│   │   │   │               │   └── Section.java
│   │   │   │               ├── dto
│   │   │   │               │   └── EventDTO.java
│   │   │   │               ├── EventApplication.java
│   │   │   │               ├── exception
│   │   │   │               │   ├── ApiExceptionHandler.java
│   │   │   │               │   ├── ApiException.java
│   │   │   │               │   └── ApiRequestException.java
│   │   │   │               ├── repository
│   │   │   │               │   └── EventRepository.java
│   │   │   │               └── service
│   │   │   │                   └── EventService.java
│   │   │   └── resources
│   │   │       ├── application-test.yml
│   │   │       ├── application.yml
│   │   │       ├── banner.txt
│   │   │       ├── db
│   │   │       │   └── migration
│   │   │       │       ├── V2__create_tbl.sql
│   │   │       │       ├── V3__create_idx.sql
│   │   │       │       └── V4__grant_priv.sql
│   │   │       └── static
│   │   │           └── index.html
│   │   └── test
│   │       └── java
│   │           └── com
│   │               └── krimo
│   │                   └── event
│   │                       ├── controller
│   │                       │   └── EventControllerTest.java
│   │                       ├── data
│   │                       │   ├── EventTest.java
│   │                       │   └── Section.java
│   │                       └── service
│   │                           └── EventServiceTest.java
│   └── target
│       ├── event.jar
│       ├── event.jar.original
├── grafana
│   ├── dashboards
│   │   ├── dashboard.yml
│   │   ├── pg-dashboard.json
│   │   └── spring-dashboard.json
│   └── datasources
│       └── datasource.yml
├── notification
│   ├── Dockerfile
│   ├── pom.xml
│   ├── src
│   │   ├── main
│   │   │   ├── java
│   │   │   │   └── com
│   │   │   │       └── krimo
│   │   │   │           └── notification
│   │   │   │               ├── client
│   │   │   │               │   └── UserProfileClient.java
│   │   │   │               ├── config
│   │   │   │               │   ├── KafkaConsumerConfig.java
│   │   │   │               │   └── RedisConfig.java
│   │   │   │               ├── message
│   │   │   │               │   ├── BrokerMessage.java
│   │   │   │               │   └── payload
│   │   │   │               │       └── TicketPurchasePayload.java
│   │   │   │               ├── NotificationApplication.java
│   │   │   │               ├── repository
│   │   │   │               │   └── BrokerMessageRepository.java
│   │   │   │               └── service
│   │   │   │                   ├── ClientService.java
│   │   │   │                   ├── MessageSenderService.java
│   │   │   │                   └── NotificationService.java
│   │   │   └── resources
│   │   │       ├── application.yml
│   │   │       └── banner.txt
│   │   └── test
│   │       └── java
│   │           └── com
│   │               └── krimo
│   │                   └── notification
│   │                       ├── repository
│   │                       │   └── BrokerMessageRepositoryImplTest.java
│   │                       └── service
│   │                           └── NotificationServiceTest.java
│   └── target
│       ├── notification.jar
│       ├── notification.jar.original
├── pom.xml
├── prometheus
│   └── prometheus.yml
├── ticket
│   ├── Dockerfile
│   ├── pom.xml
│   ├── src
│   │   ├── main
│   │   │   ├── java
│   │   │   │   └── com
│   │   │   │       └── krimo
│   │   │   │           └── ticket
│   │   │   │               ├── config
│   │   │   │               │   └── TicketAppConfig.java
│   │   │   │               ├── controller
│   │   │   │               │   ├── TicketController.java
│   │   │   │               │   └── TicketDetailsController.java
│   │   │   │               ├── dao
│   │   │   │               │   └── EventDAO.java
│   │   │   │               ├── data
│   │   │   │               │   ├── Outbox.java
│   │   │   │               │   ├── Section.java
│   │   │   │               │   ├── TicketDetails.java
│   │   │   │               │   ├── TicketDetailsPK.java
│   │   │   │               │   └── Ticket.java
│   │   │   │               ├── dto
│   │   │   │               │   ├── TicketDetailsDTO.java
│   │   │   │               │   └── TicketDTO.java
│   │   │   │               ├── exception
│   │   │   │               │   ├── ApiExceptionHandler.java
│   │   │   │               │   ├── ApiException.java
│   │   │   │               │   └── ApiRequestException.java
│   │   │   │               ├── payload
│   │   │   │               │   └── TicketPurchasePayload.java
│   │   │   │               ├── repository
│   │   │   │               │   ├── OutboxRepository.java
│   │   │   │               │   ├── TicketDetailsRepository.java
│   │   │   │               │   └── TicketRepository.java
│   │   │   │               ├── service
│   │   │   │               │   ├── TicketDetailsService.java
│   │   │   │               │   └── TicketService.java
│   │   │   │               ├── TicketApplication.java
│   │   │   │               └── utils
│   │   │   │                   └── Utils.java
│   │   │   └── resources
│   │   │       ├── application-test.yml
│   │   │       ├── application.yml
│   │   │       ├── banner.txt
│   │   │       ├── db
│   │   │       │   └── migration
│   │   │       │       ├── V5__create_tbls.sql
│   │   │       │       └── V6__create_idx.sql
│   │   │       └── static
│   │   │           └── index.html
│   │   └── test
│   │       └── java
│   │           └── com
│   │               └── krimo
│   │                   └── ticket
│   │                       ├── config
│   │                       │   └── PostgresContainerEnv.java
│   │                       ├── container
│   │                       │   └── PostgresTestContainer.java
│   │                       ├── controller
│   │                       │   ├── TicketControllerTest.java
│   │                       │   └── TicketDetailsControllerTest.java
│   │                       ├── dao
│   │                       │   └── EventDAOTest.java
│   │                       ├── data
│   │                       │   ├── Section.java
│   │                       │   ├── TicketDetailsTest.java
│   │                       │   └── TicketTest.java
│   │                       ├── repository
│   │                       │   └── TicketRepositoryTest.java
│   │                       └── service
│   │                           ├── TicketDetailsServiceTest.java
│   │                           └── TicketServiceTest.java
│   └── target
│       ├── ticket.jar
│       └── ticket.jar.original
└── userprofile
    ├── Dockerfile
    ├── pom.xml
    ├── src
    │   ├── main
    │   │   ├── java
    │   │   │   └── com
    │   │   │       └── krimo
    │   │   │           └── userprofile
    │   │   │               ├── config
    │   │   │               │   └── UserProfileAppConfig.java
    │   │   │               ├── controller
    │   │   │               │   └── UserProfileController.java
    │   │   │               ├── domain
    │   │   │               │   └── UserProfile.java
    │   │   │               ├── dto
    │   │   │               │   └── UserProfileDTO.java
    │   │   │               ├── repository
    │   │   │               │   └── UserProfileRepository.java
    │   │   │               ├── service
    │   │   │               │   └── UserProfileService.java
    │   │   │               └── UserProfileApplication.java
    │   │   └── resources
    │   │       ├── application.yml
    │   │       ├── banner.txt
    │   │       ├── db
    │   │       │   └── migration
    │   │       │       └── V1__create_tbl.sql
    │   │       └── static
    │   │           └── index.html
    │   └── test
    │       └── java
    │           └── com
    │               └── krimo
    │                   └── userprofile
    │                       └── UserprofileApplicationTests.java
    └── target
        ├── userprofile.jar
        └── userprofile.jar.original

280 directories, 265 files

```

