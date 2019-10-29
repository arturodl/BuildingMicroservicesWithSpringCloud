#!/bin/sh
echo "********************************************************"
echo "Starting License Server with Configuration Service via Eureka :  $EUREKASERVER_URI" ON PORT: $SERVER_PORT;
echo "********************************************************"
java -Deureka.client.serviceUrl.defaultZone=$EUREKASERVER_URI -Dserver.port=$SERVER_PORT  -Dspring.cloud.config.uri=$CONFIGSERVER_URI -Dspring.profiles.active=$PROFILE -Dspring.cloud.stream.kafka.binder.zkNodes=$KAFKASERVER_URI -Dspring.cloud.stream.kafka.binder.brokers=$ZKSERVER_URI -jar /usr/local/licensingservice/@project.build.finalName@.jar