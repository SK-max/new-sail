package com.usian.service;

import com.sun.org.apache.bcel.internal.generic.NEW;
import com.usian.JsonUtils;
import com.usian.mapper.*;
import com.usian.mq.MQSender;
import com.usian.pojo.*;
import com.usian.redis.RedisClient;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {


    @Value("${ORDER_ID_KEY}")
    private String ORDER_ID_KEY;

    @Value("${ORDER_ID_BEGIN}")
    private Integer ORDER_ID_BEGIN;

    @Value("${ORDER_ITEM_ID_KEY}")
    private String ORDER_ITEM_ID_KEY;

    @Autowired
    private RedisClient redisClient;

    @Autowired
    private TbOrderMapper tbOrderMapper;

    @Autowired
    private TbOrderItemMapper tbOrderItemMapper;

    @Autowired
    private TbOrderShippingMapper tbOrderShippingMapper;

    @Autowired
    private TbItemMapper tbItemMapper;

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private MQSender mqSender;

    @Autowired
    private LocalMessageMapper localMessageMapper;

    @Override
    public Long insertOrder(OrderInfo orderInfo) {

        //解析OrderInfo
        String orderItem = orderInfo.getOrderItem();
        TbOrder order = orderInfo.getOrder();
        TbOrderShipping tbOrderShipping = orderInfo.getTbOrderShipping();
        List<TbOrderItem> tbOrderItems = JsonUtils.jsonToList(orderItem, TbOrderItem.class);
        //2 保存订单信息
        if (!redisClient.exists(ORDER_ID_KEY)) {
            redisClient.set(ORDER_ITEM_ID_KEY, ORDER_ID_BEGIN);
        }
        Long orderId = redisClient.incr(ORDER_ID_KEY, 1L);
        order.setOrderId(orderId.toString());
        Date date = new Date();
        order.setCreateTime(date);
        order.setUpdateTime(date);
        //1未付款 2已付款 3未发货 4已发货 5交易成功 6交易关闭
        order.setStatus(1);
        tbOrderMapper.insertSelective(order);

        //3保存明细信息
        if (!redisClient.exists(ORDER_ITEM_ID_KEY)) {
            redisClient.set(ORDER_ITEM_ID_KEY, 0);
        }
        for (int i = 0; i < tbOrderItems.size(); i++) {
            Long orderItemId = redisClient.incr(ORDER_ITEM_ID_KEY, 1L);
            TbOrderItem tbOrderItem = tbOrderItems.get(i);
            tbOrderItem.setId(orderItemId.toString());
            tbOrderItem.setOrderId(orderId.toString());
            tbOrderItemMapper.insertSelective(tbOrderItem);
        }
        tbOrderShipping.setOrderId(orderId.toString());
        tbOrderShipping.setCreated(new Date());
        tbOrderShipping.setUpdated(new Date());
        tbOrderShippingMapper.insertSelective(tbOrderShipping);
        amqpTemplate.convertAndSend("order_exchange", "order.add", orderId);

        LocalMessage localMessage = new LocalMessage();
        localMessage.setTxNo(UUID.randomUUID().toString());
        localMessage.setOrderNo(orderId.toString());
        localMessage.setState(0);
        localMessageMapper.insertSelective(localMessage);

            //发布消息到库存 完成扣减库存
        mqSender.sendMsg(localMessage);
        return orderId;
    }

    //查询所有超时订单
    @Override
    public List<TbOrder> selectOverTimeTbOrder() {
        return tbOrderMapper.selectOvertimeOrder();
    }

    //关闭超时订单
    @Override
    public void updateOverTimeTbOrder(TbOrder tbOrder) {
        tbOrder.setStatus(6);
        Date date = new Date();
        tbOrder.setCreateTime(date);
        tbOrder.setEndTime(date);
        tbOrder.setUpdateTime(date);
        tbOrderMapper.updateByPrimaryKeySelective(tbOrder);
    }

    /**
     * 将超时订单商品加回库存
     *
     * @param orderId
     */
    @Override
    public void updateTbItemByOrderId(String orderId) {
        TbOrderItemExample example = new TbOrderItemExample();
        TbOrderItemExample.Criteria criteria = example.createCriteria();
        criteria.andOrderIdEqualTo(orderId);
        List<TbOrderItem> orderItemList = tbOrderItemMapper.selectByExample(example);
        for (TbOrderItem tbOrderItem : orderItemList) {
            TbItem tbItem = tbItemMapper.selectByPrimaryKey(Long.valueOf(orderId));
            tbItem.setNum(tbItem.getNum()+tbOrderItem.getNum());
            tbItem.setUpdated(new Date());
            tbItemMapper.updateByPrimaryKey(tbItem);
        }
    }

}
