# Use the official Gradle image to build the application
#FROM gradle:8.8-jdk17 AS build

# Set the working directory
#WORKDIR /app

#USER root

# Copy the project files
#COPY . .

# Package the Spring Boot application
#RUN gradle build -x test

# Use the official OpenJDK image as the base image
FROM openjdk:17-jdk

RUN ./gradlew bootJar

# Set the working directory
WORKDIR /app

# Copy the packaged jar file
COPY build/libs/*.jar app.jar

# Expose port 8888
EXPOSE 8888

# Run the Spring Boot application
CMD ["java", "-jar", "app.jar"]
