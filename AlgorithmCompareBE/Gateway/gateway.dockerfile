FROM openjdk:20
WORKDIR /gateway
COPY target/Gateway-1.0-SNAPSHOT.jar gateway.jar
EXPOSE 8080
CMD ["java", "-jar", "gateway.jar"]