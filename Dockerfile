FROM tomcat:9.0-jdk17

# Clean default ROOT app
RUN rm -rf /usr/local/tomcat/webapps/*

# Copy your WAR file
COPY target/spring-mvc-bug-tracking-app-1.0.0-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080

CMD ["catalina.sh", "run"]
