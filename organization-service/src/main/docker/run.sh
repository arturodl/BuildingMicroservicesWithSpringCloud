#!/bin/sh
echo "********************************************************"
echo "Waiting for the eureka server to start on port $EUREKASERVER_PORT"
echo "********************************************************"

echo "********************************************************"
echo "Starting Organization Service                           "
echo "********************************************************"
java -Djava.security.egd=file:/dev/./urandom -Dserver.port=$SERVER_PORT -Deureka.client.serviceUrl.defaultZone=$EUREKASERVER_URI -Dspring.cloud.config.uri=$CONFIGSERVER_URI -Dspring.profiles.active=$PROFILE -Dspring.cloud.stream.kafka.binder.zkNodes=$KAFKASERVER_URI -Dspring.cloud.stream.kafka.binder.brokers=$ZKSERVER_URI -jar /usr/local/organizationservice/@project.build.finalName@.jar