FROM adoptopenjdk/openjdk11:alpine-jre
COPY target/UserMicroService-0.0.1-SNAPSHOT.jar service
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "service"]
