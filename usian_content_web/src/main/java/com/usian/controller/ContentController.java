package com.usian.controller;


import com.usian.PageResult;
import com.usian.Result;
import com.usian.feign.ContentCategoryFeign;
import com.usian.pojo.TbContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/backend/content")
public class ContentController {

    @Autowired
    private ContentCategoryFeign contentCategoryFeign;


    @RequestMapping("/selectTbContentAllByCategoryId")
    public Result selectTbContentAllByCategoryId(@RequestParam(defaultValue = "1") Integer page,
                                                 @RequestParam(defaultValue = "30") Integer rows,
                                                 @RequestParam Long categoryId) {
        try {
            System.out.println(categoryId);
            PageResult pageResult = contentCategoryFeign.selectTbContentAllByCategoryId(page, rows, categoryId);
            return Result.ok(pageResult);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("查无结果");
        }
    }

    @RequestMapping("/insertTbContent")
    public Result insertTbContent(TbContent tbContent) {
        try {
            contentCategoryFeign.insertTbContent(tbContent);
            return Result.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("添加失败");
        }
    }

    @RequestMapping("/deleteContentByIds")
    public Result deleteContentByIds(Long ids) {
        try {
            contentCategoryFeign.deleteContentByIds(ids);
            return Result.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("删除失败");
        }
    }

}
