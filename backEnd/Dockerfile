# Use the official OpenJDK image as the base image
FROM openjdk:17-jdk-alpine

# 필요 패키지 설치 (xargs 포함)
RUN apk add --no-cache findutils

# Copy the Gradle wrapper and project files
COPY . /app

# Set the working directory
WORKDIR /app

# Ensure the Gradle wrapper script has execution permissions
RUN chmod +x ./gradlew

# Build the project
RUN ./gradlew bootJar

# Copy the packaged jar file
COPY build/libs/*.jar app.jar

# Expose port 8888
EXPOSE 8888

# Run the Spring Boot application
CMD ["java", "-jar", "app.jar"]
