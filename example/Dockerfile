FROM openjdk:8-jre-slim
ENV JAVA_OPTIONS="-Xms256m -Xmx256m"
ENV SERVICE_NAME=example

WORKDIR /example
COPY target/google-cloud-datastore-example-*.jar /example/example.jar

EXPOSE 8080

CMD java $JAVA_OPTIONS -jar example.jar
