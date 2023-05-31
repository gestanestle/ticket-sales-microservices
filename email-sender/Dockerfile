FROM openjdk:17
ADD target/email.jar email.jar
EXPOSE 8083
ENTRYPOINT ["java", "-jar", "email.jar"]