package com.usian.controller;


import com.usian.Result;
import com.usian.feign.ContentCategoryFeign;
import com.usian.pojo.TbContentCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/backend/content")
public class ContentCategoryController {

    @Autowired
    private ContentCategoryFeign categoryFeign;

    @RequestMapping("/selectContentCategoryByParentId")
    public Result selectContentCategoryByParentId(@RequestParam(defaultValue = "0") Long id) {
            List<TbContentCategory> list = categoryFeign.selectContentCategoryByParentId(id);
            if(list!=null&&list.size()>0){
                return Result.ok(list);
            }
            return Result.error("查无结果");

    }

    @RequestMapping("/insertContentCategory")
    public Result insertContentCategory(TbContentCategory tbContentCategory) {
        try {
            categoryFeign.insertContentCategory(tbContentCategory);
            return Result.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("添加错误");
        }
    }


    @RequestMapping("/deleteContentCategoryById")
    public Result deleteContentCategoryById(@RequestParam Long categoryId) {
        try {
            Integer num = categoryFeign.deleteContentCategoryById(categoryId);
            if (num > 0) {
                return Result.ok();
            } else {
                return Result.error("刪除失敗");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("删除出错");
        }
    }

    @RequestMapping("/updateContentCategory")
    public Result updateContentCategory(Long id, String name) {
        try {
            TbContentCategory category = new TbContentCategory();
            category.setId(id);
            category.setName(name);
            categoryFeign.updateContentCategory(category);
            return Result.ok();
        }catch (Exception e){
            e.printStackTrace();
            return Result.error("修改失败");
        }
    }

}
