# Use OpenJDK base image
FROM openjdk:17

# Set working directory inside the container
WORKDIR /app

# Copy the JAR file from the target directory (make sure this matches the correct path)
COPY app/target/app-0.0.1-SNAPSHOT.jar app.jar

# Run the JAR file when the container starts
ENTRYPOINT ["java", "-jar", "app.jar"]
