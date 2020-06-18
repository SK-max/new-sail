package com.usian.service;

import com.usian.PageResult;
import com.usian.pojo.TbItemParam;

import java.util.List;

public interface ItemParamService {

    TbItemParam selectItemParamByItemCatId(Long itemCatId);

    PageResult selectItemParamAll();

    void deleteItemParamById(Long id);

    Integer insertItemParam(Long itemCatId, String paramData);
}
