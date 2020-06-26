package com.usian.service;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sun.org.apache.regexp.internal.RE;
import com.usian.PageResult;
import com.usian.mapper.TbItemParamItemMapper;
import com.usian.mapper.TbItemParamMapper;
import com.usian.pojo.TbItemParam;
import com.usian.pojo.TbItemParamExample;
import com.usian.pojo.TbItemParamItem;
import com.usian.pojo.TbItemParamItemExample;
import com.usian.redis.RedisClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class ItemParamServiceImpl implements ItemParamService {


    @Value("${ITEM_INFO}")
    private String ITEM_INFO;

    @Value("${PARAM}")
    private String PARAM;

    @Value("${ITEM_INFO_EXPIRE}")
    private Long ITEM_INFO_EXPIRE;

    @Autowired
    private RedisClient redisClient;


    @Autowired
    private TbItemParamMapper tbItemParamMapper;

    @Autowired
    private TbItemParamItemMapper tbItemParamItemMapper;


    public TbItemParam selectItemParamByItemCatId(Long itemCatId) {
        TbItemParamExample example = new TbItemParamExample();
        TbItemParamExample.Criteria criteria = example.createCriteria();
        criteria.andItemCatIdEqualTo(itemCatId);
        List<TbItemParam> list = tbItemParamMapper.selectByExampleWithBLOBs(example);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public PageResult selectItemParamAll() {
        PageHelper.startPage(1, 2);
        List<TbItemParam> list = tbItemParamMapper.selectByExampleWithBLOBs(new TbItemParamExample());
        PageInfo<TbItemParam> pageInfo = new PageInfo<>(list);
        PageResult pageResult = new PageResult();
        pageResult.setResult(pageInfo.getList());
        pageResult.setPageIndex(pageInfo.getPageNum());
        pageResult.setTotaPage(Long.valueOf(pageInfo.getPages()));
        return pageResult;
    }

    @Override
    public void deleteItemParamById(Long id) {
        tbItemParamMapper.deleteByPrimaryKey(id);
    }

    @Override
    public Integer insertItemParam(Long itemCatId, String paramData) {
        TbItemParamExample example = new TbItemParamExample();
        TbItemParamExample.Criteria criteria = example.createCriteria();
        criteria.andItemCatIdEqualTo(itemCatId);
        List<TbItemParam> list = tbItemParamMapper.selectByExample(example);
        if (list.size() > 0) {
            return 0;
        }
        Date date = new Date();
        TbItemParam param = new TbItemParam();
        param.setItemCatId(itemCatId);
        param.setParamData(paramData);
        param.setUpdated(date);
        param.setUpdated(date);
        return tbItemParamMapper.insertSelective(param);
    }



    /**
     * 查询商品参数详情
     *
     * @param itemId
     * @return
     */
    @Override
    public TbItemParamItem selectTbItemParamItemByItemId(Long itemId) {
        TbItemParamItem tbItemParamItem = (TbItemParamItem) redisClient.get(ITEM_INFO + ":" + itemId + ":" + PARAM);
        if (tbItemParamItem != null) {
            return tbItemParamItem;
        }
        TbItemParamItemExample example = new TbItemParamItemExample();
        TbItemParamItemExample.Criteria criteria = example.createCriteria();
        criteria.andItemIdEqualTo(itemId);
        List<TbItemParamItem> list = tbItemParamItemMapper.selectByExampleWithBLOBs(example);
        if (list != null && list.size() > 0) {
            redisClient.set(ITEM_INFO + ":" + itemId + ":" + PARAM, list.get(0));
            redisClient.expire(ITEM_INFO + ":" + itemId + PARAM, ITEM_INFO_EXPIRE);
            return list.get(0);
        }
        return null;
    }
}
