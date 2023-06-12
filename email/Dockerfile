FROM maven:3.8.4-openjdk-11-slim AS build

WORKDIR /email

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

# Create the final Docker image
FROM openjdk:11-jre-slim

# Set the working directory in the container
WORKDIR /email

# Copy the built JAR file from the previous stage
COPY --from=build /email/target/email.jar .

# Expose the application port
EXPOSE 8083

# Set the command to run the application
CMD ["java", "-jar", "email.jar"]
