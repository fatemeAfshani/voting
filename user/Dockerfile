# ---- Stage 1: Build ----
FROM gradle:8.5-jdk17-alpine AS build

WORKDIR /app

COPY . .

RUN gradle bootJar --no-daemon

# ---- Stage 2: Run ----
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY --from=build app/build/libs/*.jar app.jar

ENV SPRING_PROFILES_ACTIVE=production

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
