package com.fairrummy.mq.consumer;

import com.fairrummy.mapper.PCObjectMapper;
import com.fairrummy.utility.TableInfo;
import com.fairrummy.utility.TableInfoCache;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageConsumer {

    /*@Autowired
    private PCObjectMapper objectMapper;

    @RabbitListener(queues = "gcs")
    public void receiveMessage(String message) {
        System.out.println("Vibha Received message: " + message);
        // Process the message here

        TableInfo tableInfo = objectMapper.readValue(message, TableInfo.class);

        System.out.println("Vibha Typecasted : " + tableInfo);

        TableInfoCache.putTableInfo(tableInfo.getTemplateId(), tableInfo);
    }*/
}