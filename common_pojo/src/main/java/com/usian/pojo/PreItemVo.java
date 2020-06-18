package com.usian.pojo;

public class PreItemVo  {

    private String itemCat;

    private String itemDesc;

    private TbItem item;

    private String itemParamItem = "哈哈哈";

    public PreItemVo() {
    }

    public PreItemVo(String itemCat, String itemDesc, TbItem item, String itemParamItem) {
        this.itemCat = itemCat;
        this.itemDesc = itemDesc;
        this.item = item;
        this.itemParamItem = itemParamItem;
    }

    public String getItemCat() {
        return itemCat;
    }

    public void setItemCat(String itemCat) {
        this.itemCat = itemCat;
    }

    public String getItemDesc() {
        return itemDesc;
    }

    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc;
    }

    public TbItem getItem() {
        return item;
    }

    public void setItem(TbItem item) {
        this.item = item;
    }

    public String getItemParamItem() {
        return itemParamItem;
    }

    public void setItemParamItem(String itemParamItem) {
        this.itemParamItem = itemParamItem;
    }

    @Override
    public String toString() {
        return "PreItemVo{" +
                "itemCat='" + itemCat + '\'' +
                ", itemDesc='" + itemDesc + '\'' +
                ", item=" + item +
                ", itemParamItem='" + itemParamItem + '\'' +
                '}';
    }
}
