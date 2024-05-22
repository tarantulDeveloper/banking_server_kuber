FROM openjdk:17-alpine
COPY target/transaction-service-0.0.1-SNAPSHOT.jar transaction.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "transaction.jar"]