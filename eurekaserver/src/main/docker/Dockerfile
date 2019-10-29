FROM openjdk:8-jdk-alpine
RUN apk update && apk upgrade && apk add netcat-openbsd && apk add curl
RUN mkdir -p /usr/local/eurekaserver
RUN echo $JAVA_HOME
ADD @project.build.finalName@.jar /usr/local/eurekaserver/
ADD run.sh run.sh
RUN chmod +x run.sh
EXPOSE 8761
ENTRYPOINT ["sh", "/run.sh"]