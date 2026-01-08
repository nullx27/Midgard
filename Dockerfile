FROM gradle:latest AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build

FROM eclipse-temurin:21-alpine-3.23
ENV DISCORD_TOKEN=""
COPY --from=build /home/gradle/src/build/libs/midgard.jar /app/
CMD ["java", "-jar", "/app/midgard.jar"]