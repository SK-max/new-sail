package com.usian.controller;


import com.alibaba.druid.support.json.JSONUtils;
import com.usian.JsonUtils;
import com.usian.pojo.OrderInfo;
import com.usian.pojo.TbOrder;
import com.usian.pojo.TbOrderItem;
import com.usian.pojo.TbOrderShipping;
import com.usian.service.OrderService;
import org.omg.CORBA.PRIVATE_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.transform.Result;
import java.util.List;

@RestController
@RequestMapping("/service/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @RequestMapping("/insertOrder")
    public Long insertOrder(@RequestBody OrderInfo orderInfo){

        return orderService.insertOrder(orderInfo);
    };

}
