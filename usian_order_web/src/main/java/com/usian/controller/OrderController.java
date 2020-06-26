package com.usian.controller;


import com.usian.JsonUtils;
import com.usian.Result;
import com.usian.feign.CartFeign;
import com.usian.feign.OrderServiceFeign;
import com.usian.pojo.*;
import com.usian.redis.RedisClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/frontend/order")
public class OrderController {

    @Autowired
    private OrderServiceFeign orderServiceFeign;

    @Autowired
    private CartFeign cartFeign;


    @RequestMapping("/insertOrder")
    public Result insertOrder(String orderItem, TbOrder tbOrder, TbOrderShipping tbOrderShipping) {
        //因为一个request中只包含一个requestbody ，所以feign不支持多个RequestBody
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setOrderItem(orderItem);
        orderInfo.setOrder(tbOrder);
        orderInfo.setTbOrderShipping(tbOrderShipping);
        Long orderId = orderServiceFeign.insertOrder(orderInfo);
        if (orderId != null) {
            //删除购物车
            return Result.ok(orderId);
        }
        return Result.error("error");
    }


    /**
     * 去结算展示订单页面
     *
     * @param ids
     * @param userId
     * @return
     */
    public Result goSettlement(String[] ids, String userId) {
        try {
            Map<String, TbItem> cart = cartFeign.selectCartByUserId(userId);
            List<TbItem> list = new ArrayList<TbItem>();
            for (String id : ids) {
                list.add(cart.get(id));
            }
            if (list.size() > 0) {
                return Result.ok(list);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.error("结账失败");
    }


}
