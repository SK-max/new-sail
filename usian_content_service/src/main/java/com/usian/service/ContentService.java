package com.usian.service;

import com.usian.AdNode;
import com.usian.PageResult;
import com.usian.pojo.TbContent;
import java.util.List;


public interface ContentService {

    PageResult selectTbContentAllByCategoryId(Integer page, Integer rows, Long categoryId);

    void insertTbcontent(TbContent tbContent);

    void deleteContentByIds(Long ids);

    List<AdNode> selectFrontendContentByAD();
}
