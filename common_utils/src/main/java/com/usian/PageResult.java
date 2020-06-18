package com.usian;

import java.io.Serializable;
import java.util.List;

public class PageResult implements Serializable {


    private Integer pageIndex;

    private Long totaPage;

    private List result;

    public PageResult() {
    }

    public PageResult(Integer pageIndex, Long totaPage, List result) {
        this.pageIndex = pageIndex;
        this.totaPage = totaPage;
        this.result = result;
    }

    public Integer getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }

    public Long getTotaPage() {
        return totaPage;
    }

    public void setTotaPage(Long totaPage) {
        this.totaPage = totaPage;
    }

    public List getResult() {
        return result;
    }

    public void setResult(List result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "PageResult{" +
                "pageIndex=" + pageIndex +
                ", totaPage=" + totaPage +
                ", result=" + result +
                '}';
    }
}
