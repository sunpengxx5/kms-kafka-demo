package com.huawei.service;

import com.alibaba.fastjson.JSONObject;
import com.huawei.configbean.BaseConfigBean;
import com.huawei.utils.CommonUtils;
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("consumerService")
public class ConsumerService {

    private static Logger log = Logger.getLogger(ConsumerService.class);

    @Autowired
    private BaseConfigBean baseConfigBean;

    private HashMap<String,HashMap<String,KafkaConsumer<String, String>>> consumerHashMap = null;

    private void init() {
        if(consumerHashMap == null){
            initConfig();
        }
    }

    private synchronized void initConfig(){
        if(consumerHashMap == null){
            consumerHashMap = new HashMap<>();
        }
    }

    private KafkaConsumer<String, String> getKafkaConsumer(String topic,String groupId){
        if(isExists(topic,groupId)){
            return consumerHashMap.get(topic).get(groupId);
        }else{
            return createKafkaConsumer(topic,groupId);
        }
    }

    private synchronized KafkaConsumer<String, String> createKafkaConsumer(String topic,String groupId){
        if(isExists(topic,groupId)){
            return consumerHashMap.get(topic).get(groupId);
        }else{
            Properties consumerConfig = baseConfigBean.getConsumerConfig(topic,groupId);

            KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(consumerConfig);
            kafkaConsumer.subscribe(Arrays.asList(consumerConfig.getProperty("topic")),new ConsumerListener());

            if(consumerHashMap.containsKey(topic)){
                consumerHashMap.get(topic).put(groupId,kafkaConsumer);
            }else {
                HashMap<String, KafkaConsumer<String, String>> hashMap = new HashMap<>();
                hashMap.put(groupId, kafkaConsumer);
                consumerHashMap.put(topic, hashMap);
            }
            return kafkaConsumer;
        }
    }



    private boolean isExists(String topic,String groupId){
        if(consumerHashMap != null){
            if(consumerHashMap.containsKey(topic)&&consumerHashMap.get(topic).containsKey(groupId)){
                return true;
            }else {
                return false;
            }
        }else {
            return false;
        }
    }


    private class ConsumerListener implements ConsumerRebalanceListener{

        @Override
        public void onPartitionsRevoked(Collection<TopicPartition> collection) {

        }

        @Override
        public void onPartitionsAssigned(Collection<TopicPartition> collection) {

        }
    }

    public String kafkaConsumer(Map<String, Object> param){
        init();
        JSONObject resultJson = new JSONObject();
        try {
            int pollCount = consumeMsg(param);
            resultJson.put("errCode",CommonUtils.NOMAL_CODE);
            resultJson.put("pollCount",pollCount);
            resultJson.put("msg",CommonUtils.SUCCESS);
        }catch (Exception e){
            resultJson.put("errCode",CommonUtils.ERROR_CODE);
            String errMsg = e.toString() + ":" +e.getMessage();
            resultJson.put("msg",errMsg);
            log.error("msg:" + errMsg);
        }
        return resultJson.toJSONString();
    }

    private int consumeMsg(Map<String, Object> param){
        String topic = param.get("topic").toString();
        String groupId = param.get("groupId").toString();
        int timeout = Integer.parseInt(param.get("timeout").toString());

        int pollCount;

        KafkaConsumer<String, String> kafkaConsumer = getKafkaConsumer(topic,groupId);
        ConsumerRecords<String, String> records = kafkaConsumer.poll(timeout);

        pollCount = records.count();
        log.info("Records count " + pollCount);
        kafkaConsumer.commitSync();

        return pollCount;
    }
}
