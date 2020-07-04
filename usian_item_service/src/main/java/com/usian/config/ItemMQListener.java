package com.usian.config;


import com.usian.JsonUtils;
import com.usian.pojo.DeDuplication;
import com.usian.pojo.LocalMessage;
import com.usian.service.DeDuplicationService;
import com.usian.service.ItemService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.Date;

@Component
public class ItemMQListener {

    @Autowired
    private ItemService itemService;

    @Autowired
    private DeDuplicationService duplicationService;

    /**
     * 监听者接收三要素
     * queue
     * exchange
     * routingkey
     */
    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = "item_queue", durable = "true"),
            exchange = @Exchange(value = "order_exchange", type = ExchangeTypes.TOPIC), key = {"order.*"}))
    public void listen(String msg, Channel channel, Message message) throws IOException {
        LocalMessage localMessage = JsonUtils.jsonToPojo(msg, LocalMessage.class);
        String txNo = localMessage.getTxNo();
        //进行幂等性判断防止因为网络问题MQ没有收到ACK确认导致重发消息操作
        DeDuplication deDuplication = duplicationService.selectItemDuplicationByTxNo(txNo);
        if(deDuplication==null){
            itemService.updateTbItemByOrderId(localMessage.getOrderNo());
            deDuplication = new DeDuplication();
            deDuplication.setTxNo(txNo);
            deDuplication.setCreateTime(new Date());
            duplicationService.insertDeDuplication(deDuplication);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        }else {
            System.out.println("=======幂等生效：事务"+deDuplication.getTxNo()+"已成功执行======");
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        }
    }
}
