package com.usian.controller;


import com.usian.PageResult;
import com.usian.Result;
import com.usian.feign.ItemServiceFeign;
import com.usian.pojo.PreItemVo;
import com.usian.pojo.TbItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/backend/item")
public class ItemController {

    @Autowired
    private ItemServiceFeign itemServiceFeign;

    @RequestMapping("/selectItemInfo")
    public Result selectItemInfo(Long itemId) {
        TbItem tbItem = itemServiceFeign.selectItemInfo(itemId);
        if (tbItem != null) {

            return Result.ok(tbItem);
        }
        return Result.error("查无结果");
    }

    @RequestMapping("/selectTbItemAllByPage")
    public Result selectTbItemAllByPage(@RequestParam(defaultValue = "1") Integer page,
                                        @RequestParam(defaultValue = "2") Integer rows) {
        PageResult pageResult = itemServiceFeign.selectTbItemAllByPage(page, rows);
        if (pageResult.getResult() != null && pageResult.getResult().size() > 0) {
            return Result.ok(pageResult);
        }
        return Result.error("查无结果");

    }

    /**
     * 删除类别
     *
     * @param itemId
     * @return
     */
    @RequestMapping("/deleteItemById")
    public Result deleteItemById(@RequestParam Long itemId) {
        try {
            itemServiceFeign.deleteItemById(itemId);
            return Result.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("删除失败");
        }
    }

    /**
     * 预更新查询
     *
     * @param itemId
     * @return
     */
    @RequestMapping("/preUpdateItem")
    public Result preUpdateItem(@RequestParam Long itemId) {
        try {
            PreItemVo preItemVo = itemServiceFeign.preUpdateItem(itemId);

            if (preItemVo != null) {
                //调用查询方法
                return Result.ok(preItemVo);
            } else {
                return Result.error("预更新失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("预更新失败");
        }
    }

    /**
     * 修改方法
     * @param tbItem
     * @param desc
     * @param itemParams
     * @return
     */
    @RequestMapping("/updateTbItem")
    public Result itemServiceFeign(TbItem tbItem, String desc, String itemParams) {
        try {
            itemServiceFeign.updateTbItem(tbItem,desc,itemParams);
            return Result.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("修改失败");
        }
    }

    @RequestMapping("/insertTbItem")
    public Result insertTbItem( TbItem tbItem,String desc,String itemParams){
        try {
            itemServiceFeign.insertTbItem(tbItem,desc,itemParams);
            return Result.ok();
        }catch (Exception e){
            e.printStackTrace();
            return Result.error("添加错误");
        }
    }
}
