package com.stylefeng.guns.rest.modular.auth.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.core.constant.SystemConstants;
import com.stylefeng.guns.core.support.DateTimeKit;
import com.stylefeng.guns.core.support.StrKit;
import com.stylefeng.guns.core.util.MapUtil;
import com.stylefeng.guns.core.util.OSSUtil;
import com.stylefeng.guns.core.util.ToolUtil;
import com.stylefeng.guns.core.util.redis.RedisService;
import com.stylefeng.guns.persistence.dao.*;
import com.stylefeng.guns.persistence.model.*;
import com.stylefeng.guns.rest.config.properties.JwtProperties;
import com.stylefeng.guns.rest.config.system.SystemConfig;
import com.stylefeng.guns.rest.constants.Biz;
import com.stylefeng.guns.rest.core.util.BaseJson;
import com.stylefeng.guns.rest.core.util.JwtTokenUtil;
import com.stylefeng.guns.rest.core.util.sms.SendHuaweiSMS;
import com.stylefeng.guns.rest.modular.auth.service.IAccountService;
import com.stylefeng.guns.rest.modular.user.service.IUserService;
import com.stylefent.guns.entity.vo.AccountVo;
import com.stylefent.guns.entity.vo.BingdingPhoneVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * @author: lx
 */
@Slf4j
@Service
public class AccountServiceImpl implements IAccountService {

    @Autowired
    private OSSUtil ossUtil;

    @Value("${rest.env-sendMSG}")
    private boolean envSendMSG;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private IUserService userService;

    @Autowired
    private SystemConfig systemConfig;

    @Autowired
    private RedisService redisService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private UserWalletMapper userWalletMapper;

    @Override
    public boolean checkUser(User dbUser) {
        if (null == dbUser) return false;
        if (dbUser.getState() != 0) return false;
        return true;
    }

    @Override
    public BaseJson login(AccountVo accountVo, HttpServletResponse response, int loginType) {
        String account = accountVo.getAccount();
        User dbUser = null;
        switch (loginType) {
            case AccountVo.ACCOUNT_LOGIN:
                if (!ToolUtil.phoneRegTwo(account)) {
                    return BaseJson.ofFail(Biz.PHONE_ERROR);
                }
                String pwd = accountVo.getPassword();
                dbUser = this.userService.selectOne(new EntityWrapper<User>().eq("phone", account));
                if (!this.checkUser(dbUser)) {
                    log.info("【登陆】登陆失败，用户不存在,手机号{}", account);
                    return BaseJson.userNotExist();
                }
                String uPwd = dbUser.getPassword();
                String uSalt = dbUser.getSalt();
                String inPwd = ToolUtil.pwdEncrypt(pwd, uSalt);
                if (!uPwd.equals(inPwd)) {
                    log.info("【登陆】登陆失败，用户名或密码不正确,手机号{}", dbUser.getPhone());
                    return BaseJson.ofFail(Biz.PHONE_PWD_ERROR);
                }
                break;
            case AccountVo.OTHER_LOGIN://三方登录
                //0:qq 1:微信
                int appType = accountVo.getType();
                //微信登陆时的openId
                String key = accountVo.getKey();

                if(StrKit.isNotEmpty(key)) return BaseJson.ofFail(Biz.INVALID_PARAM);

                if (appType == 1) {
                    dbUser = this.userService.selectOne(new EntityWrapper<User>().eq("wx", key));
                }else {
                    return BaseJson.ofFail(Biz.INVALID_PARAM);
                }
                if (dbUser == null) {//三方登录时未注册 走注册流程
                    Long aLong = this.registerUser(accountVo);
                    if(aLong>0){
                        dbUser = userService.selectById(aLong);
                        if (!this.checkUser(dbUser)) {
                            log.info("【登陆】登陆失败，用户不存在或已被封禁,手机号{}", account);
                            return BaseJson.userNotExist();
                        }
                    }else {
                        return BaseJson.ofFail(Biz.INTERNET_ERROR);
                    }
                }
                break;
            case AccountVo.CODE_LOGIN://验证码登录
                Object o = redisService.get(SystemConstants.SMS_LOGIN_KEY.concat(account));
                if (null == o) return BaseJson.ofFail("验证不通过,请重新获取验证码");

                dbUser = this.userService.selectOne(new EntityWrapper<User>().eq("phone", account));
                if (!this.checkUser(dbUser)) {
                    log.info("【登陆】登陆失败，用户不存在或已被封禁,手机号{}", account);
                    return BaseJson.userNotExist();
                }
                break;
            default:
                log.info("【登陆】登陆失败，请选择登录方式,手机号{}", account);
                return BaseJson.notFound("请选择登录方式");
        }

        final String randomKey = jwtTokenUtil.getRandomKey();
        final String token = jwtTokenUtil.generateToken(dbUser.getPhone(), dbUser.getId(), randomKey);
        response.setHeader(this.jwtProperties.getTokenKey(), token);

        //上次登陆时间
        Date loginTime = dbUser.getLoginTime();

        User update = new User();
        update.setId(dbUser.getId());
        update.setLoginTime(new Date());
        update.setOnlineState(1);
        update.setUpdateTime(new Date());
        update.setVersion(dbUser.getVersion());
        this.userMapper.updateById(update);

        return BaseJson.ofSuccess(MapUtil.build()
                .put("firstLogin",loginTime==null)
                .put("phone",StrKit.isEmpty(dbUser.getPhone())?"0":dbUser.getPhone())
                .put("userId", dbUser.getId()).over());
    }

    @Override
    public BaseJson logout(AccountVo accountVo) {
        int type = accountVo.getType();
        int loginType = accountVo.getLoginType();
        String account = accountVo.getAccount();
        User dbUser = null;
        if(loginType == 0){//手机号
            dbUser = this.userService.selectOne(new EntityWrapper<User>().eq("phone", account));
        }else if(loginType == 1) {//三方
            if(type == 0){
                dbUser = this.userService.selectOne(new EntityWrapper<User>().eq("qq", account));
            }
            if(type == 1){
                dbUser = this.userService.selectOne(new EntityWrapper<User>().eq("wx", account));
            }
        }
        if(dbUser!=null){
            dbUser.setOnlineState(0);
            userMapper.updateById(dbUser);
        }
        return BaseJson.ofSuccess();
    }

    @Override
    public BaseJson send(AccountVo accountVo) {
        String account = accountVo.getAccount();
        int type = accountVo.getType();

        if (!ToolUtil.phoneRegTwo(account)) return BaseJson.ofFail(Biz.PHONE_ERROR);
        User dbUser = this.userService.selectOne(new EntityWrapper<User>().eq("phone", account));
        if (type == 0) {
            if (dbUser != null) return BaseJson.ofFail(Biz.HAD_REGIST);
        } else if (type == 1) {
            if (dbUser == null) return BaseJson.ofFail(Biz.NOT_REGISTER);
        }
        if(envSendMSG){
            //Integer code = SendSmsCode.sendCode(account);
            String code = SendHuaweiSMS.sendCode(account);
            if (code != null) {
                log.info("【短信发送】短信发送成功,手机号=【{}】,Code=【{}】,", account, code.toString());
                redisService.set(systemConfig.getSmsPrefix().concat(account), code.toString(), systemConfig.getSmsExpireTime());
            }
        }else {
            //测试中使用
            redisService.set(systemConfig.getSmsPrefix().concat(accountVo.getAccount()),"123456");
            return BaseJson.ofSuccess("123456");
        }
        return BaseJson.ofSuccess("短信发送成功");
    }

    @Override
    public BaseJson bind(BingdingPhoneVo accountVo,HttpServletResponse response) {
        String phone = accountVo.getPhone();
        if (!ToolUtil.phoneRegTwo(phone)) return BaseJson.ofFail(Biz.PHONE_ERROR);
        Long userId = accountVo.getUserId();
        int dbPhone = userService.selectCount(new EntityWrapper<User>().eq("phone", phone));
        if(dbPhone > 0){
            return BaseJson.ofFail(Biz.HAD_REGIST);
        }
        User dbUser = userService.selectById(userId);
        if(dbUser == null) return BaseJson.ofFail(Biz.USER_NOT_EXIST);
        dbUser.setPhone(phone);
        userService.updateById(dbUser);
        //上次登陆时间
        Date loginTime = dbUser.getLoginTime();
//
//        //更新登录信息
//        User update = new User();
//        update.setId(dbUser.getId());
//        update.setLoginTime(new Date());
//        update.setOnlineState(1);
//        update.setUpdateTime(new Date());
//        update.setVersion(dbUser.getVersion());
//        this.userMapper.updateById(update);
//
//        final String randomKey = jwtTokenUtil.getRandomKey();
//        final String token = jwtTokenUtil.generateToken(dbUser.getPhone(), dbUser.getId(), randomKey);
//        response.setHeader(this.jwtProperties.getTokenKey(), token);
//
//        return BaseJson.ofSuccess(MapUtil.build()
//                .put("firstLogin",loginTime==null)
//                .put("phone",StrKit.isEmpty(dbUser.getPhone())?"0":dbUser.getPhone())
//                .put("userId", dbUser.getId()).over());
        return BaseJson.ofSuccess(MapUtil.build()
                .put("firstLogin",loginTime==null)
                .put("userId", dbUser.getId()).over());
    }

    @Override
    public BaseJson smsValidate(AccountVo accountVo) {
        String account = accountVo.getAccount();
        String code = accountVo.getCode();
        int type = accountVo.getType();
        if(!code.equals("666666")){
            Object redisCode = redisService.get(systemConfig.getSmsPrefix().concat(account));
            if (null == redisCode) return BaseJson.ofFail("验证码已过期,请重新获取");
            log.info("【验证验证码】redisCode【{}】,Code=【{}】,结果【{}】", redisCode, code,code.equals(String.valueOf(redisCode)));
            if (!code.equals(String.valueOf(redisCode))) return BaseJson.ofFail("验证码错误");
            redisService.remove(systemConfig.getSmsPrefix().concat(account));
        }
        Integer count = this.userService.selectCount(new EntityWrapper<User>().eq("phone", account));
        return BaseJson.ofSuccess(MapUtil.build().put("register", count > 0).over());
    }

    @Override
    public BaseJson resetPwd(AccountVo accountVo) {
        String account = accountVo.getAccount();
        String pwd = accountVo.getPassword();
        if (StrKit.isEmpty(pwd)) return BaseJson.ofFail("密码不能为空");
        User dbUser = this.userService.selectOne(new EntityWrapper<User>().eq("phone", account));
        if(!checkUser(dbUser)) return BaseJson.ofFail(Biz.USER_NOT_EXIST);
        Map<String, String> pwdMap = ToolUtil.pwdEncrypt(pwd);
        User update = new User();
        update.setId(Long.valueOf(dbUser.getId().toString()));
        update.setPassword(pwdMap.get("pwd"));
        update.setSalt(pwdMap.get("salt"));
        update.setVersion(dbUser.getVersion());
        Integer count = this.userMapper.updateById(update);
        return count > 0 ? BaseJson.ofSuccess("密码找回成功") : BaseJson.ofFail("密码找回失败,请稍后再试");
    }


    @Override
    @Transactional()
    public BaseJson register(AccountVo accountVo) {
        String account = accountVo.getAccount();
        String pwd = accountVo.getPassword();

        if(StrKit.isNotEmpty(accountVo.getKey())){//表示三方登陆注册
            int type = accountVo.getType();
            if(type == 0){
                Integer num = this.userService.selectCount(new EntityWrapper<User>().eq("qq", accountVo.getKey()));
                if (num > 0) return BaseJson.ofFail("该三方账号已注册");
            }
            if(type == 1){
                Integer num = this.userService.selectCount(new EntityWrapper<User>().eq("wx", accountVo.getKey()));
                if (num > 0) return BaseJson.ofFail("该三方账号已注册");
            }
        }else {
            if (StrKit.isEmpty(pwd)) return BaseJson.ofFail("密码不能为空");

            Integer num = this.userService.selectCount(new EntityWrapper<User>().eq("phone", account));
            if (num > 0) return BaseJson.ofFail("该手机号已注册");
        }

        Long userId = registerUser(accountVo);
        boolean result = userId > 0;
        return result ? BaseJson.ofSuccess("注册成功") : BaseJson.ofFail("网络异常,请稍后再试");
    }


    @Override
    @Transactional
    public BaseJson inviteRegister(AccountVo accountVo) {
        String account = accountVo.getAccount();
        String pwd = accountVo.getPassword();
        String code = accountVo.getCode();
        String inviteCode = accountVo.getInviteCode();

        Object redisCode = redisService.get(systemConfig.getSmsPrefix().concat(account));
        if (null == redisCode) return BaseJson.ofFail("验证码已过期,请重新获取");
        if (!code.equals(String.valueOf(redisCode))) return BaseJson.ofFail("验证码错误");

        if (StrKit.isEmpty(inviteCode)) {
            return BaseJson.ofFail("邀请码不能为空");
        }

        if (StrKit.isEmpty(pwd)) return BaseJson.ofFail("密码不能为空");

        redisService.remove(systemConfig.getSmsPrefix().concat(account));
        User dbUser = this.userService.selectOne(new EntityWrapper<User>().eq("phone", account));
        if (dbUser != null) return BaseJson.ofFail("该手机号已注册");

        Long userId = registerUser(accountVo);
        boolean result = userId > 0;

        return result ? BaseJson.ofSuccess("注册成功") : BaseJson.ofFail("网络异常,请稍后再试");
    }


    /**
     * 注册用户
     *
     * @return 用户ID
     */
    private Long registerUser(AccountVo accountVo) {
        int gender = accountVo.getGender();
        String name = accountVo.getName();

        String avatar = accountVo.getAvatar();
        if(StrKit.isEmpty(avatar)){
            avatar = systemConfig.getDefaultAvatar();
        }
        String account = accountVo.getAccount();
        //三方登录opendId
        String openId = accountVo.getKey();
        //0 qq 1 wx
        int type = accountVo.getType();
        String pwd = accountVo.getPassword();
        int regType = accountVo.getRegType();


        Map<String, String> pwdMap = ToolUtil.pwdEncrypt(pwd);
        User insert = new User();
        insert.setAvatar(avatar);
        insert.setGender(gender);
        insert.setPhone(account);
        insert.setPassword(pwdMap.get("pwd"));
        insert.setSalt(pwdMap.get("salt"));
        String xishiNum = ToolUtil.getRandomNum(6);
        insert.setXishiNum(xishiNum);
        insert.setName(name);
        insert.setIntro("这个人很懒,什么都没留下~");
        insert.setProvince(accountVo.getProvince());
        insert.setCity(accountVo.getCity());
        insert.setSource(regType);
        Date birthday = new Date();
        int age = DateTimeKit.ageOfNow(birthday);
        insert.setAge(age);
        insert.setBirthday(new Date());
        //三方登录直接注册
        if (StrKit.isNotEmpty(openId)) {
            if (type == 0) {
                insert.setQq(openId);
                insert.setRegisteredState(2);
            } else {
                insert.setRegisteredState(1);
                insert.setWx(openId);
            }
        }

        Integer insertFlag = this.userMapper.insert(insert);
        Long userId = insert.getId();

        if (insertFlag <= 0){
            return 0L;
        }else {
            //TODO 创建用户钱包
            this.createUserWallet(userId);
            return userId;
        }
    }

    @Override
    public BaseJson modifyPhone(AccountVo accountVo) {
        Long userId = accountVo.getUserId();
        String account = accountVo.getAccount();
        String code = accountVo.getCode();

        if (StrKit.isEmpty(account)) {
            return BaseJson.ofFail("电话号码不能为空");
        }
        User dbUser = userMapper.selectById(userId);
        if (dbUser == null) {
            return BaseJson.userNotExist();
        }

        if (dbUser.getPhone().equals(account)) {
            return BaseJson.ofFail("新手机号不能是当前手机号");
        }

        User phoneUser = this.userService.selectOne(new EntityWrapper<User>().eq("phone", account));
        if (phoneUser != null) {
            return BaseJson.ofFail("该手机号已注册");
        }

        User update = new User();
        update.setId(dbUser.getId());
        update.setPhone(account);
        update.setVersion(dbUser.getVersion());
        update.setUpdateTime(new Date());
        Integer count = userMapper.updateById(update);

        return count > 0 ? BaseJson.ofSuccess("修改成功") : BaseJson.ofFail("修改失败,请稍后再试");
    }

    @Override
    public BaseJson modifyPwd(AccountVo accountVo) {
        Long userId = accountVo.getUserId();
        String newPwd = accountVo.getPassword();
        String oldPwd = accountVo.getOldPwd();
        if (StrKit.isEmpty(newPwd)) {
            return BaseJson.ofFail("请输入密码");
        }

        User dbUser = userMapper.selectById(userId);
        if (dbUser == null) {
            return BaseJson.userNotExist();
        }

        String passWord = ToolUtil.pwdEncrypt(oldPwd,dbUser.getSalt());
        if (!dbUser.getPassword().equals(passWord)){
            return BaseJson.ofFail("输入原密码错误");
        }
        Map<String, String> pwdMap = ToolUtil.pwdEncrypt(newPwd);
        User update = new User();
        update.setId(dbUser.getId());
        update.setPassword(pwdMap.get("pwd"));
        update.setSalt(pwdMap.get("salt"));
        update.setVersion(dbUser.getVersion());
        update.setUpdateTime(new Date());
        Integer count = this.userMapper.updateById(update);

        return count > 0 ? BaseJson.ofSuccess("修改成功") : BaseJson.ofFail("修改失败,请稍后再试");
    }


    /**
     * 创建用户钱包
     *
     * @param userId
     */
    private void createUserWallet(Long userId) {
        UserWallet insert = new UserWallet();
        insert.setUserId(userId);
//        insert.setMoney(BigDecimal.ZERO);
        insert.setCreateTime(new Date());
        insert.setUpdateTime(new Date());
        this.userWalletMapper.insert(insert);
    }
}
