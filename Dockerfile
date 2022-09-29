FROM gradle:7.4.1 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build

FROM amazoncorretto:17-alpine
ENV DISCORD_TOKEN=""
COPY --from=build /home/gradle/src/build/libs/midgard.jar /app/
CMD ["java", "-jar", "/app/midgard.jar"]