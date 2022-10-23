package com.lrcmallbackend.util;

import com.lrcmallbackend.service.OrderService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrderListener {
    @Autowired
    private OrderService orderService;
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private WebSocketServer ws;

    @KafkaListener(topics = "orderRequest", groupId = "group_topic_test")
    public void orderRequestListener(ConsumerRecord<String, String> record) throws InterruptedException {
        String[] value = record.value().split(",");
        Thread.sleep(10000);
        String msg = orderService.addToOrder(Integer.parseInt(value[0]),Integer.parseInt(value[1]),Integer.parseInt(value[2]));
        kafkaTemplate.send("orderReply",  "key", msg+","+value[2]);
    }

    @KafkaListener(topics = "orderReply", groupId = "group_topic_test")
    public void orderReplyListener(ConsumerRecord<String, String> record) {
        String[] value = record.value().split(",");
        System.out.println(value);
        ws.sendMessageToUser(value[1],value[0]);
    }

}
