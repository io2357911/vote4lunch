FROM maven:3.6.1-jdk-8-alpine AS MAVEN_BUILD
 
COPY ./ ./
 
RUN mvn clean package
 
FROM tomcat:9.0.41-jdk8-openjdk

COPY --from=MAVEN_BUILD /target/vote4lunch.war /usr/local/tomcat/webapps/

EXPOSE 8080

CMD ["catalina.sh", "run"]
