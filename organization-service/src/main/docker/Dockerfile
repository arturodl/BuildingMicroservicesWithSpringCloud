FROM openjdk:8-jdk-alpine
RUN apk update && apk upgrade && apk add netcat-openbsd && apk add curl
RUN mkdir -p /usr/local/organizationservice
RUN echo $JAVA_HOME
ADD @project.build.finalName@.jar /usr/local/organizationservice/
ADD run.sh run.sh
RUN chmod +x run.sh
EXPOSE 8080
ENV PROFILE=default
ENV SERVER_PORT=8080
ENV CONFIGSERVER_URI=http://configserver:8888
ENV EUREKASERVER_URI=http://eurekaserver:8761/eureka/
ENV KAFKASERVER_URI=kafkaserver
ENV ZKSERVER_URI=kafkaserver
ENV EUREKASERVER_PORT=8761
ENV CONFIGSERVER_PORT=8888
ENV DATABASESERVER_PORT=5432
ENV ZUULSERVER_PORT=5555
ENV KAFKASERVER_PORT=2181
ENV REDIS_PORT=6379
ENV ENCRYPT_KEY=IMSYMMETRIC
ENTRYPOINT ["sh", "/run.sh"]