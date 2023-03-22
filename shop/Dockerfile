FROM openjdk:17-jdk

WORKDIR /app

COPY build/libs/shop-0.0.1-SNAPSHOT.jar app.jar

COPY --from=docker.elastic.co/observability/apm-agent-java:latest /usr/agent/elastic-apm-agent.jar elastic-apm-agent.jar

ENV JAVA_TOOL_OPTIONS="-javaagent:/app/elastic-apm-agent.jar"
ENV ELASTIC_APM_SERVICE_NAME="shop-service"
ENV ELASTIC_APM_SERVER_URL="http://172.17.0.1:8200"
ENV ELASTIC_APM_APPLICATION_PACKAGES="com.qdot.tracing"

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]