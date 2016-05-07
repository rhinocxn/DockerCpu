FROM tomcat:7
ADD /DockerTest /usr/local//tomcat/webapps/
CMD ["catalina.sh", "run"]
