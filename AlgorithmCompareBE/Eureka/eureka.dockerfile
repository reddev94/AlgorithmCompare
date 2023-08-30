FROM openjdk:20
WORKDIR /eureka
COPY target/Eureka-1.0-SNAPSHOT.jar eureka.jar
EXPOSE 8083
CMD ["java", "-jar", "eureka.jar"]