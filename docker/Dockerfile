FROM openjdk:8-jre-alpine

WORKDIR root/

ADD build/libs/api-*.jar ./application.jar

CMD java -Xmx1024M \
    -jar /root/application.jar
