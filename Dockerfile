FROM tomcat:7
COPY /DockerCpu/ /usr/local/tomcat/webapps/
CMD ["catalina.sh", "run"]
