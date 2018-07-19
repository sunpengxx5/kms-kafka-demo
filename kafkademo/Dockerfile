FROM tomcat:9.0

ENV bootstrapServers dms-kafka.cn-north-1.myhuaweicloud.com:37003
ENV accessKey G14IYNXRHFV9ISEWHEMW
ENV secretKey ZGaTtLs1KwcRCOlfmzFMBJ9lqbsZJoyU3Kcgr5gh
ENV projectId 0e964f1ae251477ebbfe8cd85e758ee6

WORKDIR /usr/local/tomcat/webapps/
COPY kafkademo/target/kafkademo $WORKDIR

CMD ["/usr/local/tomcat/webapps/kafkademo/initDemo.sh", "run"]