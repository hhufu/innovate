FROM java:8
EXPOSE 8088

VOLUME /tmp
ADD /target/innovate-admin.jar /app.jar
RUN bash -c 'touch /app.jar'
ENTRYPOINT ["java","-jar","/app.jar"]
