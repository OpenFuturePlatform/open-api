FROM openjdk:8-jre-alpine

WORKDIR root/

ADD build/libs/*.jar ./application.jar

CMD java -Xmx1024M -Djava.security.egd=/dev/zrandom \
    -jar /root/application.jar
