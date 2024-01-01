FROM maven:3-eclipse-temurin-21

WORKDIR /app

COPY mvnw .
COPY mvnw.cmd .
COPY pom.xml .

COPY .mvn .mvn
COPY src src

RUN mvn package

ENV PORT=8080
ENV SPRING_REDIS_HOST=localhost SPRING_REDIS_PORT=6379
ENV SPRING_REDIS_USERNAME= SPRING_REDIS_PASSWORD=
ENV NEWSAPI_KEY="placeholder"

EXPOSE ${PORT}

ENTRYPOINT SERVER_PORT=${PORT} java -jar target/pokemon-0.0.1-SNAPSHOT.jar