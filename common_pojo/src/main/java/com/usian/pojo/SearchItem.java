package com.usian.pojo;

import java.io.Serializable;

public class SearchItem implements Serializable {

    private  String id;
    private String item_title;
    private String item_sell_title;
    private String item_price;
    private String item_image;
    private String item_category_name;
    private String item_desc;

    public SearchItem() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getItem_title() {
        return item_title;
    }

    public void setItem_title(String item_title) {
        this.item_title = item_title;
    }

    public String getItem_sell_title() {
        return item_sell_title;
    }

    public void setItem_sell_title(String item_sell_title) {
        this.item_sell_title = item_sell_title;
    }

    public String getItem_price() {
        return item_price;
    }

    public void setItem_price(String item_price) {
        this.item_price = item_price;
    }

    public String getItem_image() {
        return item_image;
    }

    public void setItem_image(String item_image) {
        this.item_image = item_image;
    }

    public String getItem_category_name() {
        return item_category_name;
    }

    public void setItem_category_name(String item_category_name) {
        this.item_category_name = item_category_name;
    }

    public String getItem_desc() {
        return item_desc;
    }

    public void setItem_desc(String item_desc) {
        this.item_desc = item_desc;
    }

    public SearchItem(String id, String item_title, String item_sell_title, String item_price, String item_image, String item_category_name, String item_desc) {
        this.id = id;
        this.item_title = item_title;
        this.item_sell_title = item_sell_title;
        this.item_price = item_price;
        this.item_image = item_image;
        this.item_category_name = item_category_name;
        this.item_desc = item_desc;
    }

    @Override
    public String toString() {
        return "SearchItem{" +
                "id='" + id + '\'' +
                ", item_title='" + item_title + '\'' +
                ", item_sell_title='" + item_sell_title + '\'' +
                ", item_price='" + item_price + '\'' +
                ", item_image='" + item_image + '\'' +
                ", item_category_name='" + item_category_name + '\'' +
                ", item_desc='" + item_desc + '\'' +
                '}';
    }
}
