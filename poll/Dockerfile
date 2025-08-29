FROM openjdk:17-jdk-slim

WORKDIR /app

COPY ./build/libs/poll-0.0.1-SNAPSHOT-plain.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]
