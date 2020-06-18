package com.usian.service;

import com.usian.PageResult;
import com.usian.pojo.TbContent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


public interface ContentService {

    PageResult selectTbContentAllByCategoryId(Integer page, Integer rows, Long categoryId);

    void insertTbcontent(TbContent tbContent);

    void deleteContentByIds(Long ids);
}
