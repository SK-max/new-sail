package com.usian.controller;


import com.usian.AdNode;
import com.usian.Result;
import com.usian.feign.ContentCategoryFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/frontend/content")
public class ContentController {

    //注入Contentfeign接口
    @Autowired
    private ContentCategoryFeign categoryFeign;

    /**
     * 映射查询首页大广告
     * @return
     */
    @RequestMapping("/selectFrontendContentByAD")
    public Result selectFrontendContentByAD() {
        try {
            List<AdNode> adNodes = categoryFeign.selectFrontendContentByAD();
            return Result.ok(adNodes);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("查无结果");
        }
    }
}
