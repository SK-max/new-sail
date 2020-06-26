package com.usian.service;

import com.usian.pojo.TbItem;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@Transactional
public interface CartService {


    Map<String, TbItem> selectCartByUserId(String userId);

    Boolean addCartToRedis(String userId, Map<String, TbItem> cart);
}
