FROM openjdk:17-jdk

WORKDIR /jar

COPY test.jar .

ARG ARG_PROFILE
ENV PROFILE=${ARG_PROFILE}

ENTRYPOINT ["java", "-Dspring.profiles.active=${PROFILE}", "-Duser.timezone=Asia/Seoul", "-jar","test.jar"]
