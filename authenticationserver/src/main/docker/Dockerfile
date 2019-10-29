FROM openjdk:8-jdk-alpine
RUN apk update && apk upgrade && apk add netcat-openbsd && apk add curl
RUN mkdir -p /usr/local/authenticationservice
RUN echo $JAVA_HOME
ADD @project.build.finalName@.jar /usr/local/authenticationservice/
ADD run.sh run.sh
RUN chmod +x run.sh
EXPOSE 8901
ENV PROFILE=default
ENV SERVER_PORT=8901
ENV CONFIGSERVER_URI=http://configserver:8888
ENV EUREKASERVER_URI=http://eurekaserver:8761/eureka/
ENV EUREKASERVER_PORT=8761
ENV CONFIGSERVER_PORT=8888
ENV ENCRYPT_KEY=IMSYMMETRIC
ENTRYPOINT ["sh", "/run.sh"]