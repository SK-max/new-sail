package com.usian.fallcack;


import com.usian.CatResult;
import com.usian.PageResult;
import com.usian.feign.ItemServiceFeign;
import com.usian.pojo.*;
import feign.hystrix.FallbackFactory;

import java.util.List;

public class ItemServiceFallback implements FallbackFactory<ItemServiceFeign> {

    @Override
    public ItemServiceFeign create(Throwable throwable) {

        return new ItemServiceFeign(){

            @Override
            public TbItem selectItemInfo(Long itemId) {
                return null;
            }

            @Override
            public PageResult selectTbItemAllByPage(Integer page, Integer rows) {
                return null;
            }

            @Override
            public List<TbItemCat> selectItemCategoryByParentId(Long id) {
                return null;
            }

            @Override
            public TbItemParam selectItemParamByItemCatId(Long itemCatId) {
                return null;
            }

            @Override
            public void deleteItemById(Long itemId) {

            }

            @Override
            public PreItemVo preUpdateItem(Long itemId) {
                return null;
            }

            @Override
            public void updateTbItem(TbItem tbItem) {

            }

            @Override
            public void insertTbItem(TbItem tbItem, String desc, String itemParams) {

            }

            @Override
            public void updateTbItem(TbItem tbItem, String desc, String itemParams) {

            }

            @Override
            public PageResult selectItemParamAll() {
                return null;
            }

            @Override
            public void deleteItemParamById(Long id) {

            }

            @Override
            public Integer insertItemParam(Long itemCatId, String paramData) {
                return null;
            }

            @Override
            public CatResult selectItemCategoryAll() {
                return null;
            }

            @Override
            public TbItemDesc selectItemDescByItemId(Long itemId) {
                return null;
            }

            @Override
            public TbItemParamItem selectTbItemParamItemByItemId(Long itemId) {
                return null;
            }
        };
    }
}