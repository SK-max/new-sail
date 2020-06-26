package com.usian.controller;


import com.usian.PageResult;
import com.usian.pojo.PreItemVo;
import com.usian.pojo.TbItem;
import com.usian.pojo.TbItemDesc;
import com.usian.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/service/item")
public class ItemController {

    @Autowired
    private ItemService itemService;

    /**
     * 根据商品ID查询主键
     *
     * @return
     */
    @RequestMapping("/selectItemInfo")
    public TbItem selectItemInfo(Long itemId) {

        return itemService.selectItemInfo(itemId);
    }

    @RequestMapping("/selectTbItemAllByPage")
    public PageResult selectTbItemAllByPage(Integer page, Integer rows) {
        return itemService.selectTbItemAllByPage(page, rows);
    }

    @RequestMapping("/deleteItemById")
    public void deleteItemById(Long itemId) {
        itemService.deleteItemById(itemId);
    }

    @RequestMapping("/preUpdateItem")
    public PreItemVo preUpdateItem(Long itemId) {
        return itemService.preUpdateItem(itemId);
    }


    @RequestMapping("/insertTbItem")
    public void insertTbItem(@RequestBody TbItem tbItem, @RequestParam String desc, @RequestParam String itemParams) {
        itemService.insertTbItem(tbItem, desc, itemParams);
    }

    @RequestMapping("/updateTbItem")
    public void updateTbItem(@RequestBody TbItem tbItem, @RequestParam String desc, @RequestParam String itemParams) {
        itemService.updateTbItem(tbItem, desc, itemParams);

    }

    /**
     * 根据itemId查询商品详情
     * @param itemId
     * @return
     */
    @RequestMapping("/selectItemDescByItemId")
    public TbItemDesc selectItemDescByItemId(Long itemId) {
        return itemService.selectItemDescByItemId(itemId);
    }

}
