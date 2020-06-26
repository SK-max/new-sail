package com.usian.controller;


import com.usian.pojo.TbItem;
import com.usian.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/service/cart")
public class CartServiceController {

    @Autowired
    private CartService cartService;

    @RequestMapping("/selectCartByUserId")
    public Map<String, TbItem> selectCartByUserId(@RequestParam String userId) {
        return cartService.selectCartByUserId(userId);
    }

    @RequestMapping("/addCartToRedis")
     public Boolean addCartToRedis(String userId, Map<String, TbItem> cart){
        return cartService.addCartToRedis(userId,cart);
    };


}
