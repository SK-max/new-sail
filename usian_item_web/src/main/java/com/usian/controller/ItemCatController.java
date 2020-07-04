package com.usian.controller;


import com.usian.Result;
import com.usian.feign.ItemServiceFeign;
import com.usian.pojo.TbContentCategory;
import com.usian.pojo.TbItem;
import com.usian.pojo.TbItemCat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/backend/itemCategory")
public class ItemCatController {


    @Autowired
    private ItemServiceFeign itemServiceFeign;

    @RequestMapping("/selectItemCategoryByParentId")
    public Result selectItemCategoryByParentId(@RequestParam (defaultValue = "0") Long id) {
        System.out.println("哈哈哈哈");
        List<TbItemCat> list = itemServiceFeign.selectItemCategoryByParentId(id);
        if(list!=null&&list.size()>0){
            return Result.ok(list);
        }
        System.out.println("嘻嘻嘻嘻");
        return Result.error("查无结果");
    }



}
