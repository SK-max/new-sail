package com.usian.quartz;

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
import java.util.List;

public class OrderQuartz implements Job {

    @Autowired
    private OrderService orderService;

    @Autowired
    private RedisClient redisClient;


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
            //查询超时订单
            List<TbOrder> orderList = orderService.selectOverTimeTbOrder();
            for (TbOrder tbOrder : orderList) {
                //关闭超时订单
                orderService.updateOverTimeTbOrder(tbOrder);
                //将超时订单中的商品库存数量加回去
                orderService.updateTbItemByOrderId(tbOrder.getOrderId());
            }
            redisClient.del("SETNX_LOCK_ORDER_KEY");
        }else {
            System.out.println("----機器:"+ip+"占用分佈式鎖，任務正在執行");
        }
    }


}
