package com.usian.feign;


import com.usian.pojo.OrderInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("usian-order-service")
public interface OrderServiceFeign {

    /**
     * 添加商品
     * @param orderInfo
     * @return
     */
    @RequestMapping("/service/order/insertOrder")
    public Long insertOrder(@RequestBody OrderInfo orderInfo);
}
