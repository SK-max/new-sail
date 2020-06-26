package com.usian.service;


import com.usian.IDUtils;
import com.usian.MD5Utils;
import com.usian.mapper.TbUserMapper;
import com.usian.pojo.TbUser;
import com.usian.pojo.TbUserExample;
import com.usian.redis.RedisClient;
import jdk.nashorn.internal.parser.Token;
import org.bouncycastle.crypto.paddings.TBCPadding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class SSOServiceImpl implements SSOService {


    @Autowired
    private TbUserMapper tbUserMapper;

    @Autowired
    private RedisClient redisClient;

    @Value("${USER_INFO}")
    private String USER_INFO;

    @Value("${SESSION_EXPIRE}")
    private Long SESSION_EXPIRE;


    @Override
    public Boolean checkUserInfo(String checkValue, Integer checkFlag) {

        TbUserExample tbUserExample = new TbUserExample();
        TbUserExample.Criteria criteria = tbUserExample.createCriteria();
        //查询条件 根据checkflag 如果为1 是查询username 2 phone
        if (checkFlag == 1) {
            criteria.andUsernameEqualTo(checkValue);
        } else if (checkFlag == 2) {
            criteria.andPhoneEqualTo(checkValue);
        }
        List<TbUser> userList = tbUserMapper.selectByExample(tbUserExample);
        if (userList == null || userList.size() > 0) {
            return true;
        }
        return false;
    }

    /**
     * 注册方法
     *
     * @param tbUser
     * @return
     */
    @Override
    public Integer userRegister(TbUser tbUser) {

        String pwd = MD5Utils.digest(tbUser.getPassword().toString());
        tbUser.setPassword(pwd);
        Date date = new Date();
        tbUser.setCreated(date);
        tbUser.setUpdated(date);
        return tbUserMapper.insertSelective(tbUser);
    }

    @Override
    public Map userLogin(String username, String password) {
        //加密
        String pwd = MD5Utils.digest(password);
        //判断用户密码是否正确
        TbUserExample example = new TbUserExample();
        TbUserExample.Criteria criteria = example.createCriteria();
        criteria.andUsernameEqualTo(username);
        criteria.andPasswordEqualTo(pwd);
        List<TbUser> list = tbUserMapper.selectByExample(example);
        if (list == null || list.size() <= 0) {
            return null;
        }
        //登录成功后把user装到Redis 并设置失效时间
        TbUser tbUser = list.get(0);
        String token = UUID.randomUUID().toString();
        tbUser.setPassword(null);
        redisClient.set(USER_INFO + ":" + token, tbUser);
        redisClient.expire(USER_INFO + ":" + token, SESSION_EXPIRE);
        //返回结果
        HashMap<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("userid", tbUser.getId().toString());
        map.put("username", tbUser.getUsername());
        return map;
    }

    /**
     * 根据token获取登录状态
     *
     * @param token
     * @return
     */
    @Override
    public TbUser getUserByToken(String token) {
        TbUser tbUser = (TbUser) redisClient.get(USER_INFO + ":" + token);
        if (tbUser != null) {
            redisClient.expire(USER_INFO + ":" + token, SESSION_EXPIRE);
            return tbUser;
        }
        return null;
    }

    /**
     * 登出
     * @param token
     * @return
     */
    @Override
    public Boolean logOut(String token) {
        return redisClient.del(USER_INFO + ":" + token);
    }

}
