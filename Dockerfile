FROM tomcat:7
<<<<<<< HEAD
ADD DockerCpu /usr/local/tomcat/webapps/
=======
COPY /DockerCpu /usr/local/tomcat/webapps
>>>>>>> 185c5712c9c38428fb78dd0b36aaa66e7c846b3b
CMD ["catalina.sh", "run"]
