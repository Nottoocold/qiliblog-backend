FROM openjdk:17-ea-slim

VOLUME /app

WORKDIR /app

COPY ./@project.build.finalName@.jar /app/app.jar
COPY ./libs /app/libs

CMD ["java", "-server", "-Dloader.path=libs", "-jar", "app.jar", "--spring.profiles.active=prod"]
