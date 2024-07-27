FROM openjdk:17-jdk
LABEL maintainer="sixtale"
ARG JAR_FILE=build/libs/SIXTALEBackEnd-0.0.1-SNAPSHOT-plain.jar
COPY ${JAR_FILE} docker-springboot.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/docker-springboot.jar"]
