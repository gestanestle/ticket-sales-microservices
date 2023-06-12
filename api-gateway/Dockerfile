FROM maven:3.8.4-openjdk-11-slim AS build

WORKDIR /api-gateway

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

# Create the final Docker image
FROM openjdk:11-jre-slim

# Set the working directory in the container
WORKDIR /api-gateway

# Copy the built JAR file from the previous stage
COPY --from=build /api-gateway/target/api-gateway.jar .

# Expose the application port
EXPOSE 9000

# Set the command to run the application
CMD ["java", "-jar", "api-gateway.jar"]
