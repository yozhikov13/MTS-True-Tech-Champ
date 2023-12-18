FROM gradle:jdk17 AS build

COPY --chown=gradle:gradle . /home/gradle/src

WORKDIR /home/gradle/src
RUN rm -rf /home/gradle/src/build/libs/*.jar
RUN gradle build --no-daemon -x test 

FROM openjdk:17-alpine

RUN mkdir -p /app/tmp
WORKDIR /app
COPY --from=build /home/gradle/src/build/libs/*.jar /app/
RUN mv /app/cloud-0.0.1-SNAPSHOT.jar /app/app.jar

ENTRYPOINT [ "/bin/sh", "-c", "java -Djava.security.egd=file:/dev/./urandom -jar /app/app.jar"]
