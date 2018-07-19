FROM tomcat:9.0
WORKDIR /usr/local/tomcat/webapps/
COPY kafkademo/target/kafkademo/ $WORKDIR

CMD ["/usr/local/tomcat/webapps/kafkademo/initDemo.sh", "run"]