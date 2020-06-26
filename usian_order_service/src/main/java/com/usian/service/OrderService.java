package com.usian.service;

import com.usian.pojo.OrderInfo;
import com.usian.pojo.TbOrder;
import org.springframework.core.annotation.Order;

import java.util.List;

public interface OrderService {


    Long insertOrder(OrderInfo orderInfo);

    List<TbOrder> selectOverTimeTbOrder();
    //关闭超时订单
    void updateOverTimeTbOrder(TbOrder tbOrder);
    //将超时订单 商品加回库存
    void updateTbItemByOrderId(String orderId);
}
