FROM openjdk:17-jdk
LABEL maintainer="sixtale"
ARG JAR_FILE=gradle/wrapper/gradle-wrapper.jar
ADD ${JAR_FILE} docker-springboot.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/docker-springboot.jar"]
