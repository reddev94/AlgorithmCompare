FROM openjdk:20
WORKDIR /algorithm-compare-be
COPY Core/target/Core-1.0-SNAPSHOT.jar algorithm-compare-be.jar
COPY plugins-zipped/* /algorithm-compare-be/plugins-zipped/
EXPOSE 8080
CMD ["java", "-jar", "algorithm-compare-be.jar"]