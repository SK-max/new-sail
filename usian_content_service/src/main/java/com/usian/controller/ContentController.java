package com.usian.controller;


import com.usian.AdNode;
import com.usian.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/service/content")
public class ContentController {

    @Autowired
    private ContentService contentService;


    @RequestMapping("/selectFrontendContentByAD")
    public List<AdNode> selectFrontendContentByAD(){
        return contentService.selectFrontendContentByAD();
    }


}
