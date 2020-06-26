package com.usian.controller;


import com.usian.Result;

import com.usian.feign.SSOServiceFeign;
import com.usian.pojo.TbUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/frontend/sso")
public class SSOController {


    @Autowired
    private SSOServiceFeign ssoServiceFeign;

    @RequestMapping("/checkUserInfo/{checkValue}/{checkFlag}")
    public Result checkUserInfo(@PathVariable String checkValue, @PathVariable Integer checkFlag) {
        Boolean flag = ssoServiceFeign.checkUserInfo(checkValue, checkFlag);
        if (!flag) {
            return Result.ok();
        }
        return Result.error("登录失败");
    }

    @RequestMapping("/userRegister")
    public Result userRegister(TbUser tbUser) {
        Integer userRegister = ssoServiceFeign.userRegister(tbUser);
        if (userRegister == 1) {
            return Result.ok(200);
        }
        return Result.error("注册失败");
    }

    @RequestMapping("/userLogin")
    public Result userLogin(String username, String password) {
        Map map = ssoServiceFeign.userLogin(username, password);
        if (map != null) {
            return Result.ok(map);
        }
        return Result.error("查无结果");
    }

    @RequestMapping("/getUserByToken/{token}")
    public Result getUserByToken(@PathVariable String token) {
        TbUser tbUser = ssoServiceFeign.getUserByToken(token);
        if (tbUser != null) {
            return Result.ok();
        }
        return null;
    }

    @RequestMapping("/logOut")
    public Result logOut(String token) {
        Boolean logOuts = ssoServiceFeign.logOut(token);
        if (logOuts) {
            return Result.ok();
        }
        return Result.error("退出失败");
    }


}
