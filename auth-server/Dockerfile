FROM openjdk:17-alpine
COPY target/auth-server-0.0.1-SNAPSHOT.jar auth_server.jar
EXPOSE 7070
ENTRYPOINT ["java", "-jar", "auth_server.jar"]