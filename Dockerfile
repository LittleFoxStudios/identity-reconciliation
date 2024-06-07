FROM openjdk:17-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY ./target/identityreconciliation-0.0.1-SNAPSHOT.jar app.jar
LABEL version="1.0"
ENTRYPOINT ["java","-jar","/app.jar"]