package com.usian.service;


import com.usian.PageResult;
import com.usian.pojo.PreItemVo;
import com.usian.pojo.TbItem;

public interface ItemService {

    TbItem selectItemInfo(Long itemId);

    /**
     * 查询并分页
     * @param page
     * @param rows
     * @return
     */
    PageResult selectTbItemAllByPage(Integer page, Integer rows);

    /**
     * 根据主键ID删除
     * @param itemId
     */
    void deleteItemById(Long itemId);

    /**
     * 预更新方法
     * @param itemId
     * @return
     */
    PreItemVo preUpdateItem(Long itemId);

    /**
     * 添加方法
     * @param tbItem
     * @param desc
     * @param itemParams
     */
    void insertTbItem(TbItem tbItem, String desc, String itemParams);

    /**
     * 修改方法
     * @param tbItem
     * @param desc
     * @param itemParams
     */
    void updateTbItem(TbItem tbItem, String desc, String itemParams);

}
