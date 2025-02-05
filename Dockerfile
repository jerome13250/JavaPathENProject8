## This uses multi-stage build :
## https://docs.docker.com/develop/develop-images/multistage-build/

## Need to add curl to the alpine for healthcheck in docker-compose.yml file

###########################
#GPS-API
###########################
# Alpine Linux with OpenJDK JRE
FROM openjdk:8-jre-alpine AS gpsapi
# Install curl for healthcheck
RUN apk --no-cache add curl
# Add folder :
RUN mkdir /tmp/app
# copy JAR into image
COPY ./gps-api/build/libs/gpsapi-1.0.0.jar /tmp/app
#api port
EXPOSE 9001
# run java app:
CMD java -jar /tmp/app/gpsapi-1.0.0.jar

###########################
#REWARD-API
###########################
# Alpine Linux with OpenJDK JRE
FROM openjdk:8-jre-alpine AS rewardapi
# Install curl for healthcheck
RUN apk --no-cache add curl
# Add folder :
RUN mkdir /tmp/app
# copy JAR into image
COPY ./reward-api/build/libs/rewardapi-1.0.0.jar /tmp/app
#api port
EXPOSE 9002
# run java app:
CMD java -jar /tmp/app/rewardapi-1.0.0.jar

###########################
#TRIPPRICER-API
###########################
# Alpine Linux with OpenJDK JRE
FROM openjdk:8-jre-alpine AS trippricerapi
# Install curl for healthcheck
RUN apk --no-cache add curl
# Add folder :
RUN mkdir /tmp/app
# copy JAR into image
COPY ./trippricer-api/build/libs/trippricerapi-1.0.0.jar /tmp/app
#api port
EXPOSE 9003
# run java app:
CMD java -jar /tmp/app/trippricerapi-1.0.0.jar

###########################
#WEBAPP
###########################
# Alpine Linux with OpenJDK JRE
FROM openjdk:8-jre-alpine AS webapp
# Add folder :
RUN mkdir /tmp/app
# copy JAR into image
COPY ./webapp/build/libs/webapp-1.0.0.jar /tmp/app
#api port
EXPOSE 9000
# run java app:
CMD java -jar /tmp/app/webapp-1.0.0.jar