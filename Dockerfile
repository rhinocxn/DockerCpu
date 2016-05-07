FROM tomcat:7
ADD /DockerTest /usr/local/tomcat/webapps/DockerTest
CMD ["catalina.sh", "run"]
