# Stage 1: Build the application
FROM maven:3.9.9-sapmachine-21 AS builder

# Set the working directory
WORKDIR /app

# Copy the Maven configuration file and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the application source code
COPY src/ ./src/

# Build the application package, skipping tests for faster build
RUN mvn clean package -DskipTests=true

# Stage 2: Production image
FROM sapmachine:21-jdk-headless-ubuntu-20.04 AS prod

# Install wget and dockerize to wait for MySQL to be ready
RUN apt-get update && apt-get install -y wget \
    && wget https://github.com/jwilder/dockerize/releases/download/v0.6.1/dockerize-linux-amd64-v0.6.1.tar.gz \
    && tar -xvzf dockerize-linux-amd64-v0.6.1.tar.gz \
    && mv dockerize /usr/local/bin/

# Set the working directory
WORKDIR /app

# Copy the application JAR file from the builder stage
COPY --from=builder /app/target/*.jar /app/app.jar

# Environment variables for the Spring application
ENV SPRING_SERVER_PORT=8085 \
    SPRING_APP_NAME=Chatapp

# Expose the application port
EXPOSE 8085

# Wait for MySQL to be available before starting the Spring Boot application
ENTRYPOINT ["sh", "-c", "dockerize -wait tcp://mysql:3306 -timeout 60s && java -jar /app/app.jar"]
