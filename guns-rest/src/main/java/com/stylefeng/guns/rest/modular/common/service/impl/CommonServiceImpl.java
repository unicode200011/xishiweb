package com.stylefeng.guns.rest.modular.common.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.github.pagehelper.PageHelper;
import com.stylefeng.guns.core.util.redis.RedisService;
import com.stylefeng.guns.persistence.dao.CommonDataMapper;
import com.stylefeng.guns.persistence.dao.GiftMapper;
import com.stylefeng.guns.persistence.dao.UserMapper;
import com.stylefeng.guns.persistence.model.CommonData;
import com.stylefeng.guns.persistence.model.Gift;
import com.stylefeng.guns.persistence.model.User;
import com.stylefeng.guns.rest.core.util.BaseJson;
import com.stylefeng.guns.rest.modular.common.service.ICommonService;
import com.stylefeng.guns.rest.modular.user.service.IUserService;
import com.stylefent.guns.entity.query.CommonQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author: lx
 */
@Slf4j
@Service
public class CommonServiceImpl implements ICommonService {

    @Value("${rest.env-product}")
    private Boolean env;



    @Autowired
    private UserMapper userMapper;

    @Autowired
    private GiftMapper giftMapper;

    @Autowired
    private IUserService userService;

    @Autowired
    private RedisService redisService;


    @Autowired
    private CommonDataMapper commonDataMapper;


    @Override
    public CommonData findCommonDataById(Long id) {
        return this.commonDataMapper.selectById(id);
    }

    @Override
    public String findCommonDataFromCache(Long id) {
        CommonData commonParam = findCommonDataById(id);
        String commonParamStr = commonParam.getValue();
        return commonParamStr;
    }

    @Override
    public CommonData findCommonDataByKT(String key, String type) {
        CommonData query = new CommonData();
        query.setDataKey(key);
        query.setType(type);
        return this.commonDataMapper.selectOne(query);
    }

    @Override
    public BaseJson search(CommonQuery query) {
        return null;
    }

    @Override
    public BaseJson helpCenter(CommonQuery query) {
        return null;
    }


    @Override
    public BaseJson gifts(CommonQuery query) {
        PageHelper.startPage(query.getPage(), query.getRows());
        List<Gift> gifts = giftMapper.selectList(new EntityWrapper<Gift>().orderDesc(Arrays.asList("num", "money", "id")));
        return BaseJson.ofSuccess(gifts);
    }

    @Override
    public BaseJson richList(CommonQuery query) {
        return null;
    }

    @Override
    public BaseJson getWithRate() {
        return null;
    }


}
