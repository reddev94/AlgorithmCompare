FROM openjdk:20
WORKDIR /admin-monitor
COPY target/AdminMonitor-1.0-SNAPSHOT.jar admin-monitor.jar
EXPOSE 8082
CMD ["java", "-jar", "admin-monitor.jar"]