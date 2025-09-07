FROM openjdk:17-jre-slim

VOLUME /app

WORKDIR /app

COPY ./@project.build.finalName@.jar /app/app.jar
COPY ./libs /app/libs

ENV JAVA_OPTS="-server \
               -Dloader.path=libs \
               -Dspring.profiles.active=prod"

ENTRYPOINT ["/bin/sh", "-c", "java $JAVA_OPTS -jar app.jar"]