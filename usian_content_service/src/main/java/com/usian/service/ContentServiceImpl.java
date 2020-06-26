package com.usian.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.usian.AdNode;
import com.usian.PageResult;
import com.usian.mapper.TbContentMapper;
import com.usian.pojo.TbContent;
import com.usian.pojo.TbContentExample;
import com.usian.redis.RedisClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class ContentServiceImpl implements ContentService {

    //注入属性
    @Value("${AD_CATEGORY_ID}")
    private Long AD_CATEGORY_ID;

    @Value("${AD_HEIGHT}")
    private Integer AD_HEIGHT;

    @Value("${AD_HEIGHTB}")
    private Integer AD_HEIGHTB;

    @Value("${AD_WIDTH}")
    private Integer AD_WIDTH;

    @Value("${AD_WIDTHB}")
    private Integer AD_WIDTHB;

    @Value("${portal_ad_redis_key}")
    private String portal_ad_redis_key;

    @Autowired
    private RedisClient redisClient;


    /**
     * 注入Mapper
     */
    @Autowired
    private TbContentMapper tbContentMapper;


    /**
     * 分页查询
     *
     * @param page
     * @param rows
     * @param categoryId
     * @return
     */
    @Override
    public PageResult selectTbContentAllByCategoryId(Integer page, Integer rows, Long categoryId) {
        PageHelper.startPage(page, rows);
        TbContentExample example = new TbContentExample();
        TbContentExample.Criteria criteria = example.createCriteria();
        criteria.andCategoryIdEqualTo(categoryId);
        List<TbContent> list = tbContentMapper.selectByExample(example);
        PageInfo<TbContent> pageInfo = new PageInfo<>(list);
        PageResult pageResult = new PageResult();
        pageResult.setResult(pageInfo.getList());
        pageResult.setPageIndex(pageInfo.getPageNum());
        pageResult.setTotaPage(Long.valueOf(pageInfo.getPages()));
        return pageResult;
    }

    /**
     * 添加广告内容
     *
     * @param tbContent
     */
    @Override
    public void insertTbcontent(TbContent tbContent) {
        Date date = new Date();
        tbContent.setCreated(date);
        tbContent.setUpdated(date);
        tbContentMapper.insertSelective(tbContent);
        redisClient.hdel(portal_ad_redis_key, AD_CATEGORY_ID.toString());
    }

    /**
     * 删除内容
     *
     * @param ids
     */
    @Override
    public void deleteContentByIds(Long ids) {
        System.out.println(ids);
        tbContentMapper.deleteByPrimaryKey(ids);
        redisClient.hdel(portal_ad_redis_key, AD_CATEGORY_ID.toString());
    }

    /**
     * 查询首页大广告
     *
     * @return
     */
    @Override
    public List<AdNode> selectFrontendContentByAD() {

        List<AdNode> adNodeList = (List<AdNode>) redisClient.hget(portal_ad_redis_key, AD_CATEGORY_ID.toString());
        if (adNodeList != null && adNodeList.size() > 0) {
            return adNodeList;
        }
        TbContentExample example = new TbContentExample();
        TbContentExample.Criteria criteria = example.createCriteria();
        criteria.andCategoryIdEqualTo(AD_CATEGORY_ID);
        List<TbContent> contentList = tbContentMapper.selectByExample(example);
        List<AdNode> list = new ArrayList<>();
        for (TbContent tbContent : contentList) {
            AdNode node = new AdNode();
            node.setHeight(AD_HEIGHT);
            node.setHeightB(AD_HEIGHTB);
            node.setWidth(AD_WIDTH);
            node.setWidthB(AD_WIDTHB);
            node.setSrc(tbContent.getPic());
            node.setSrcB(tbContent.getPic2());
            list.add(node);
        }
        redisClient.hset(portal_ad_redis_key, AD_CATEGORY_ID.toString(), list);
        return list;
    }

}
