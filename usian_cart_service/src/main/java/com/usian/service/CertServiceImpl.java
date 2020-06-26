package com.usian.service;

import com.usian.pojo.TbItem;
import com.usian.redis.RedisClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;


@Service
@Transactional
public class CertServiceImpl implements CartService {


    @Autowired
    private RedisClient redisClient;

    @Value("${CART_REDIS_KEY}")
    private String CART_REDIS_KEY;

    /**
     * 查询并返回购物车
     * @param userId
     * @return
     */
    @Override
    public Map<String, TbItem> selectCartByUserId(String userId) {
        return (Map<String, TbItem>) redisClient.hget(CART_REDIS_KEY, userId);
    }

    /**
     * 将购物车缓存到Redis中
     * @param userId
     * @param cart
     * @return
     */
    @Override
    public Boolean addCartToRedis(String userId, Map<String, TbItem> cart) {
        return redisClient.hset(CART_REDIS_KEY, userId, cart);
    }
}
