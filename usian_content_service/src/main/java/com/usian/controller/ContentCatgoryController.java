package com.usian.controller;


import com.usian.PageResult;
import com.usian.pojo.TbContent;
import com.usian.pojo.TbContentCategory;
import com.usian.service.ContentCatgoryService;
import com.usian.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/service/contentCategory")
public class ContentCatgoryController {

    @Autowired
    private ContentCatgoryService catgoryService;

    @Autowired
    private ContentService contentService;

    @RequestMapping("/selectContentCategoryByParentId")
    public List<TbContentCategory> selectContentCategoryByParentId(@RequestParam Long id) {
        return catgoryService.selectContentCategoryByParentId(id);
    };

    @RequestMapping("/insertContentCategory")
    public void insertContentCategory(@RequestBody TbContentCategory tbContentCategory) {
        catgoryService.insertContentCategory(tbContentCategory);
    };

    @RequestMapping("/deleteContentCategoryById")
    public Integer deleteContentCategoryById(@RequestParam Long cateGoryId) {
        return catgoryService.deleteContentCategoryById(cateGoryId);
    };

    @RequestMapping("/updateContentCategory")
    public void updateContentCategory(@RequestBody TbContentCategory tbContentCategory) {
        catgoryService.updateContentCategory(tbContentCategory);
    };

    @RequestMapping("/selectTbContentAllByCategoryId")
    PageResult selectTbContentAllByCategoryId(Integer page, Integer rows, @RequestParam Long categoryId) {
        return contentService.selectTbContentAllByCategoryId(page,rows,categoryId);
    };

    @RequestMapping("/insertTbcontent")
    void insertTbContent(@RequestBody TbContent tbContent){
        contentService.insertTbcontent(tbContent);
    }

    @RequestMapping("/deleteContentByIds")
    void deleteContentByIds(@RequestParam Long ids){
        contentService.deleteContentByIds(ids);
    };

}
