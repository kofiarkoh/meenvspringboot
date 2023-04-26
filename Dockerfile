# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory to /app
FROM maven:3.9.0-eclipse-temurin-17-alpine AS build
COPY ..
RUN mun clean package -DskipTests
#RUN mvn clean package

# Copy the application jar file to the container
COPY target/*.jar app.jar

# Expose port 8080
EXPOSE 8080

# Run the application when the container starts
CMD ["java", "-jar", "app.jar"]
