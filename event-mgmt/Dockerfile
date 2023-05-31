FROM openjdk:17
ADD target/event.jar event.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "event.jar"]