FROM java:8
ARG JAR_FILE=upupor-web/target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar","-Xmx1024m","-Xms1024m"]