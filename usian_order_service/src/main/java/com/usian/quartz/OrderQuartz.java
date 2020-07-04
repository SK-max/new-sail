package com.usian.quartz;

import com.usian.mapper.LocalMessageMapper;
import com.usian.mq.MQSender;
import com.usian.pojo.LocalMessage;
import com.usian.pojo.LocalMessageExample;
import com.usian.pojo.TbOrder;
import com.usian.redis.RedisClient;
import com.usian.service.OrderService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

public class OrderQuartz implements Job {

    @Autowired
    private OrderService orderService;

    @Autowired
    private RedisClient redisClient;

    @Autowired
    private LocalMessageMapper localMessageMapper;

    @Autowired
    private MQSender mqSender;


    /**
     * 关闭超时订单
     *
     * @param jobExecutionContext
     * @throws JobExecutionException
     */
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        String ip = null;
        try {
            ip = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        if(redisClient.setNx("SETNX_LOCK_ORDER_KEY",ip,30)) {

            System.out.println("执行关闭超时订单任务"+new Date());
            //查询超时订单
            List<TbOrder> orderList = orderService.selectOverTimeTbOrder();
            for (TbOrder tbOrder : orderList) {
                //关闭超时订单
                orderService.updateOverTimeTbOrder(tbOrder);
                //将超时订单中的商品库存数量加回去
                orderService.updateTbItemByOrderId(tbOrder.getOrderId());
            }
            System.out.println("执行检查本地消息的任务"+new Date());
            LocalMessageExample example = new LocalMessageExample();
            LocalMessageExample.Criteria criteria = example.createCriteria();
            criteria.andStateEqualTo(0);
            List<LocalMessage> localMessageList = localMessageMapper.selectByExample(example);
            for (LocalMessage  localMessage : localMessageList) {
                mqSender.sendMsg(localMessage);
            }
            redisClient.del("SETNX_LOCK_ORDER_KEY");
        }else {

            System.out.println("----機器:"+ip+"占用分佈式鎖，任務正在執行");
        }
    }

}
