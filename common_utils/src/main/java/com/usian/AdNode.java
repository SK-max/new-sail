package com.usian;


import java.io.Serializable;

public class AdNode implements Serializable {


    private String SrcB;

    private String Src;

    private Integer height;

    private Integer heightB;

    private Integer width;

    private Integer widthB;

    private String href;

    private String alt;


    public AdNode() {
    }

    public AdNode(String srcB, String src, Integer height, Integer heightB, Integer width, Integer widthB, String href, String alt) {
        SrcB = srcB;
        Src = src;
        this.height = height;
        this.heightB = heightB;
        this.width = width;
        this.widthB = widthB;
        this.href = href;
        this.alt = alt;
    }

    public String getSrcB() {
        return SrcB;
    }

    public void setSrcB(String srcB) {
        SrcB = srcB;
    }

    public String getSrc() {
        return Src;
    }

    public void setSrc(String src) {
        Src = src;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getHeightB() {
        return heightB;
    }

    public void setHeightB(Integer heightB) {
        this.heightB = heightB;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getWidthB() {
        return widthB;
    }

    public void setWidthB(Integer widthB) {
        this.widthB = widthB;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getAlt() {
        return alt;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

    @Override
    public String toString() {
        return "AdNode{" +
                "SrcB='" + SrcB + '\'' +
                ", Src='" + Src + '\'' +
                ", height=" + height +
                ", heightB=" + heightB +
                ", width=" + width +
                ", widthB=" + widthB +
                ", href='" + href + '\'' +
                ", alt='" + alt + '\'' +
                '}';
    }
}
