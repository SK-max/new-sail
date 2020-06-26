package com.usian.service;

import com.usian.CatNode;
import com.usian.CatResult;
import com.usian.mapper.TbItemCatMapper;
import com.usian.pojo.TbItemCat;
import com.usian.pojo.TbItemCatExample;
import com.usian.redis.RedisClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ItemCatServiceImpl implements ItemCatService {

    @Autowired
    private TbItemCatMapper tbItemCatMapper;

   @Value("${portal_catresult_redis_key}")
    private String portal_catresult_redis_key;

   @Autowired
   private RedisClient redisClient;

    @Override
    public List<TbItemCat> selectItemCategoryByParentId(Long id) {
        TbItemCatExample example = new TbItemCatExample();
        TbItemCatExample.Criteria criteria = example.createCriteria();
        criteria.andParentIdEqualTo(id);
        criteria.andStatusEqualTo(1);
        List<TbItemCat> tbItemCatList = tbItemCatMapper.selectByExample(example);
        return tbItemCatList;
    }

    @Override
    public CatResult selectItemCategoryAll() {

        CatResult catResult = (CatResult)redisClient.get(portal_catresult_redis_key);
        if(catResult!=null){
            return catResult;
        }
        CatResult result = new CatResult();
        //查询并将查询之后的集合赋值给data属性
        result.setData(getCatList(0L));
        //添加到缓存
        redisClient.set(portal_catresult_redis_key,result);
        return result;
    }

    //私有方法 查询cat菜单
    private List<?> getCatList(Long parentId) {
        TbItemCatExample example = new TbItemCatExample();
        TbItemCatExample.Criteria criteria = example.createCriteria();
        criteria.andParentIdEqualTo(parentId);
        //创建条件查询模板
        List<TbItemCat> list = tbItemCatMapper.selectByExample(example);
        //创捷叶子节点集合
        List catNodes = new ArrayList();
        //定义计数器 count
        int count = 0;
        //遍历父级菜单集合list
        for (TbItemCat cat : list) {
            CatNode catNode = new CatNode();
            if (cat.getIsParent()) {
                //如果当前节点是父节点 将当前节点加入叶子节点对象 并遍历当前父节点下的子节点
                catNode.setName(cat.getName());
                catNode.setItem(getCatList(cat.getId()));
                catNodes.add(catNode);
                //计数器自增1
                count++;
            }
            //如果数量到达18
            if (count == 18) {
                //  中断当前循环
                break;
            } else {
                //  将当前节点加入叶子节点集合
                catNodes.add(cat.getName());  }
        }
        return catNodes;
    }
}
