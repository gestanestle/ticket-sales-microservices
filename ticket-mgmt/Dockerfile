FROM openjdk:17
ADD target/ticket.jar ticket.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "ticket.jar"]