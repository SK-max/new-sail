package com.usian.service;

import com.usian.CatResult;
import com.usian.pojo.TbItemCat;
import java.util.List;

public interface ItemCatService {

    List<TbItemCat> selectItemCategoryByParentId(Long id);

    CatResult selectItemCategoryAll();
}
