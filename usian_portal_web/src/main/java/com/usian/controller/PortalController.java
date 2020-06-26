package com.usian.controller;


import com.usian.AdNode;
import com.usian.CatResult;
import com.usian.Result;
import com.usian.feign.ContentCategoryFeign;
import com.usian.feign.ItemServiceFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/frontend/itemCategory")
public class PortalController {


    @Autowired
    private ItemServiceFeign itemServiceFeign;



    @RequestMapping("/selectItemCategoryAll")
    public Result selectItemCategoryAll() {
        CatResult catResults = itemServiceFeign.selectItemCategoryAll();
        if (catResults.getData().size() > 0) {
            return Result.ok(catResults);
        }
        return Result.error("查询失败");
    }



}
