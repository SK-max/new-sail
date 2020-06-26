package com.usian.pojo;

import java.io.Serializable;

public class OrderInfo implements Serializable {

    private TbOrder order;

    private TbOrderShipping tbOrderShipping;

    private String orderItem;


    public OrderInfo() {
    }

    public OrderInfo(TbOrderShipping tbOrderShipping, String orderItem) {
        this.tbOrderShipping = tbOrderShipping;
        this.orderItem = orderItem;
    }

    public TbOrder getOrder() {
        return order;
    }

    public void setOrder(TbOrder order) {
        this.order = order;
    }

    public TbOrderShipping getTbOrderShipping() {
        return tbOrderShipping;
    }

    public void setTbOrderShipping(TbOrderShipping tbOrderShipping) {
        this.tbOrderShipping = tbOrderShipping;
    }

    public String getOrderItem() {
        return orderItem;
    }

    public void setOrderItem(String orderItem) {
        this.orderItem = orderItem;
    }

    @Override
    public String toString() {
        return "OrderInfo{" +
                "order=" + order +
                ", tbOrderShipping=" + tbOrderShipping +
                ", orderItem='" + orderItem + '\'' +
                '}';
    }
}
