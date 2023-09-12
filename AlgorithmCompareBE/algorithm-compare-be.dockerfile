# build all module
FROM maven:3.9.4-eclipse-temurin-20 as build
WORKDIR /AlgorithmCompareBE

COPY pom.xml .

COPY AdminMonitor/pom.xml AdminMonitor/
COPY AdminMonitor/src/main AdminMonitor/src/main

COPY Common/pom.xml Common/
COPY Common/src/main Common/src/main

COPY Core/pom.xml Core/
COPY Core/src/main Core/src/main

COPY Eureka/pom.xml Eureka/
COPY Eureka/src/main Eureka/src/main

COPY Gateway/pom.xml Gateway/
COPY Gateway/src/main Gateway/src/main

COPY Plugins/pom.xml Plugins/

COPY Plugins/PluginModel/pom.xml Plugins/PluginModel/
COPY Plugins/PluginModel/src/main Plugins/PluginModel/src/main

COPY Plugins/BubbleSort/pom.xml Plugins/BubbleSort/
COPY Plugins/BubbleSort/plugin.properties Plugins/BubbleSort/
COPY Plugins/BubbleSort/src/main Plugins/BubbleSort/src/main

COPY Plugins/InsertionSort/pom.xml Plugins/InsertionSort/
COPY Plugins/InsertionSort/plugin.properties Plugins/InsertionSort/
COPY Plugins/InsertionSort/src/main Plugins/InsertionSort/src/main

COPY Plugins/MergeSort/pom.xml Plugins/MergeSort/
COPY Plugins/MergeSort/plugin.properties Plugins/MergeSort/
COPY Plugins/MergeSort/src/main Plugins/MergeSort/src/main

COPY Plugins/QuickSort/pom.xml Plugins/QuickSort/
COPY Plugins/QuickSort/plugin.properties Plugins/QuickSort/
COPY Plugins/QuickSort/src/main Plugins/QuickSort/src/main

COPY Plugins/SelectionSort/pom.xml Plugins/SelectionSort/
COPY Plugins/SelectionSort/plugin.properties Plugins/SelectionSort/
COPY Plugins/SelectionSort/src/main Plugins/SelectionSort/src/main

RUN mvn -f pom.xml clean package -Dmaven.test.skip=true

# admin-monitor image
FROM eclipse-temurin:20-jre-jammy as admin-img
WORKDIR /admin-monitor
EXPOSE 8082
COPY --from=build AlgorithmCompareBE/AdminMonitor/target/*.jar *.jar
ENTRYPOINT ["java", "-jar", "*.jar" ]

# eureka image
FROM eclipse-temurin:20-jre-jammy as eureka-img
WORKDIR /eureka
EXPOSE 8083
COPY --from=build AlgorithmCompareBE/Eureka/target/*.jar *.jar
ENTRYPOINT ["java", "-jar", "*.jar" ]

# gateway image
FROM eclipse-temurin:20-jre-jammy as gateway-img
WORKDIR /gateway
EXPOSE 8081
COPY --from=build AlgorithmCompareBE/Gateway/target/*.jar *.jar
ENTRYPOINT ["java", "-jar", "*.jar" ]

# core image
FROM eclipse-temurin:20-jre-jammy as core-img
WORKDIR /core
EXPOSE 8084
COPY plugins-zipped/*.zip plugins-zipped/
COPY --from=build AlgorithmCompareBE/Core/target/*.jar *.jar
ENTRYPOINT ["java", "-jar", "*.jar" ]