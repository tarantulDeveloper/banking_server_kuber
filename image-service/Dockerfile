FROM openjdk:17-alpine
COPY target/image-service-0.0.1-SNAPSHOT.jar image.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "image.jar"]