package com.usian.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.usian.IDUtils;
import com.usian.PageResult;
import com.usian.mapper.TbItemDescMapper;
import com.usian.mapper.TbItemMapper;
import com.usian.mapper.TbItemParamItemMapper;
import com.usian.mapper.TbOrderItemMapper;
import com.usian.pojo.*;
import com.usian.redis.RedisClient;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.Cipher;
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

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private TbOrderItemMapper tbOrderItemMapper;

    @Autowired
    private RedisClient redisClient;

    @Value("${ITEM_INFO}")
    private String ITEM_INFO;

    @Value("${BASE}")
    private String BASE;

    @Value("${DESC}")
    private String DESC;

    @Value("${PARAM}")
    private String PARAM;

    @Value("${ITEM_INFO_EXPIRE}")
    private Long ITEM_INFO_EXPIRE;

    @Value("${SETNX_LOCK_KEY}")
    private String SETNX_LOCK_KEY;

    @Value("${DESC_LOCK_KEY}")
    private String DESC_LOCK_KEY;

    /**
     * 查询商品信息
     *
     * @param itemId
     * @return
     */
    @Override
    public TbItem selectItemInfo(Long itemId) {
        //先查询Redis如果有直接返回
        TbItem tbItem = (TbItem) redisClient.get(ITEM_INFO + ":" + itemId + ":" + BASE);
        if (tbItem != null) {
            return tbItem;
        }
        //如果Redis查不到 再查询mysal,查询到之后缓存到Redis
        //解决缓存击穿
        if (redisClient.setNx(SETNX_LOCK_KEY + ":" + itemId, itemId, 30L)) {
            tbItem = tbItemMapper.selectByPrimaryKey(itemId);
            redisClient.del(SETNX_LOCK_KEY + ":" + itemId + BASE);
            //将查询结果无论是否为空 都存入redis，避免缓存穿透
            redisClient.set(ITEM_INFO + ":" + itemId + ":" + BASE, tbItem);
            if (tbItem == null) {
                //如果查询结果为空就设置过期key时间并返回null
                redisClient.expire(ITEM_INFO + ":" + itemId + ":" + BASE, 30L);
                return null;
            } else {
                //如果不为空就设置过期时间
                redisClient.expire(ITEM_INFO + ":" + itemId + ":" + BASE, ITEM_INFO_EXPIRE);
                return tbItem;
            }
        } else {
            try {
                //如果没有拿到锁 就让线程休眠1秒钟
                Thread.sleep(1000);
                //一秒钟之后递归调用
                selectItemInfo(itemId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
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
     *
     * @param tbItem
     * @param desc
     * @param itemParams
     */
    @Override
    public void insertTbItem(TbItem tbItem, String desc, String itemParams) {

        //补全Item
        Long itemId = IDUtils.genItemId();
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
        amqpTemplate.convertAndSend("item_exchange", "item.add", itemId);
        int a3 = tbItemParamItemMapper.insertSelective(tbItemParamItem);
    }

    /**
     * 修改方法
     *
     * @param tbItem
     * @param desc
     * @param itemParams
     */
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
        itemDescMapper.updateByExample(tbItemDesc, example);
        //修改商品规格参数
        TbItemParamItem tbItemParamItem = new TbItemParamItem();
        tbItemParamItem.setParamData(itemParams);
        TbItemParamItemExample tbItemParamItemExample = new TbItemParamItemExample();
        TbItemParamItemExample.Criteria tbItemParamItemExampleCriteria = tbItemParamItemExample.createCriteria();
        tbItemParamItemExampleCriteria.andItemIdEqualTo(itemId);
        tbItemParamItemMapper.updateByExample(tbItemParamItem, tbItemParamItemExample);
    }

    /**
     * 根据ItemId查询并返回详情
     *
     * @param itemId
     * @return
     */
    @Override
    public TbItemDesc selectItemDescByItemId(Long itemId) {
        //1先从Redis查询 如果存在直接返回
        TbItemDesc tbItemDesc = (TbItemDesc) redisClient.get(ITEM_INFO + ":" + itemId + ":" + DESC);
        if (tbItemDesc != null) {
            return tbItemDesc;
        }
        if (redisClient.setNx(DESC_LOCK_KEY + ":" + itemId, itemId, 30L)) {
            //2再从Mysql查询并设置失效时间
            TbItemDescExample example = new TbItemDescExample();
            TbItemDescExample.Criteria criteria = example.createCriteria();
            criteria.andItemIdEqualTo(itemId);
            List<TbItemDesc> list = itemDescMapper.selectByExampleWithBLOBs(example);
            redisClient.del(DESC_LOCK_KEY + ":" + itemId + ":" + DESC);
            redisClient.set(ITEM_INFO + ":" + itemId + ":" + DESC, list.get(0));
            if (list != null && list.size() > 0) {
                redisClient.expire(ITEM_INFO + ":" + itemId + ":" + DESC, ITEM_INFO_EXPIRE);
                return list.get(0);
            } else {
                redisClient.expire(ITEM_INFO + ":" + itemId + ":" + DESC, 30L);
                return null;
            }
        } else {
            try {
                Thread.sleep(1000);
                selectItemDescByItemId(itemId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 根据订单ID 扣减库存
     *
     * @param orderId
     * @return
     */
    @Override
    public Integer updateTbItemByOrderId(String orderId) {
        TbOrderItemExample example = new TbOrderItemExample();
        TbOrderItemExample.Criteria criteria = example.createCriteria();
        criteria.andOrderIdEqualTo(orderId);
        List<TbOrderItem> itemList = tbOrderItemMapper.selectByExample(example);
        int result = 0;
        for (TbOrderItem tbOrderItem : itemList) {
            TbItem tbItem = tbItemMapper.selectByPrimaryKey(Long.valueOf(tbOrderItem.getItemId()));
            tbItem.setNum(tbItem.getNum() - tbOrderItem.getNum());
            result += tbItemMapper.updateByPrimaryKeySelective(tbItem);
        }
        return result;
    }
}
