FROM tomcat:7
ADD DockerCpu /usr/local/tomcat/webapps/
CMD ["catalina.sh", "run"]
