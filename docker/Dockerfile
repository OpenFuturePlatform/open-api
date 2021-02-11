FROM openjdk:11.0.10-jdk

WORKDIR root/

ADD build/libs/*.jar ./application.jar

CMD java -Xmx1024M -Djava.security.egd=/dev/zrandom \
    -jar /root/application.jar
