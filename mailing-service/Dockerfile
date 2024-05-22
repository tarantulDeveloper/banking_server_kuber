FROM openjdk:17-alpine
COPY target/mailing-service-0.0.1-SNAPSHOT.jar mailing.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "mailing.jar"]