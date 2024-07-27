FROM openjdk:17-jdk 
LABEL maintainer="sixtale"
COPY build/libs/SIXTALEBackEnd-0.0.1-SNAPSHOT.jar docker-springboot.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/docker-springboot.jar"]
