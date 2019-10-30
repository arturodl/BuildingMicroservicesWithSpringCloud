FROM openjdk:8-jdk-alpine
RUN apk update && apk upgrade && apk add netcat-openbsd && apk add curl
RUN mkdir -p /usr/local/zuulservice
RUN echo $JAVA_HOME
ADD @project.build.finalName@.jar /usr/local/zuulservice/
ADD run.sh run.sh
RUN chmod +x run.sh
EXPOSE 5555
ENV PROFILE=default
ENV SERVER_PORT=5555
ENV CONFIGSERVER_URI=http://configserver:8888
ENV EUREKASERVER_URI=http://eurekaserver:8761/eureka/
ENV EUREKASERVER_PORT=8761
ENV CONFIGSERVER_PORT=8888
ENV LICENSINGSERVICE_PORT=8090
ENV ORGANIZATIONSERVICE_PORT=8080
ENV ENCRYPT_KEY=IMSYMMETRIC
ENTRYPOINT ["sh", "/run.sh"]