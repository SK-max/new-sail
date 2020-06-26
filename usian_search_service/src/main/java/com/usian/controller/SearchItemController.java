package com.usian.controller;



import com.usian.pojo.SearchItem;
import com.usian.service.SearchItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/service/searchItem")
public class SearchItemController {

    @Autowired
    private SearchItemService searchService;

    @RequestMapping("/importAll")
    public Boolean importAll() {

        return searchService.importAll();
    }

    @RequestMapping("/selectByQ")
    List<SearchItem> selectByQ(String q, Long page, Integer pageSize){
       return searchService.selectByQ(q,page,pageSize);
    };


}
