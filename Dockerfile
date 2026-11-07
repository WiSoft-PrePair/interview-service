FROM eclipse-temurin:21-jre-noble

WORKDIR /app

RUN apt-get update \
    && apt-get install -y --no-install-recommends ffmpeg \
    && rm -rf /var/lib/apt/lists/*

COPY build/libs/*.jar app.jar

EXPOSE 7300

ENTRYPOINT ["java", "-jar", "app.jar"]
