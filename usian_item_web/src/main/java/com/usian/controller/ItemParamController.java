package com.usian.controller;


import com.usian.PageResult;
import com.usian.Result;
import com.usian.feign.ItemServiceFeign;
import com.usian.pojo.TbItemParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/backend/itemParam")
public class ItemParamController {

    @Autowired
    private ItemServiceFeign itemServiceFeign;

    @RequestMapping("/selectItemParamByItemCatId/{itemCatId}")
    public Result selectItemParamByItemCatId(@PathVariable Long itemCatId) {
        TbItemParam itemParam = itemServiceFeign.selectItemParamByItemCatId(itemCatId);
        if (itemParam != null) {
            return Result.ok(itemParam);
        }
        return Result.error("查无结果");
    }

    @RequestMapping("/selectItemParamAll")
    public Result selectItemParamAll() {

        try {
            PageResult pageResult = itemServiceFeign.selectItemParamAll();
            return Result.ok(pageResult);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("查无结果");
        }
    }

    @RequestMapping("/deleteItemParamById")
    public Result deleteItemParamById(Long id) {
        try {
            itemServiceFeign.deleteItemParamById(id);
            return Result.ok();
        } catch (Exception e) {
            return Result.error("查无结果");
        }
    }

    @RequestMapping("/insertItemParam")
    public Result insertItemParam(@RequestParam Long itemCatId, @RequestParam String paramData) {

           Integer num =itemServiceFeign.insertItemParam(itemCatId,paramData);
            if (num>0){
                return Result.ok();
            }else {
                return Result.error("添加失败");
            }


    }
}
