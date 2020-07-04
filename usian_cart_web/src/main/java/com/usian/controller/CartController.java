package com.usian.controller;


import com.usian.CookieUtils;
import com.usian.JsonUtils;
import com.usian.Result;
import com.usian.feign.CartFeign;
import com.usian.feign.ItemServiceFeign;
import com.usian.pojo.TbItem;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@RestController
@RequestMapping("/frontend/cart")
public class CartController {

    @Autowired
    private ItemServiceFeign itemServiceFeign;

    @Autowired
    private CartFeign cartFeign;

    @Value("${CART_COOKIE_KEY}")
    private String CART_COOKIE_KEY;

    @Value("${CART_COOKIE_EXPIRE}")
    private Integer CART_COOKIE_EXPIRE;

    @RequestMapping("/addItem")
    public Result addItem(Long itemId, String userId,
                          @RequestParam(defaultValue = "1") Integer num,
                          HttpServletRequest request,
                          HttpServletResponse response) {
        try {
            if (StringUtils.isBlank(userId)) {
                //1 未登录状态
                Map<String, TbItem> cart = getCartFromCookie(request);
                //2 添加商品到购物车
                addItemTocart(cart, itemId, num);
                //3将购物车存入cookie响应客户端
                addClientCookie(request, response, cart);
            } else {
                //已登录
                Map<String, TbItem> cart = getCartFromRedis(userId);
                addCartToRedis(userId, cart);
            }
            return Result.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("添加失败");
        }
    }

    /**
     * 查看购物车
     *
     * @param userId
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/showCart")
    public Result showCart(String userId, HttpServletRequest request, HttpServletResponse response) {
        try {
            List<TbItem> tblist = new ArrayList<>();
            if (StringUtils.isBlank(userId)) {
                Map<String, TbItem> cartMap = getCartFromCookie(request);
                //如果userId为空 则为未登录状态
                Set<String> keySet = cartMap.keySet();
                if (keySet != null && keySet.size() > 0) {
                    for (String key : keySet) {
                        TbItem tbItem = cartMap.get(key);
                        tblist.add(tbItem);
                    }
                } else {
                    //如果不为空则为登录状态
                    Map<String, TbItem> cart = getCartFromRedis(userId);
                    Set<String> set = cart.keySet();
                            for (String  key : set) {
                                TbItem tbItem = cart.get(key);
                                tblist.add(tbItem);
                            }
                }
            }
            return Result.ok(tblist);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("查看错误");
        }

    }

    @RequestMapping("/updateItemNum")
    public Result updateItemNum(Integer num, String userId, Long itemId,
                                HttpServletRequest request,
                                HttpServletResponse response) {
        try {
            if (StringUtils.isBlank(userId)) {
                //未登录状态
                Map<String, TbItem> cart = getCartFromCookie(request);
                TbItem tbItem = cart.get(itemId.toString());
                System.out.println(num + "哈哈哈哈哈");
                tbItem.setNum(num);
                cart.put(itemId.toString(), tbItem);
                addClientCookie(request, response, cart);
            } else {
                //登录状态
                Map<String, TbItem> cart = getCartFromRedis(userId);
                TbItem tbItem = cart.get(itemId);
                tbItem.setNum(num);
                cart.put(itemId.toString(), tbItem);
                addCartToRedis(userId, cart);
            }
            return Result.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("修改失败");
        }
    }

    @RequestMapping("/deleteItemFromCart")
    public Result deleteItemFromCart(String userId, Long itemId, HttpServletRequest request, HttpServletResponse response) {
        try {
            if (StringUtils.isBlank(userId)) {
                //用户未登录状态
                Map<String, TbItem> cart = getCartFromCookie(request);
                cart.remove(itemId.toString());
                addClientCookie(request, response, cart);

            } else {
                //用户已登录状态
                Map<String, TbItem> cart = getCartFromRedis(userId);
                cart.remove(itemId);
                addCartToRedis(userId, cart);


            }
            return Result.ok();
        } catch (Exception e) {

            e.printStackTrace();
            return Result.error("删除出错");
        }
    }


    /**
     * 私有方法 从cookie中获取 购物车
     *
     * @param request
     * @return
     */
    private Map<String, TbItem> getCartFromCookie(HttpServletRequest request) {
        String cartJson = CookieUtils.getCookieValue(request, CART_COOKIE_KEY, true);
        // 如果json串不为空
        if (StringUtils.isNotBlank(cartJson)) {
            Map<String, TbItem> map = JsonUtils.jsonToMap(cartJson, TbItem.class);
            return map;
        }
        //返回空购物车对象
        return new HashMap<String, TbItem>();
    }

    /**
     * 私有方法将商品添加进购物车
     *
     * @param cart
     * @param itemId
     * @param num
     */
    private void addItemTocart(Map<String, TbItem> cart, Long itemId, Integer num) {
        TbItem tbItem = cart.get(itemId.toString());
        if (tbItem != null) {
            tbItem.setNum(num);
        } else {
            tbItem = itemServiceFeign.selectItemInfo(itemId);
            tbItem.setNum(num);
        }
        cart.put(itemId.toString(), tbItem);
    }

    /**
     * 私有方法 将购物车通过cookie返回客户端
     *
     * @param request
     * @param response
     * @param cart
     */
    private void addClientCookie(HttpServletRequest request, HttpServletResponse response, Map<String, TbItem> cart) {
        String cartJson = JsonUtils.objectToJson(cart);
        CookieUtils.setCookie(request, response, CART_COOKIE_KEY, cartJson, CART_COOKIE_EXPIRE, true);
    }

    /**
     * 私有方法 获取从Redis中获取Cart
     *
     * @param userId
     * @return
     */
    private Map<String, TbItem> getCartFromRedis(String userId) {
        Map<String, TbItem> cart = cartFeign.selectCartByUserId(userId);
        if (cart != null && cart.size() > 0) {
            return cart;
        }
        return new HashMap<String, TbItem>();
    }

    private Boolean addCartToRedis(String userId, Map<String, TbItem> cart) {
        return cartFeign.addCartToRedis(userId, cart);
    }


}
