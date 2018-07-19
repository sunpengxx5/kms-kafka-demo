FROM tomcat:9.0
WORKDIR /usr/local/tomcat/webapps/
COPY target/kafkademo/ $WORKDIR
RUN sh $WORKDIR/kafkademo/initDemo.sh
CMD ["catalina.sh", "run"]