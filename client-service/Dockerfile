FROM openjdk:17-alpine
COPY target/client-service-0.0.1-SNAPSHOT.jar client.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "client.jar"]