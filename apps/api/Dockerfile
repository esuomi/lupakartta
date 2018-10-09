# Never use "latest" as base container but specific tagged version instead
FROM openjdk:8u151-jre

LABEL maintainer="esko.suomi@siili.com"

# Copy application JAR(s) to /app
COPY target/uberjar/api-0.1.0-SNAPSHOT-standalone.jar /app/api.jar
COPY app.sh /
RUN chmod +x /app.sh

ENTRYPOINT ["/app.sh"]

