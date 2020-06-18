package com.usian.controller;


import com.usian.PageResult;
import com.usian.pojo.TbItemParam;
import com.usian.pojo.TbItemParamItem;
import com.usian.service.ItemParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/service/itemParam")
public class ItemParamController {

    @Autowired
    private ItemParamService itemParamService;

    @RequestMapping("/selectItemParamByItemCatId")
    public TbItemParam selectItemParamByItemCatId(Long itemCatId) {
        return itemParamService.selectItemParamByItemCatId(itemCatId);
    }

    @RequestMapping("/selectItemParamAll")
    public PageResult selectItemParamAll() {
       return itemParamService.selectItemParamAll();
    }

    @RequestMapping("/deleteItemParamById")
    public void deleteItemParamById(@RequestParam Long id){
        itemParamService.deleteItemParamById(id);
    };

    @RequestMapping("/insertItemParam")
   public Integer insertItemParam(@RequestParam Long itemCatId, @RequestParam String paramData){
        return itemParamService.insertItemParam(itemCatId,paramData);
    };

}
