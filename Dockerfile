FROM openjdk:8-jre-alpine
COPY /target/wlcp-api-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8082
CMD ["/usr/bin/java", "-jar", "app.jar"]