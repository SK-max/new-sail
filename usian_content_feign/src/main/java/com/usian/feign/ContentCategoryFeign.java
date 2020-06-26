package com.usian.feign;


import com.usian.AdNode;
import com.usian.PageResult;
import com.usian.pojo.TbContent;
import com.usian.pojo.TbContentCategory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("usian-content-service")
public interface ContentCategoryFeign {


    @RequestMapping("/service/contentCategory/selectContentCategoryByParentId")
    List<TbContentCategory> selectContentCategoryByParentId(@RequestParam Long id);

    @RequestMapping("/service/contentCategory/insertContentCategory")
    void insertContentCategory(@RequestBody TbContentCategory tbContentCategory);

    @RequestMapping("/service/contentCategory/deleteContentCategoryById")
    Integer deleteContentCategoryById(@RequestParam Long cateGoryId);

    @RequestMapping("/service/contentCategory/updateContentCategory")
    void updateContentCategory(@RequestBody TbContentCategory tbContentCategory);

    @RequestMapping("/service/contentCategory/selectTbContentAllByCategoryId")
    PageResult selectTbContentAllByCategoryId(@RequestParam Integer page, @RequestParam Integer rows, @RequestParam Long categoryId);

    @RequestMapping("/service/contentCategory/insertTbcontent")
    void insertTbContent(@RequestBody TbContent tbContent);

    @RequestMapping("/service/contentCategory/deleteContentByIds")
    void deleteContentByIds(@RequestParam Long ids);

    @RequestMapping("/service/content/selectFrontendContentByAD")
    List<AdNode> selectFrontendContentByAD();


}
