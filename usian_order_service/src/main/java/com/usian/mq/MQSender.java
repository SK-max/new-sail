package com.usian.mq;

import com.usian.JsonUtils;
import com.usian.mapper.LocalMessageMapper;
import com.usian.pojo.LocalMessage;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate.ConfirmCallback;
import org.springframework.amqp.rabbit.core.RabbitTemplate.ReturnCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MQSender implements ReturnCallback, ConfirmCallback {

    @Autowired
    private LocalMessageMapper localMessageMapper;

    @Autowired
    private AmqpTemplate amqpTemplate;

    public void sendMsg(LocalMessage localMessage) {
        RabbitTemplate rabbitTemplate = (RabbitTemplate) this.amqpTemplate;
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setConfirmCallback(this); //确认回退
        rabbitTemplate.setReturnCallback(this);//失败回退

        CorrelationData correlationData = new CorrelationData(localMessage.getTxNo());
        rabbitTemplate.convertAndSend("order_exchange", "order_add",
                JsonUtils.objectToJson(localMessage), correlationData);
    }

    /**
     * 确认回调
     *
     * @param correlationData
     * @param ack
     * @param cause
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        String id = correlationData.getId();
        if (ack) {
            LocalMessage localMessage = localMessageMapper.selectByPrimaryKey(id);
            localMessage.setState(1);
            localMessageMapper.updateByPrimaryKey(localMessage);
        }
    }

    /**
     * 失败回调
     *
     * @param message
     * @param replyCode
     * @param replyText
     * @param exchange
     * @param routingKey
     */
    @Override
    public void returnedMessage(Message message, int replyCode, String replyText,
                                String exchange, String routingKey) {
        System.out.println("return--message" + new String(message.getBody()) + ",exchange:" + exchange + ",routingkey:" + routingKey);
    }
}
