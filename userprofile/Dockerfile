FROM openjdk:17
ADD target/userprofile.jar userprofile.jar
EXPOSE 8084
ENTRYPOINT ["java", "-jar", "userprofile.jar"]