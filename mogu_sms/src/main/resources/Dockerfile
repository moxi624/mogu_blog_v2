FROM java:alpine
VOLUME /tmp
ADD mogu_sms-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-Xms256m","-Xmx256m","-jar","/app.jar"]
