FROM openjdk:17-jdk-slim
MAINTAINER jorgetricarico
COPY /target/conversor-0.0.1-SNAPSHOT.jar conversor.jar
ENTRYPOINT ["java", "-jar", "/conversor.jar"]