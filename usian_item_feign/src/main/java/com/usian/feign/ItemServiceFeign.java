package com.usian.feign;


import com.usian.PageResult;
import com.usian.pojo.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "usian-item-service")
public interface ItemServiceFeign {

    @RequestMapping("/service/item/selectItemInfo")
    TbItem selectItemInfo(@RequestParam Long itemId);

    @RequestMapping("/service/item/selectTbItemAllByPage")
    PageResult selectTbItemAllByPage(@RequestParam Integer page, @RequestParam Integer rows);


    @RequestMapping("/service/itemCategory/selectItemCategoryByParentId")
    List<TbItemCat> selectItemCategoryByParentId(@RequestParam Long id);

    @RequestMapping("/service/itemParam/selectItemParamByItemCatId")
    TbItemParam selectItemParamByItemCatId(@RequestParam Long itemCatId);

    /**
     * 根据主键ID删除商品
     *
     * @param itemId
     */
    @RequestMapping("/service/item/deleteItemById")
    void deleteItemById(@RequestParam Long itemId);

    /**
     * 预更新
     *
     * @param itemId
     * @return
     */
    @RequestMapping("/service/item/preUpdateItem")
    PreItemVo preUpdateItem(@RequestParam Long itemId);

    /**
     * 修改商品信息
     *
     * @param tbItem
     */
    @RequestMapping("/service/item/updateTbItem")
    void updateTbItem(@RequestBody TbItem tbItem);

    @RequestMapping("/service/item/insertTbItem")
    void insertTbItem(@RequestBody TbItem tbItem, @RequestParam String desc, @RequestParam String itemParams);

    @RequestMapping("/service/item/updateTbItem")
    void updateTbItem(@RequestBody TbItem tbItem, @RequestParam String desc, @RequestParam String itemParams);

    @RequestMapping("/service/itemParam/selectItemParamAll")
    PageResult selectItemParamAll();

    @RequestMapping("/service/itemParam/deleteItemParamById")
    void deleteItemParamById(@RequestParam Long id);

    @RequestMapping("/service/itemParam/insertItemParam")
    Integer insertItemParam(@RequestParam Long itemCatId, @RequestParam String paramData);
}
