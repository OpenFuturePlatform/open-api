FROM openjdk:12.0.2-jdk-oracle

WORKDIR root/

ADD build/libs/*.jar ./application.jar

CMD java -Xmx1024M -Djava.security.egd=/dev/zrandom \
    -jar /root/application.jar
