package com.usian.config;


import com.usian.service.ItemService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ItemMQListener {

    @Autowired
    private ItemService itemService;

    /**
     * 监听者接收三要素
     * queue
     * exchange
     * routingkey
     */
    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = "item_queue", durable = "true"),
            exchange = @Exchange(value = "order_exchange", type = ExchangeTypes.TOPIC), key = {"order.*"}))
    public void listen(String orderId) {
        System.out.println("接收到消息" + orderId);
        Integer result = itemService.updateTbItemByOrderId(orderId);
        if (result > 0) {
            throw new RuntimeException("扣减库存失败");
        }

    }

}
