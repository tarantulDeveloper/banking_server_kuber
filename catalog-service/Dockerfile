FROM openjdk:17-alpine
COPY target/catalog-service-0.0.1-SNAPSHOT.jar catalog.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "catalog.jar"]