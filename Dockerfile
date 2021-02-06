FROM maven:3.6.3-openjdk-15-slim AS MAVEN_BUILD
 
COPY ./ ./
 
RUN mvn clean package
 
FROM tomcat:9.0.43-jdk15-openjdk

COPY --from=MAVEN_BUILD /target/vote4lunch.war /usr/local/tomcat/webapps/

EXPOSE 8080

CMD ["catalina.sh", "run"]
