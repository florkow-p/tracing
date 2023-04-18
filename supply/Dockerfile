FROM openjdk:17-jdk

WORKDIR /app

COPY build/libs/supply-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8082

ENTRYPOINT ["java","-jar","app.jar"]