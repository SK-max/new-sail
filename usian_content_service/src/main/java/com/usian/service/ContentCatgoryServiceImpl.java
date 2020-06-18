package com.usian.service;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.usian.PageResult;
import com.usian.mapper.TbContentCategoryMapper;
import com.usian.mapper.TbContentMapper;
import com.usian.pojo.TbContent;
import com.usian.pojo.TbContentCategory;
import com.usian.pojo.TbContentCategoryExample;
import com.usian.pojo.TbContentExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;

@Service
//开启事务
@Transactional
public class ContentCatgoryServiceImpl implements ContentCatgoryService {

    //注入Mapper接口
    @Autowired
    private TbContentCategoryMapper categoryMapper;

    @Autowired
    private TbContentMapper tbContentMapper;


    /**
     * 根据parentId查询内容分类
     *
     * @param id
     * @return
     */
    @Override
    public List<TbContentCategory> selectContentCategoryByParentId(Long id) {
        //创建内容分类查询模板
        TbContentCategoryExample example = new TbContentCategoryExample();
        TbContentCategoryExample.Criteria criteria = example.createCriteria();
        //根据父类ID查询
        criteria.andParentIdEqualTo(id);
        List<TbContentCategory> list = categoryMapper.selectByExample(example);
        return list;
    }

    /**
     * 添加方法
     *
     * @param tbContentCategory
     */
    @Override
    public void insertContentCategory(TbContentCategory tbContentCategory) {
        //补全内容信息
        tbContentCategory.setCreated(new Date());
        tbContentCategory.setUpdated(new Date());
        tbContentCategory.setStatus(1);
        tbContentCategory.setIsParent(false);
        tbContentCategory.setSortOrder(1);
        //添加内容
        categoryMapper.insertSelective(tbContentCategory);
        TbContentCategory category = categoryMapper.selectByPrimaryKey(tbContentCategory.getParentId());
        if (!category.getIsParent()) {
            category.setIsParent(true);
            categoryMapper.updateByPrimaryKey(category);
        }
    }

    /**
     * 根据主键ID删除
     *
     * @param cateGoryId
     * @return
     */
    @Override
    public Integer deleteContentCategoryById(Long cateGoryId) {
        TbContentCategory category = categoryMapper.selectByPrimaryKey(cateGoryId);
        //判断当前节点是否为父节点
        if (category.getIsParent() == true) {
            //父节点不能删除
            return 0;
        }
        //不是父节点 可以删除
        categoryMapper.deleteByPrimaryKey(cateGoryId);
        //查询出兄弟节点的数量
        TbContentCategoryExample example = new TbContentCategoryExample();
        TbContentCategoryExample.Criteria criteria = example.createCriteria();
        criteria.andParentIdEqualTo(category.getParentId());
        List<TbContentCategory> list = categoryMapper.selectByExample(example);
        //如果父节点删除当前节点之后 子节点为0则将isparent改为false
        System.out.println("嘻嘻嘻嘻嘻" + list.size());
        if (list.size() == 0) {
            TbContentCategory contentCategory = categoryMapper.selectByPrimaryKey(category.getParentId());
            contentCategory.setUpdated(new Date());
            contentCategory.setIsParent(false);
            categoryMapper.updateByPrimaryKey(contentCategory);
        }
        return 200;
    }

    /**
     * 修改方法
     *
     * @param tbContentCategory
     */
    @Override
    public void updateContentCategory(TbContentCategory tbContentCategory) {
        TbContentCategory contentCategory = categoryMapper.selectByPrimaryKey(tbContentCategory.getId());
        contentCategory.setName(tbContentCategory.getName());
        categoryMapper.updateByPrimaryKey(contentCategory);

    }




}
