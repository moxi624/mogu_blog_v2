FROM registry.cn-shenzhen.aliyuncs.com/mogublog/jdk:8-alpine
VOLUME /tmp
ADD mogu_spider-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-Xms256m","-Xmx256m","-jar","/app.jar"]
