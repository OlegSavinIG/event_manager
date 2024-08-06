FROM openjdk:11-jre-slim
VOLUME /tmp
COPY target/ewm-service-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]