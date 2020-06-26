package com.usian.controller;


import com.usian.Result;
import com.usian.feign.ItemServiceFeign;
import com.usian.pojo.TbItem;
import com.usian.pojo.TbItemDesc;
import com.usian.pojo.TbItemParamItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/frontend/detail")
public class DetailController {

    @Autowired
    private ItemServiceFeign itemServiceFeign;

    /**
     * 查询商品基本信息并返回
     * @param itemId
     * @return
     */
    @RequestMapping("/selectItemInfo")
    public Result selectItemInfo(@RequestParam Long itemId){
        TbItem tbItem = itemServiceFeign.selectItemInfo(itemId);
        if(tbItem!=null){
            return Result.ok(tbItem);
        }
        return Result.error("查无结果");
    }

    /**
     * 查询商品详情并返回
     * @param itemId
     * @return
     */
    @RequestMapping("/selectItemDescByItemId")
    public Result selectItemDescByItemId(@RequestParam Long itemId){
       TbItemDesc itemDesc =itemServiceFeign.selectItemDescByItemId(itemId);
       if(itemDesc!=null){
           return Result.ok(itemDesc);
       }
        return Result.error("查无结果");
    }

    /**
     * 商品规格参数详情 并返回
     * @param itemId
     * @return
     */
    @RequestMapping("/selectTbItemParamItemByItemId")
    public Result selectTbItemParamItemByItemId(@RequestParam Long itemId){
     TbItemParamItem tbItemParamItem=itemServiceFeign.selectTbItemParamItemByItemId(itemId);
     if(tbItemParamItem!=null){
         return Result.ok(tbItemParamItem);
     }
      return Result.error("查无结果");
    }

}
