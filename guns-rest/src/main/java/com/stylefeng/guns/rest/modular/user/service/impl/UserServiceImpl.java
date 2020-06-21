package com.stylefeng.guns.rest.modular.user.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.stylefeng.guns.core.constant.RedisConstants;
import com.stylefeng.guns.core.constant.SystemConstants;
import com.stylefeng.guns.core.exception.GunsException;
import com.stylefeng.guns.core.support.DateTimeKit;
import com.stylefeng.guns.core.support.StrKit;
import com.stylefeng.guns.core.util.*;
import com.stylefeng.guns.core.util.redis.RedisService;
import com.stylefeng.guns.persistence.dao.*;
import com.stylefeng.guns.persistence.model.*;
import com.stylefeng.guns.rest.constants.Biz;
import com.stylefeng.guns.rest.core.util.BaseJson;
import com.stylefeng.guns.rest.modular.user.service.IUserService;
import com.stylefent.guns.entity.query.*;
import com.stylefent.guns.entity.vo.AccountVo;
import com.stylefent.guns.entity.vo.UpdateUserInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author lx
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IService<User>, IUserService {

    /**
     * 邀请注册地址
     */
    private static final String INVITE_REG_URL_TEST = "http://api.douniu.nhys.cdnhxx.com/invite?u=";
    private static final String INVITE_REG_URL_PROD = "https://api.douniuv.com/invite?u=";

    private final static Object syncLock = new Object();


    @Value("${rest.env-product}")
    private Boolean env;


    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisService redisService;

    @Autowired
    private AttentionMapper attentionMapper;

    @Override
    public boolean checkUser(User dbUser) {
        if (null == dbUser) return false;
        if (dbUser.getState() != 0) return false;
        return true;
    }

    @Override
    public User getFromCache(Long userId) {
        String userKey = SystemConstants.USER_REDIS_KEY + userId;
        Object userCache = redisService.getFromCache(userKey);
        if (userCache != null) {
            return (User) userCache;
        } else {
            User user = userMapper.selectById(userId);
            if (user != null) {
                redisService.toCache(userKey, user);
            }
            return user;
        }
    }

    @Override
    public void reloadUserToCache(Long userId) {
        String userKey = SystemConstants.USER_REDIS_KEY + userId;
        User user = userMapper.selectById(userId);
        if (user != null) {
            redisService.toCache(userKey, user);
        }
    }

    @Override
    public BaseJson changePwd(AccountVo accountVo) {
        Long userId = accountVo.getUserId();
        String pwd = accountVo.getPassword();
        if (StrKit.isEmpty(pwd)) {
            return BaseJson.ofFail("请输入支付密码");
        }
        User dbUser = userMapper.selectById(userId);
        if (!checkUser(dbUser)) {
            return BaseJson.userNotExist();
        }
        String phone = dbUser.getPhone();
        Object o = redisService.get(SystemConstants.SMS_FIND_FIND_SET_PASS_KEY.concat(phone));
        if (null == o) return BaseJson.ofFail("验证不通过,请重新获取验证码");

        redisService.remove(SystemConstants.SMS_FIND_FIND_SET_PASS_KEY.concat(phone));

        String enPwd = MD5Password.md5(pwd);
        User update = new User();
        update.setId(userId);
        update.setPayPwd(enPwd);
        update.setUpdateTime(new Date());
        update.setVersion(dbUser.getVersion());
        userMapper.updateById(update);

        reloadUserToCache(userId);
        return BaseJson.ofSuccess("修改成功");
    }



    @Override
    public BaseJson payPwdValidate(CommonQuery query) {
        Long userId = query.getUserId();
        String pwd = query.getKeyword();

        User dbUser = this.userMapper.selectById(userId);
        if (!this.checkUser(dbUser)) return BaseJson.userNotExist();

        if (StrKit.isEmpty(pwd)) {
            return BaseJson.ofFail("请输入支付密码");
        }
        if (StrKit.isEmpty(dbUser.getPayPwd())) {
            return BaseJson.ofFail(Biz.SET_PAY_PWD);
        }

        pwd = MD5Password.md5(pwd);
        if (!pwd.equals(dbUser.getPayPwd())) {
            return BaseJson.ofFail("密码错误");
        }
        redisService.set(SystemConstants.PAY_PWD_REDIS_KEY_PREFIX_.concat(userId.toString()), 1, 120L);
        return BaseJson.ofSuccess("验证通过");
    }

    @Override
    public BaseJson userInfo(Long userId) {
        return BaseJson.ofSuccess();
    }


    @Override
    public BaseJson checkUserInfo(Long othorUid,Long userId) {
        User rlbUser = selectById(othorUid);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Map userInfo = MapUtil.build()
                .put("userId", rlbUser.getId())
                .put("name", rlbUser.getName())
                .put("applyStatus", rlbUser.getApplyStatus())
                .put("gender", rlbUser.getGender())
                .put("age", rlbUser.getAge() != null ? rlbUser.getAge() : 0)
                .put("rlbNum", rlbUser.getXishiNum())
                .put("address", rlbUser.getProvince() + "-" + rlbUser.getCity())
                .put("intro", rlbUser.getIntro())
                .put("avatar", rlbUser.getAvatar())
                .put("onlineState", rlbUser.getOnlineState())
                .put("followed",0)
                .over();
        return BaseJson.ofSuccess(userInfo);
    }

    @Override
    public BaseJson updateUserInfo(UpdateUserInfoVo updateUserInfoVo) {
        User user = this.selectById(updateUserInfoVo.getUserId());
        if(!this.checkUser(user))return BaseJson.ofFail(Biz.USER_NOT_EXIST);
        BeanUtils.copyProperties(updateUserInfoVo,user);
        user.setBirthday(DateUtil.parseDate(updateUserInfoVo.getBirthday()));
        boolean result = updateById(user);
        reloadUserToCache(user.getId());
        return result ? BaseJson.ofSuccess("修改成功") : BaseJson.ofFail("修改失败,请稍后再试");
    }

    @Override
    public BaseJson otherUserIndexInfo(CommonQuery query) {
        Long userId = query.getUserId();
        Long linkId = query.getLinkId();

        User linkUser = userMapper.selectById(linkId);
        if (!checkUser(linkUser)) {
            return BaseJson.ofFail("该用户不存在或已被封禁");
        }
        return BaseJson.ofSuccess();
    }

    @Override
    public BaseJson addAttention(CommonQuery query) {
        EntityWrapper entityWrapper = new EntityWrapper<Attention>();
        entityWrapper.eq("user_id", query.getUserId());
        entityWrapper.eq("link_id", query.getLinkId());
        entityWrapper.eq("type", query.getType());
        Integer integer = this.checkAttention(query,entityWrapper);
        if (integer > 0) {
            return BaseJson.ofFail("已关注此人");
        }else {
            Attention attention = new Attention();
            attention.setLinkId(query.getLinkId());
            attention.setUserId(query.getUserId());
            attention.setCreateTime(new Date());
            attentionMapper.insert(attention);
            return BaseJson.ofSuccess("关注成功");
        }
    }

    @Override
    public BaseJson cancelAttention(CommonQuery query) {
        EntityWrapper entityWrapper = new EntityWrapper<Attention>();
        entityWrapper.eq("user_id", query.getUserId());
        entityWrapper.eq("link_id", query.getLinkId());
        entityWrapper.eq("type", query.getType());
        Integer integer = this.checkAttention(query,entityWrapper);
        if (integer > 0) {
            attentionMapper.delete(entityWrapper);
            return BaseJson.ofSuccess("取消关注成功");
        }else {
            return BaseJson.ofFail("未关注此人");
        }
    }

    public Integer checkAttention(CommonQuery query,EntityWrapper entityWrapper){
        Long userId = query.getUserId();
        Long linkId = query.getLinkId();
        if(userId == linkId){
            throw new GunsException(513,"不能关注自己");
        }
        return attentionMapper.selectCount(entityWrapper);
    }

    @Override
    public BaseJson selectWalletIndex(Long userId) {
//        UserWallet userWallet = userWalletMapper.selectByUserId(userId);
        return BaseJson.ofSuccess();
    }

    @Autowired
    private GiftMapper giftMapper;
    @Override
    public BaseJson getGiftList() {
        Object o = redisService.get(RedisConstants.GIFT_LIST);
        if(Objects.isNull(o)){
            List<Gift> gifts = giftMapper.selectList(new EntityWrapper<Gift>().eq("state",0));
            redisService.set(RedisConstants.GIFT_LIST,gifts);
            return BaseJson.ofSuccess(gifts);
        }else {
            return BaseJson.ofSuccess(o);
        }
    }

    @Override
    public BaseJson userIndexInfo(Long userId) {
        return null;
    }

    @Override
    public void userReport(CommonQuery query) {

    }

    @Override
    public BaseJson validatePwd(AccountVo accountVo) {
        return BaseJson.ofSuccess();
    }

}
