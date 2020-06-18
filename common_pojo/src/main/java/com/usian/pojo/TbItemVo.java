package com.usian.pojo;


public class TbItemVo extends TbItem {

    private String desc;

    private String itemParams;

    public TbItemVo() {
    }

    public TbItemVo(String desc, String itemParams) {
        this.desc = desc;
        this.itemParams = itemParams;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getItemParams() {
        return itemParams;
    }

    public void setItemParams(String itemParams) {
        this.itemParams = itemParams;
    }

    @Override
    public String toString() {
        return "TbItemVo{" +
                "desc='" + desc + '\'' +
                ", itemParams='" + itemParams + '\'' +
                '}';
    }


}
