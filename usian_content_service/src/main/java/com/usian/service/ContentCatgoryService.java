package com.usian.service;

import com.usian.PageResult;
import com.usian.pojo.TbContent;
import com.usian.pojo.TbContentCategory;

import java.util.List;

public interface ContentCatgoryService {

    List<TbContentCategory> selectContentCategoryByParentId(Long id);

    void insertContentCategory(TbContentCategory tbContentCategory);

    Integer deleteContentCategoryById(Long cateGoryId);

    void updateContentCategory(TbContentCategory tbContentCategory);


}
