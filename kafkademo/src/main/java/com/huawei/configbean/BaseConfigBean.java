package com.huawei.configbean;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Properties;

public class BaseConfigBean {

    private static final String SASL_CONFIG = "dms_kafka_client_jaas.conf";

    private static final String TRUSTSTORE_PATH = "client.truststore.jks";

    private static Logger log = Logger.getLogger(BaseConfigBean.class);

    //Common param
    private String bootstrapServers;
    private String sslTruststorePassword;
    private String securityProtocol;
    private String saslMechanism;

    //Produce
    private String acks;
    private String retries;
    private String batchSize;
    private String bufferMemory;
    private String keySerializer;
    private String valueSerializer;
    private String msgCount;
    private String produceCount;
    private String msgSize;

    //Consume
    private String keyDeserializer;
    private String valueDeserializer;
    private String autoOffsetReset;
    private String enableAutoCommit;
    private String connectionsMaxIdleMs;
    private static String sslTruststoreLocation = null;
    private static String  javaSecurityAuthLoginConfig = null;

    static {
        setJavaSecurityAuthLoginConfig();
        setSslTruststoreLocation();
    }

    public void setBootstrapServers(String bootstrapServers) {
        this.bootstrapServers = bootstrapServers;
    }

    public void setSslTruststorePassword(String sslTruststorePassword) {
        this.sslTruststorePassword = sslTruststorePassword;
    }

    public void setSecurityProtocol(String securityProtocol) {
        this.securityProtocol = securityProtocol;
    }

    public void setSaslMechanism(String saslMechanism) {
        this.saslMechanism = saslMechanism;
    }

    public void setAcks(String acks) {
        this.acks = acks;
    }

    public void setRetries(String retries) {
        this.retries = retries;
    }

    public void setBatchSize(String batchSize) {
        this.batchSize = batchSize;
    }

    public void setBufferMemory(String bufferMemory) {
        this.bufferMemory = bufferMemory;
    }

    public void setKeySerializer(String keySerializer) {
        this.keySerializer = keySerializer;
    }

    public void setValueSerializer(String valueSerializer) {
        this.valueSerializer = valueSerializer;
    }

    public void setMsgCount(String msgCount) {
        this.msgCount = msgCount;
    }

    public void setProduceCount(String produceCount) {
        this.produceCount = produceCount;
    }

    public void setMsgSize(String msgSize) {
        this.msgSize = msgSize;
    }

    public void setKeyDeserializer(String keyDeserializer) {
        this.keyDeserializer = keyDeserializer;
    }

    public void setValueDeserializer(String valueDeserializer) {
        this.valueDeserializer = valueDeserializer;
    }

    public void setAutoOffsetReset(String autoOffsetReset) {
        this.autoOffsetReset = autoOffsetReset;
    }

    public void setEnableAutoCommit(String enableAutoCommit) {
        this.enableAutoCommit = enableAutoCommit;
    }

    public void setConnectionsMaxIdleMs(String connectionsMaxIdleMs) {
        this.connectionsMaxIdleMs = connectionsMaxIdleMs;
    }

    private static void setSslTruststoreLocation() {
        if(sslTruststoreLocation == null){
            try{
                sslTruststoreLocation = getTrustStorePath();
            }catch (Exception e){
                log.error("SslTruststoreLocation:" + e.getCause() + ":" + e.getMessage());
            }

        }
    }

    private static void  setJavaSecurityAuthLoginConfig() {
        if(javaSecurityAuthLoginConfig == null){
            try{
                System.setProperty("java.security.auth.login.config", getSaslConfig());
            }catch (Exception e){
                log.error("JavaSecurityAuthLoginConfig:" + e.getCause() + ":" + e.getMessage());
            }
        }
    }

    public Properties getProducerConfig(String topic) {
        Properties producerConfig = new Properties();
        producerConfig.setProperty("bootstrap.servers", bootstrapServers);
        producerConfig.setProperty("ssl.truststore.password", sslTruststorePassword);
        producerConfig.setProperty("acks", acks);
        producerConfig.setProperty("retries", retries);
        producerConfig.setProperty("batch.size", batchSize);
        producerConfig.setProperty("buffer.memory", bufferMemory);
        producerConfig.setProperty("key.serializer", keySerializer);
        producerConfig.setProperty("value.serializer", valueSerializer);
        producerConfig.setProperty("security.protocol", securityProtocol);
        producerConfig.setProperty("sasl.mechanism", saslMechanism);
        producerConfig.setProperty("msg.count", msgCount);
        producerConfig.setProperty("produce.count", produceCount);
        producerConfig.setProperty("msg.size", msgSize);
        producerConfig.put("ssl.truststore.location", sslTruststoreLocation);
        producerConfig.put("topic", topic);
        return producerConfig;
    }


    public Properties getConsumerConfig(String topic,String groupId) {
        Properties consumerConfig = new Properties();
        consumerConfig.setProperty("bootstrap.servers", bootstrapServers);
        consumerConfig.setProperty("ssl.truststore.password", sslTruststorePassword);
        consumerConfig.setProperty("security.protocol", securityProtocol);
        consumerConfig.setProperty("sasl.mechanism", saslMechanism);
        consumerConfig.setProperty("key.deserializer", keyDeserializer);
        consumerConfig.setProperty("value.deserializer", valueDeserializer);
        consumerConfig.setProperty("auto.offset.reset", autoOffsetReset);
        consumerConfig.setProperty("enable.auto.commit", enableAutoCommit);
        consumerConfig.setProperty("connections.max.idle.ms", connectionsMaxIdleMs);
        consumerConfig.put("ssl.truststore.location", sslTruststoreLocation);
        consumerConfig.put("topic", topic);
        consumerConfig.put("group.id", groupId);
        return consumerConfig;
    }

    private static String getSaslConfig() throws IOException
    {
        return getClassLoader().getResource(SASL_CONFIG).getPath();
//        return "D:\\Program Files\\apache-tomcat-8.5.32\\webapps\\ROOT\\WEB-INF\\classes\\dms_kafka_client_jaas.conf";
    }

    private static String getTrustStorePath() throws IOException
    {
        return getClassLoader().getResource(TRUSTSTORE_PATH).getPath();
//        return "D:\\Program Files\\apache-tomcat-8.5.32\\webapps\\ROOT\\WEB-INF\\classes\\client.truststore.jks";
    }

    private static ClassLoader getClassLoader()
    {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if (classLoader == null)
        {
            classLoader = BaseConfigBean.class.getClassLoader();
        }
        return classLoader;
    }
}
