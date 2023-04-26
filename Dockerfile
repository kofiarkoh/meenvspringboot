# Define the first stage
FROM maven:3.9.0-eclipse-temurin-17-alpine AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Define the second stage
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Set the environment variables for the database connection
ENV DB_HOST=85.10.205.173
ENV DB_PORT=3306
ENV DB_NAME=meenvspringboot
ENV DB_USER=dsfhdsfhs
ENV DB_PASSWORD=Vbnm789090@

# Expose port 8080
EXPOSE 8080

# Run the application with the database connection configuration
CMD ["java", "-jar", "app.jar", "--spring.datasource.url=jdbc:mysql://${DB_HOST}:${DB_PORT}/${DB_NAME}", "--spring.datasource.username=${DB_USER}", "--spring.datasource.password=${DB_PASSWORD}"]
