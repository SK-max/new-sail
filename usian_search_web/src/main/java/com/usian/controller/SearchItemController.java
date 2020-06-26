package com.usian.controller;


import com.usian.Result;
import com.usian.feign.SearchFeign;
import com.usian.pojo.SearchItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/frontend/searchItem")
public class SearchItemController {

    @Autowired
    private SearchFeign searchFeign;

    /**
     * 索引导入
     * @return
     */
    @RequestMapping("/importAll")
    public Result importAll() {
        try {
            Boolean b = searchFeign.importAll();
            return Result.ok(200);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("失败了");
        }
    }

    /**
     * 搜索展示
     * @param q
     * @param page
     * @param pageSize
     * @return
     */
    @RequestMapping("/list")
    public List<SearchItem> selectByQ(String q, @RequestParam(defaultValue = "1")Long page,
                                                @RequestParam(defaultValue = "20")Integer pageSize){
        return searchFeign.selectByQ(q,page,pageSize);
    }


}
