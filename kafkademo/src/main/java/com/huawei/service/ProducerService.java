package com.huawei.service;

import com.alibaba.fastjson.JSONObject;
import com.huawei.configbean.BaseConfigBean;
import com.huawei.utils.CommonUtils;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Future;

@Service("producerService")
public class ProducerService {

    private static Logger log = Logger.getLogger(ProducerService.class);

    @Autowired
    private BaseConfigBean baseConfigBean;

    HashMap<String,Producer<String, String>> producerHashMap;

    private void init() {
        if(producerHashMap == null){
            initConfig();
        }
    }
    private Producer<String, String> getKafkaProducer(String topic){
        if(isExists(topic)){
            return producerHashMap.get(topic);
        }else{
            return createKafkaProducer(topic);
        }
    }

    private synchronized Producer<String, String> createKafkaProducer(String topic){
        if(isExists(topic)){
            return producerHashMap.get(topic);
        }else{
            Properties producerConfig = baseConfigBean.getProducerConfig(topic);

            Producer<String, String> producer = new KafkaProducer<>(producerConfig);

            producerHashMap.put(topic,producer);

            return producer;
        }
    }

    private boolean isExists(String topic){
        if(producerHashMap != null){
            if(producerHashMap.containsKey(topic)){
                return true;
            }else {
                return false;
            }
        }else {
            return false;
        }
    }

    private synchronized void initConfig(){
        if(producerHashMap == null){
            producerHashMap = new HashMap<>();
        }
    }


    public String kafkaProduce(Map<String, Object> param){
        init();
        JSONObject resultJson = new JSONObject();
        try {
            produceMsg(param);
            resultJson.put("errCode",CommonUtils.NOMAL_CODE);
            resultJson.put("msg",CommonUtils.SUCCESS);
        }catch (Exception e){
            resultJson.put("errCode",CommonUtils.ERROR_CODE);
            String errMsg = e.toString() + ":" +e.getMessage();
            resultJson.put("msg",errMsg);
            log.error("msg" + errMsg);
        }
        return resultJson.toJSONString();
    }

    private void produceMsg(Map<String, Object> param) throws Exception{

        String topic = param.get("topic").toString();
        int msgCount = Integer.parseInt(param.get("msgCount").toString());
        String msg = param.get("msg").toString();

        Producer<String, String> producer = getKafkaProducer(topic);

        for (int i = 0; i < msgCount; i++){
            Future<RecordMetadata> future =
                    producer.send(new ProducerRecord<String, String>(topic,null, msg));
            RecordMetadata rm;
            rm = future.get();
            log.info("Succeed to send msg: " + rm.offset());
        }
    }
}
