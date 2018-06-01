FROM openjdk:8-jre-alpine

WORKDIR root/

ADD build/libs/api-*.jar ./application.jar

CMD java -Xmx128M \
    -jar /root/application.jar
