FROM tomcat:7
ADD ./ /usr/local/tomcat/webapps/
CMD ["catalina.sh", "run"]
