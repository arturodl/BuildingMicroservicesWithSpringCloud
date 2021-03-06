#!/bin/sh
getPort() {
    echo $1 | cut -d : -f 3 | xargs basename
}

echo "********************************************************"
echo "Waiting for the eureka server to start on port $(getPort $EUREKASERVER_PORT)"
echo "********************************************************"
while ! `nc -z eurekaserver  $(getPort $EUREKASERVER_PORT)`; do sleep 3; done
echo "******* Eureka Server has started"

echo "********************************************************"
echo "Waiting for the configuration server to start on port $(getPort $CONFIGSERVER_PORT)"
echo "********************************************************"
while ! `nc -z configserver $(getPort $CONFIGSERVER_PORT)`; do sleep 3; done
echo "*******  Configuration Server has started"

echo "********************************************************"
echo "Waiting for the licensing service to start on port $(getPort $LICENSINGSERVICE_PORT)"
echo "********************************************************"
while ! `nc -z licensingservice $(getPort $LICENSINGSERVICE_PORT)`; do sleep 3; done
echo "*******  Licencing Service has started"

echo "********************************************************"
echo "Waiting for the organization service to start on port $(getPort $ORGANIZATIONSERVICE_PORT)"
echo "********************************************************"
while ! `nc -z organizationservice $(getPort $ORGANIZATIONSERVICE_PORT)`; do sleep 3; done
echo "*******  Organization Service has started"

echo "********************************************************"
echo "Starting Zuul Service with $CONFIGSERVER_URI"
echo "********************************************************"
java -Djava.security.egd=file:/dev/./urandom -Dserver.port=$SERVER_PORT   \
     -Deureka.client.serviceUrl.defaultZone=$EUREKASERVER_URI   \
     -Dspring.cloud.config.uri=$CONFIGSERVER_URI                \
     -Dspring.profiles.active=$PROFILE                          \
     -jar /usr/local/zuulservice/@project.build.finalName@.jar