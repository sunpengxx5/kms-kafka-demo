FROM tomcat:9.0
WORKDIR /usr/local/tomcat/webapps/
COPY kafkademo/target/kafkademo/

CMD ["/usr/local/tomcat/webapps/kafkademo/initDemo.sh", "run"]