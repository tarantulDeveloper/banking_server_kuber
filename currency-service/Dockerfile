FROM openjdk:17-alpine
COPY target/currency-service-0.0.1-SNAPSHOT.jar currency.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "currency.jar"]