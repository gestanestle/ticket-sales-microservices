FROM openjdk:17
ADD target/notification.jar notification.jar
EXPOSE 8085
ENTRYPOINT ["java", "-jar", "notification.jar"]