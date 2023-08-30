FROM openjdk:20
WORKDIR /algorithm-compare-be
COPY Core/target/Core-1.0-SNAPSHOT.jar algorithm-compare-be.jar
COPY plugins-zipped/*.zip plugins-zipped/
EXPOSE 8084
CMD ["java", "-jar", "algorithm-compare-be.jar"]