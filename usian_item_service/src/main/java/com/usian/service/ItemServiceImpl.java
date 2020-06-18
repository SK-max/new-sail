package com.usian.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.usian.IDUtils;
import com.usian.PageResult;
import com.usian.mapper.TbItemDescMapper;
import com.usian.mapper.TbItemMapper;
import com.usian.mapper.TbItemParamItemMapper;
import com.usian.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;


@Service
@Transactional
public class ItemServiceImpl implements ItemService {

    @Autowired
    private TbItemMapper tbItemMapper;

    @Autowired
    private TbItemDescMapper itemDescMapper;

    @Autowired
    private TbItemParamItemMapper tbItemParamItemMapper;


    @Override
    public TbItem selectItemInfo(Long itemId) {
        return tbItemMapper.selectByPrimaryKey(itemId);
    }

    /**
     * 查询并分页
     *
     * @param page
     * @param rows
     * @return
     */
    @Override
    public PageResult selectTbItemAllByPage(Integer page, Integer rows) {
        PageHelper.startPage(page, rows);
        TbItemExample example = new TbItemExample();
        TbItemExample.Criteria criteria = example.createCriteria();
        criteria.andStatusEqualTo((byte) 1);
        List<TbItem> list = tbItemMapper.selectByExample(example);
        PageInfo<TbItem> tbItemPageInfo = new PageInfo<>(list);
        PageResult pageResult = new PageResult();
        pageResult.setPageIndex(tbItemPageInfo.getPageNum());
        pageResult.setTotaPage(Long.valueOf(tbItemPageInfo.getPages()));
        pageResult.setResult(tbItemPageInfo.getList());
        return pageResult;
    }

    /**
     * 根据主键删除
     *
     * @param itemId
     */
    @Override
    public void deleteItemById(Long itemId) {
        tbItemMapper.deleteByPrimaryKey(itemId);
    }

    /**
     * 预更新查询
     *
     * @param itemId
     * @return
     */
    @Override
    public PreItemVo preUpdateItem(Long itemId) {
        return tbItemMapper.preUpdateItem(itemId);
    }

    /**
     * 添加方法
     * @param tbItem
     * @param desc
     * @param itemParams
     */
    @Override
    public void insertTbItem(TbItem tbItem, String desc, String itemParams) {

        //补全Item
        long itemId = IDUtils.genItemId();
        Date date = new Date();
        tbItem.setId(itemId);
        tbItem.setStatus((byte) 1);
        tbItem.setUpdated(date);
        tbItem.setCreated(date);
        int a1 = tbItemMapper.insertSelective(tbItem);
        //补齐商品对象描述对象
        TbItemDesc tbItemDesc = new TbItemDesc();
        tbItemDesc.setItemId(itemId);
        tbItemDesc.setItemDesc(desc);
        tbItemDesc.setCreated(date);
        tbItemDesc.setUpdated(date);
        int a2 = itemDescMapper.insertSelective(tbItemDesc);
        //补齐商品规格参数
        TbItemParamItem tbItemParamItem = new TbItemParamItem();
            tbItemParamItem.setItemId(itemId);
            tbItemParamItem.setParamData(itemParams);
            tbItemParamItem.setUpdated(date);
            tbItemParamItem.setCreated(date);
        int a3 = tbItemParamItemMapper.insertSelective(tbItemParamItem);
    }

    @Override
    public void updateTbItem(TbItem tbItem, String desc, String itemParams) {
        //修改商品
        Long itemId = tbItem.getId();
        Date date = new Date();
        tbItem.setUpdated(date);
        tbItemMapper.updateByPrimaryKey(tbItem);
        //修改商品描述
        TbItemDescExample example = new TbItemDescExample();
        TbItemDescExample.Criteria criteria = example.createCriteria();
        criteria.andItemIdEqualTo(itemId);
        TbItemDesc tbItemDesc = new TbItemDesc();
        tbItemDesc.setItemDesc(desc);
        itemDescMapper.updateByExample(tbItemDesc,example);
        //修改商品规格参数
        TbItemParamItem tbItemParamItem = new TbItemParamItem();
        tbItemParamItem.setParamData(itemParams);
        TbItemParamItemExample tbItemParamItemExample = new TbItemParamItemExample();
        TbItemParamItemExample.Criteria tbItemParamItemExampleCriteria = tbItemParamItemExample.createCriteria();
        tbItemParamItemExampleCriteria.andItemIdEqualTo(itemId);
        tbItemParamItemMapper.updateByExample(tbItemParamItem,tbItemParamItemExample);
    }
}
