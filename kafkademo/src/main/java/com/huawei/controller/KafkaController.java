package com.huawei.controller;

import com.huawei.service.ConsumerService;
import com.huawei.service.ProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
public class KafkaController {

    @Autowired
    private ProducerService producerService;

    @Autowired
    private ConsumerService consumerService;

    @RequestMapping(value="v1/rest/kafkaProduce", method = RequestMethod.POST)
    @ResponseBody
    public String kafkaProduce(@RequestBody Map<String, Object> param){
        return producerService.kafkaProduce(param);
    }

    @RequestMapping(value="v1/rest/kafkaConsumer", method = RequestMethod.POST)
    @ResponseBody
    public String kafkaConsumer(@RequestBody Map<String, Object> param){
        return consumerService.kafkaConsumer(param);
    }

    @RequestMapping(value="v1/rest/KafkaConsumer", method = RequestMethod.GET)
    @ResponseBody
    public String reset(){
        return "";
    }


}
