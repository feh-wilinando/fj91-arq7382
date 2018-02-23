FROM java:8-jre-alpine

ARG JAR_FILE

RUN mkdir /var/app

COPY ${JAR_FILE} /var/app/default.jar

WORKDIR /var/app

EXPOSE 8080

CMD java -jar default.jar