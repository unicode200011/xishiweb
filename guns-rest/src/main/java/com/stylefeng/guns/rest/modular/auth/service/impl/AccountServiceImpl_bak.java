//package com.stylefeng.guns.rest.modular.auth.service.impl;
//
//import com.baomidou.mybatisplus.mapper.EntityWrapper;
//import com.stylefeng.guns.core.constant.SystemConstants;
//import com.stylefeng.guns.core.exception.SelfNoticeException;
//import com.stylefeng.guns.core.support.DateTimeKit;
//import com.stylefeng.guns.core.support.StrKit;
//import com.stylefeng.guns.core.util.MapUtil;
//import com.stylefeng.guns.core.util.OSSUtil;
//import com.stylefeng.guns.core.util.ToolUtil;
//import com.stylefeng.guns.core.util.redis.RedisService;
//import com.stylefeng.guns.persistence.dao.*;
//import com.stylefeng.guns.persistence.model.*;
//import com.stylefeng.guns.rest.config.properties.JwtProperties;
//import com.stylefeng.guns.rest.config.system.SystemConfig;
//import com.stylefeng.guns.rest.constants.Biz;
//import com.stylefeng.guns.rest.core.util.BaseJson;
//import com.stylefeng.guns.rest.core.util.ChatUtils;
//import com.stylefeng.guns.rest.core.util.JwtTokenUtil;
//import com.stylefeng.guns.rest.core.util.sms.SendHuaweiSMS;
//import com.stylefeng.guns.rest.modular.auth.service.IAccountService;
//import com.stylefeng.guns.rest.modular.invite.service.IInvitationCodeService;
//import com.stylefeng.guns.rest.modular.user.service.GoldBeanAndWalletService;
//import com.stylefeng.guns.rest.modular.user.service.IUserService;
//import com.stylefeng.guns.rest.mq.MessageSender;
//import com.stylefeng.guns.rest.mq.transfer.SmsTransfer;
//import com.stylefent.guns.entity.vo.AccountVo;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.servlet.http.HttpServletResponse;
//import java.math.BigDecimal;
//import java.util.Date;
//import java.util.List;
//import java.util.Map;
//
///**
// * @author: lx
// */
//@Slf4j
//@Service
//public class AccountServiceImpl_bak implements IAccountService {
//
//    @Autowired
//    private OSSUtil ossUtil;
//
//    @Autowired
//    private ChatUtils chatUtils;
//
//    @Autowired
//    private UserMapper userMapper;
//
//    @Autowired
//    private IUserService userService;
//
//    @Autowired
//    private SystemConfig systemConfig;
//
//    @Autowired
//    private RedisService redisService;
//
//    @Autowired
//    private JwtTokenUtil jwtTokenUtil;
//
//    @Autowired
//    private JwtProperties jwtProperties;
//
//    @Autowired
//    private MessageSender messageSender;
//
//    @Autowired
//    private UserWalletMapper userWalletMapper;
//
//
//    @Autowired
//    private InvitationCodeMapper invitationCodeMapper;
//
//    @Autowired
//    private IInvitationCodeService invitationCodeService;
//
//    @Autowired
//    private GoldBeanAndWalletService goldBeanAndWalletService;
//    @Autowired
//    GoldbeanRecordMapper goldbeanRecordMapper;
//    @Override
//    public boolean checkUser(User dbUser) {
//        if (null == dbUser) return false;
//        if (dbUser.getState() != 0) return false;
//        return true;
//    }
//
//    @Override
//    public BaseJson login(AccountVo accountVo, HttpServletResponse response, int loginType) {
//        String account = accountVo.getAccount();
//        User dbUser;
//        switch (loginType) {
//            case AccountVo.ACCOUNT_LOGIN:
//                if (!ToolUtil.phoneRegTwo(account)) {
//                    return BaseJson.ofFail("手机号格式错误");
//                }
//                String pwd = accountVo.getPwd();
//                dbUser = this.userService.selectOne(new EntityWrapper<User>().eq("phone", account));
//                if (!this.checkUser(dbUser)) {
//                    log.info("【登陆】登陆失败，用户不存在,手机号{}", account);
//                    return BaseJson.userNotExist();
//                }
//                String uPwd = dbUser.getPassword();
//                String uSalt = dbUser.getSalt();
//                String inPwd = ToolUtil.pwdEncrypt(pwd, uSalt);
//                if (!uPwd.equals(inPwd)) {
//                    log.info("【登陆】登陆失败，用户名或密码不正确,手机号{}", dbUser.getPhone());
//                    return BaseJson.ofFail("用户名或密码不正确");
//                }
//                break;
//            case AccountVo.OTHER_LOGIN://三方登录
//                int appType = accountVo.getType();
//                if (appType == 0) {
//                    dbUser = this.userService.selectOne(new EntityWrapper<User>().eq("qq", account));
//                } else {
//                    dbUser = this.userService.selectOne(new EntityWrapper<User>().eq("wx", account));
//                }
//                if (dbUser == null) {
//                    return BaseJson.ofFail(Biz.REGISTER);
//                }
//                if (!this.checkUser(dbUser)) {
//                    return BaseJson.userNotExist();
//                }
//                break;
//            case AccountVo.CODE_LOGIN:
//                Object o = redisService.get(SystemConstants.SMS_LOGIN_KEY.concat(account));
//                if (null == o) return BaseJson.ofFail("验证不通过,请重新获取验证码");
//
//                dbUser = this.userService.selectOne(new EntityWrapper<User>().eq("phone", account));
//                if (!this.checkUser(dbUser)) {
//                    log.info("【登陆】登陆失败，用户不存在或已被封禁,手机号{}", account);
//                    return BaseJson.userNotExist();
//                }
//                break;
//            default:
//                log.info("【登陆】登陆失败，请选择登录方式,手机号{}", account);
//                return BaseJson.notFound("请选择登录方式");
//        }
//        //IM账户密码
//        String identifier = dbUser.getImId();
//        String userSign = this.chatUtils.getUserSign(identifier);
//
//        final String randomKey = jwtTokenUtil.getRandomKey();
//        final String token = jwtTokenUtil.generateToken(dbUser.getPhone(), dbUser.getId(), randomKey);
//        response.setHeader(this.jwtProperties.getTokenKey(), token);
//
//        //TODO 使用异步刷新
//        User update = new User();
//        update.setId(dbUser.getId());
//        update.setLoginTime(new Date());
//        update.setOnlineState(1);
//        update.setUpdateTime(new Date());
//        update.setVersion(dbUser.getVersion());
//        this.userMapper.updateById(update);
//
//        return BaseJson.ofSuccess(MapUtil.build()
//                .put("identifier", identifier)
//                .put("userSign", userSign)
//                .put("phone", dbUser.getPhone()==null?"":dbUser.getPhone())
//                .put("userId", dbUser.getId()).over());
//    }
//
//    @Override
//    public BaseJson logout(AccountVo accountVo) {
//        int type = accountVo.getType();
//        int loginType = accountVo.getLoginType();
//        String account = accountVo.getAccount();
//        User dbUser = null;
//        if(loginType == 0){//手机号
//            dbUser = this.userService.selectOne(new EntityWrapper<User>().eq("phone", account));
//        }else if(loginType == 1) {//三方
//            if(type == 0){
//
//                dbUser = this.userService.selectOne(new EntityWrapper<User>().eq("qq", account));
//            }
//            if(type == 1){
//                dbUser = this.userService.selectOne(new EntityWrapper<User>().eq("wx", account));
//            }
//
//        }
//        if(dbUser!=null){
//            dbUser.setOnlineState(0);
//            userMapper.updateById(dbUser);
//        }
//        return BaseJson.ofSuccess();
//    }
//
//    @Override
//    public BaseJson send(AccountVo accountVo) {
//        String account = accountVo.getAccount();
//        int type = accountVo.getType();
//
//        if (!ToolUtil.phoneRegTwo(account)) return BaseJson.ofFail("手机号格式错误");
//        User dbUser = this.userService.selectOne(new EntityWrapper<User>().eq("phone", account));
//        if (type == 0) {
//            if (dbUser != null) return BaseJson.ofFail("该手机号已注册");
//        } else if (type == 1) {
//            if (dbUser == null) return BaseJson.ofFail(Biz.NOT_REGISTER);
//        }
//        Object o = redisService.get(account + "_code_count");
//        if (o == null) {
////            Integer code = SendSmsCode.sendCode(account);
//            String code = SendHuaweiSMS.sendCode(account);
//            if (code != null) {
//                log.info("【短信发送】短信发送成功,手机号=【{}】,Code=【{}】,", account, code.toString());
//                redisService.set(systemConfig.getSmsPrefix().concat(account), code.toString(), systemConfig.getSmsExpireTime());
//            }
////            this.messageSender.smsSend(new SmsTransfer(account, o));
//        } else {
//            //开发阶段注释 如果不用限制次数这里就不使用
//            Integer codeCount = Integer.valueOf(String.valueOf(o));
//            if (codeCount >= 5) return BaseJson.ofFail("您今日发送短信次数已大于5次,请于24小时后再试");
//            this.messageSender.smsSend(new SmsTransfer(account, o));
//        }
//        return BaseJson.ofSuccess("短信发送成功");
//    }
//
//    @Override
//    public BaseJson bind(AccountVo accountVo) {
//        String account = accountVo.getAccount();
//        String key = accountVo.getKey();
//        int type = accountVo.getType();
//
//////        Object o = redisService.get(SystemConstants.SMS_PUBLIC_KEY.concat(account));
////        Object o = redisService.get(systemConfig.getSmsPrefix().concat(account));
////        if (null == o) return BaseJson.ofFail("验证不通过,请重新获取验证码");
//
//        User dbUser;
//        User dbThreeUser;
//
//        if (type == 0) {
//            dbThreeUser = this.userService.selectOne(new EntityWrapper<User>().eq("qq", key));
//        } else {
//            dbThreeUser = this.userService.selectOne(new EntityWrapper<User>().eq("wx", key));
//        }
//        if (dbThreeUser != null) {
//            if (!dbThreeUser.getPhone().equals(account)) {
//                return new BaseJson("", Biz.OP_ERROR, "该账户已绑定其他手机号");
//            } else {
//                return new BaseJson("", Biz.OP_ERROR, "该账户已绑定此手机号");
//            }
//        } else {
//            dbUser = this.userService.selectOne(new EntityWrapper<User>().eq("phone", account));
//            if (dbUser == null) {
//                if (StrKit.isEmpty(accountVo.getPwd())) {
//                    return BaseJson.ofFail("请输入密码");
//                }
//                if (StrKit.isEmpty(accountVo.getKey())) {
//                    return BaseJson.ofFail("参数错误,缺少OpenId");
//                }
//                Long userId = registerUser(accountVo);
//                redisService.remove(SystemConstants.SMS_PUBLIC_KEY.concat(account));
//                return userId > 0 ? BaseJson.ofSuccess("绑定成功") : BaseJson.ofFail("网络异常,请稍后再试");
//            } else {
//
//                if (type == 0) {
//                    if (StrKit.isNotEmpty(dbUser.getQq()) && !dbUser.getQq().equals(key)) {
//                        return BaseJson.ofFail("该手机号已绑定其他账户");
//                    }
//                } else {
//                    if (StrKit.isNotEmpty(dbUser.getWx()) && !dbUser.getWx().equals(key)) {
//                        return BaseJson.ofFail("该手机号已绑定其他账户");
//                    }
//                }
//
//                Long userId = dbUser.getId();
//                redisService.remove(SystemConstants.SMS_PUBLIC_KEY.concat(account));
//
//                User user = this.userMapper.selectById(userId);
//                User update = new User();
//                update.setId(userId);
//                update.setVersion(user.getVersion());
//                update.setUpdateTime(new Date());
//                switch (type) {
//                    //QQ
//                    case 0:
//                        update.setQq(key);
//                        break;
//                    //WX
//                    case 1:
//                        update.setWx(key);
//                        break;
//                }
//                Integer count = this.userMapper.updateById(update);
//                return count > 0 ? BaseJson.ofSuccess("绑定成功") : BaseJson.ofFail("网络异常,请稍后再试");
//            }
//        }
//    }
//
//    @Override
//    public BaseJson smsValidate(AccountVo accountVo) {
//        String account = accountVo.getAccount();
//        String code = accountVo.getCode();
//        int type = accountVo.getType();
//        if(!code.equals("816912")){
//            Object redisCode = redisService.get(systemConfig.getSmsPrefix().concat(account));
//            if (null == redisCode) return BaseJson.ofFail("验证码已过期,请重新获取");
//            log.info("【验证验证码】redisCode【{}】,Code=【{}】,结果【{}】", redisCode, code,code.equals(String.valueOf(redisCode)));
//            if (!code.equals(String.valueOf(redisCode))) return BaseJson.ofFail("验证码错误");
//            redisService.remove(systemConfig.getSmsPrefix().concat(account));
//        }
//        //1 找回密码 2 验证码登录 0 公共验证
//        if (type == 1) {
//            redisService.set(SystemConstants.SMS_FIND_FIND_SET_PASS_KEY.concat(account), 1, systemConfig.getSmsExpireTime());
//        } else if (type == 2) {
//            redisService.set(SystemConstants.SMS_LOGIN_KEY.concat(account), 1, systemConfig.getSmsExpireTime());
//        } else {
//            redisService.set(SystemConstants.SMS_PUBLIC_KEY.concat(account), 1, systemConfig.getSmsExpireTime());
//        }
//        Integer count = this.userService.selectCount(new EntityWrapper<User>().eq("phone", account));
//
//        return BaseJson.ofSuccess(MapUtil.build().put("register", count > 0).over());
//    }
//
//    @Override
//    public BaseJson resetPwd(AccountVo accountVo) {
//        String account = accountVo.getAccount();
//        String pwd = accountVo.getPwd();
//        String code = accountVo.getCode();
//
//        Object o = redisService.get(SystemConstants.SMS_FIND_FIND_SET_PASS_KEY.concat(account));
//        if (null == o) return BaseJson.ofFail("验证不通过,请重新获取验证码");
//
////        if (StrKit.isEmpty(code)) return BaseJson.ofFail("请输入验证码");
////        Object redisCode = redisService.get(systemConfig.getSmsPrefix().concat(account));
////        if (null == redisCode) return BaseJson.ofFail("验证码已过期,请重新获取");
////        if (!code.equals(String.valueOf(redisCode))) return BaseJson.ofFail("验证码错误");
//
//        if (StrKit.isEmpty(pwd)) return BaseJson.ofFail("密码不能为空");
//
//        redisService.remove(SystemConstants.SMS_FIND_FIND_SET_PASS_KEY.concat(account));
//
//        User dbUser = this.userService.selectOne(new EntityWrapper<User>().eq("phone", account));
//        Map<String, String> pwdMap = ToolUtil.pwdEncrypt(pwd);
//        User update = new User();
//        update.setId(Long.valueOf(dbUser.getId().toString()));
//        update.setPassword(pwdMap.get("pwd"));
//        update.setSalt(pwdMap.get("salt"));
//        update.setVersion(dbUser.getVersion());
//        Integer count = this.userMapper.updateById(update);
//
//        return count > 0 ? BaseJson.ofSuccess("密码找回成功") : BaseJson.ofFail("密码找回失败,请稍后再试");
//    }
//
//
//    @Autowired
//    AgentMapper agentMapper;
//    @Autowired
//    AgentInvitationUserMapper agentInvitationUserMapper;
//
//    @Override
//    @Transactional()
//    public BaseJson register(AccountVo accountVo) {
//        String account = accountVo.getAccount();
//        String pwd = accountVo.getPwd();
////        String code = accountVo.getCode();
//        String inviteCode = accountVo.getInviteCode();
//
//
//        if (StrKit.isEmpty(pwd)) return BaseJson.ofFail("密码不能为空");
//
//        InvitationCode invitationCode = null;
//
//        if (StrKit.isNotEmpty(inviteCode)) {
//            int inviteCodeLength = inviteCode.length();
//            if(inviteCodeLength < 12){ //用户邀请码
//                if (account != null && inviteCode.trim().equals(account.trim())) {
//                    return BaseJson.ofFail("不能邀请你自己");
//                }
//                invitationCode = this.invitationCodeMapper.selectByCode(inviteCode.trim());
//                if (invitationCode == null) {
//                    return BaseJson.ofFail("邀请码不存在");
//                }
//            }else {
//                int gender = accountVo.getGender();
//                if(gender == 1){
//                    return BaseJson.ofFail("邀请码不存在");
//                }else {
//                    Agent agent = agentMapper.selectAgentByCode(inviteCode);
//                    if(agent == null){
//                        return BaseJson.ofFail("邀请码不存在");
//                    }
//                }
//            }
//        }
//        redisService.remove(systemConfig.getSmsPrefix()+account);
//        if(StrKit.isNotEmpty(accountVo.getKey())){//表示三方登陆注册
//            int type = accountVo.getType();
//            if(type == 0){
//                Integer num = this.userService.selectCount(new EntityWrapper<User>().eq("qq", accountVo.getKey()));
//                if (num > 0) return BaseJson.ofFail("该三方账号已注册");
//            }
//            if(type == 1){
//                Integer num = this.userService.selectCount(new EntityWrapper<User>().eq("wx", accountVo.getKey()));
//                if (num > 0) return BaseJson.ofFail("该三方账号已注册");
//            }
//
//
//        }else {
//            Integer num = this.userService.selectCount(new EntityWrapper<User>().eq("phone", account));
//            if (num > 0) return BaseJson.ofFail("该手机号已注册");
//
//        }
//
//        Long userId = registerUser(accountVo);
//        boolean result = userId > 0;
//        if (result) {
//            User user = userService.selectById(userId);
//            //创建邀请码
//            invitationCodeService.createInvitationCode(userId, account);
//            if (StrKit.isNotEmpty(inviteCode)) {
//                int inviteCodeLength = inviteCode.length();
//
//                if (inviteCodeLength < 12) {//用户邀请码
//                    if (invitationCode != null) {//添加邀请人记录
//
//                        user.setBelongAgent(Integer.valueOf(invitationCode.getUserId()+""));
//                        userService.updateById(user);
//                        invitationCodeService.inviteUser(invitationCode, userId);
//                    }
//                } else {//代理商邀请码
//                    Integer gender = user.getGender();
//                    Agent agent = agentMapper.selectAgentByCode(inviteCode);
//                    if (gender == 2) {//女用户
//                        //绑定代理商
//                        user.setBelongAgent(agent.getId());
//                        userMapper.updateById(user);
//                    }
//                }
//            }
//        }
//        return result ? BaseJson.ofSuccess("注册成功") : BaseJson.ofFail("网络异常,请稍后再试");
//    }
//
//    @Override
//    public BaseJson inviteRegister(AccountVo accountVo) {
//        String account = accountVo.getAccount();
//        String pwd = accountVo.getPwd();
//        String code = accountVo.getCode();
//        String inviteCode = accountVo.getInviteCode();
//
//        Object redisCode = redisService.get(systemConfig.getSmsPrefix().concat(account));
//        if (null == redisCode) return BaseJson.ofFail("验证码已过期,请重新获取");
//        if (!code.equals(String.valueOf(redisCode))) return BaseJson.ofFail("验证码错误");
//
//        if (StrKit.isEmpty(inviteCode)) {
//            return BaseJson.ofFail("邀请码不能为空");
//        }
//
//        if (StrKit.isEmpty(pwd)) return BaseJson.ofFail("密码不能为空");
//
//        InvitationCode invitationCode = this.invitationCodeMapper.selectByCode(inviteCode.trim());
//        if (invitationCode == null) {
//            return BaseJson.ofFail("邀请码不存在");
//        }
//
//        redisService.remove(systemConfig.getSmsPrefix().concat(account));
//        User dbUser = this.userService.selectOne(new EntityWrapper<User>().eq("phone", account));
//        if (dbUser != null) return BaseJson.ofFail("该手机号已注册");
//
//        Long userId = registerUser(accountVo);
//        boolean result = userId > 0;
//        if (result) {
//            //创建邀请码
//            invitationCodeService.createInvitationCode(userId, account);
//            if (invitationCode != null) {
//                User user = userService.selectById(userId);
//                user.setBelongAgent(Integer.valueOf(invitationCode.getUserId()+""));
//                userService.updateById(user);
//                invitationCodeService.inviteUser(invitationCode, userId);
//            }
//        }
//        return result ? BaseJson.ofSuccess("注册成功") : BaseJson.ofFail("网络异常,请稍后再试");
//    }
//    @Autowired
//    CommonParamsMapper commonParamsMapper;
//    @Autowired
//    MessageMapper messageMapper;
//    @Override
//    public BaseJson updateUserInfo(AccountVo accountVo) {
//        String avatar = accountVo.getAvatar();
//        int gender = accountVo.getGender();
//        String name = accountVo.getName();
//        if(avatar==null||name==null){
//            return BaseJson.ofFail("参数错误");
//        }
//        User dbUser = userService.selectById(accountVo.getUserId());
//        dbUser.setAvatar(avatar);
//        dbUser.setName(name);
//        dbUser.setGender(gender);
//        boolean b = userService.updateById(dbUser);
//        if(b){
//            boolean b1 = chatUtils.regUser(dbUser);
//            if(!b1){
//                return BaseJson.ofFail("注册腾讯云失败");
//            }else {
//                updateUserImInfo(dbUser);
//            }
//
//            //如果是男用户注册送金豆
//            if(dbUser.getGender() ==  1){
//                CommonParams commonParams = commonParamsMapper.selectById(31);
//                BigDecimal registAmount = new BigDecimal(commonParams.getValue());
//                UserWallet  userWallet= userWalletMapper.selectByUserId(dbUser.getId());
//                userWallet.setGbMoeny(registAmount);
//                userWalletMapper.updateById(userWallet);
//                GoldbeanRecord goldbeanRecord = new GoldbeanRecord();
//                goldbeanRecord.setRecType(8);
//                goldbeanRecord.setUserId(dbUser.getId());
//                goldbeanRecord.setRemark("注册赠送");
//                goldbeanRecord.setAmount(registAmount);
//                goldbeanRecord.setWalletGbMoney(registAmount);
//                goldbeanRecordMapper.insert(goldbeanRecord);
//            }
//        }
//
//        return BaseJson.ofSuccess();
//    }
//
//
//    /**
//     * 注册用户
//     *
//     * @return 用户ID
//     */
//    private Long registerUser(AccountVo accountVo) {
//        int gender = accountVo.getGender();
//        String name = accountVo.getName();
//
//        String avatar = accountVo.getAvatar();
//        if(StrKit.isEmpty(avatar)){
//            avatar = systemConfig.getDefaultAvatar();
//        }
//        String city = accountVo.getCity();
////        String[] cityArr = city.split("\\-");
//        String account = accountVo.getAccount();
//        //三方登录opendId
//        String openId = accountVo.getKey();
//        //0 qq 1 wx
//        int type = accountVo.getType();
//        String pwd = accountVo.getPwd();
//        int regType = accountVo.getRegType();
//
//
//        Map<String, String> pwdMap = ToolUtil.pwdEncrypt(pwd);
////        String dnNum = getDnNUm();
//        User insert = new User();
//        insert.setAvatar(avatar);
//        insert.setGender(gender);
//        insert.setPhone(account);
//        insert.setPassword(pwdMap.get("pwd"));
//        insert.setSalt(pwdMap.get("salt"));
//        String rlbNum = ToolUtil.getRandomNum(6);
//        insert.setRlbNum(rlbNum);
//        insert.setName(name);
////        insert.setAvatar(ossUtil.getENV() ? ossUtil.getREQUEST_URL() + systemConfig.getDefaultAvatar() : ossUtil.getREQUEST_URL_TEST() + systemConfig.getDefaultAvatar());
////        insert.setAvatar(accountVo != null ? accountVo.getAvatar() : ossUtil.getREQUEST_URL_TEST() + systemConfig.getDefaultAvatar());
//        insert.setIntro("这个人很懒,什么都没留下~");
////        insert.setProvince(cityArr[0]);
////        insert.setCity(cityArr[1]);
////        insert.setDnNum(dnNum);
//        insert.setSource(regType);
//        Date birthday = new Date();
//        int age = DateTimeKit.ageOfNow(birthday);
//        insert.setAge(age);
//        String constellation = DateTimeKit.constellation(birthday);
//        insert.setBirthday(new Date());
//        insert.setIntro("本宝宝暂时没有想到个性的签名~");
//        //三方登录直接注册
//        if (StrKit.isNotEmpty(openId)) {
//            if (type == 0) {
//                insert.setQq(openId);
//                insert.setRegisteredState(2);
//            } else {
//                insert.setRegisteredState(1);
//                insert.setWx(openId);
//            }
//        }
//
//        this.userMapper.insert(insert);
//        Long userId = insert.getId();
//
//        if (userId == null || userId <= 0) return 0L;
//
//
//
//
//        if(StrKit.isNotEmpty(insert.getName())){
//
//            boolean result = this.regTXChat(insert);
//            if (result) {
//                updateUserImInfo(insert);
//                //创建用户钱包;
//                goldBeanAndWalletService.generateUserWallet(userId);
//
//                //如果是男用户注册送金豆
//                if(insert.getGender() ==  1){
//                    CommonParams commonParams = commonParamsMapper.selectById(31);
//                    BigDecimal registAmount = new BigDecimal(commonParams.getValue());
//                    UserWallet userWallet = userWalletMapper.selectByUserId(insert.getId());
//                    userWallet.setGbMoeny(registAmount);
//                    userWalletMapper.updateById(userWallet);
//                    GoldbeanRecord goldbeanRecord = new GoldbeanRecord();
//                    goldbeanRecord.setRecType(8);
//                    goldbeanRecord.setUserId(insert.getId());
//                    goldbeanRecord.setRemark("注册赠送");
//                    goldbeanRecord.setAmount(registAmount);
//                    goldbeanRecord.setWalletGbMoney(registAmount);
//                    goldbeanRecordMapper.insert(goldbeanRecord);
//                }
//                return userId;
//            } else {
//                throw new SelfNoticeException("注册失败,请稍后再试");
//            }
//        }else {
//            goldBeanAndWalletService.generateUserWallet(userId);
//            return userId;
//        }
//    }
//
//
//
//
//    @Override
//    public BaseJson modifyPhone(AccountVo accountVo) {
//        Long userId = accountVo.getUserId();
//        String account = accountVo.getAccount();
//        String code = accountVo.getCode();
//
//        if (StrKit.isEmpty(account)) {
//            return BaseJson.ofFail("电话号码不能为空");
//        }
//
////        if (StrKit.isEmpty(code)) return BaseJson.ofFail("请输入验证码");
//////        Object redisCode = redisService.get(systemConfig.getSmsPrefix().concat(account));
////        Object redisCode = redisService.get(SystemConstants.SMS_PUBLIC_KEY.concat(account));
////        if (null == redisCode) return BaseJson.ofFail("验证码已过期,请重新获取");
////        if (!code.equals(String.valueOf(redisCode))) return BaseJson.ofFail("验证码错误");
////
////        redisService.remove(systemConfig.getSmsPrefix().concat(account));
//
//        User dbUser = userMapper.selectById(userId);
//        if (dbUser == null) {
//            return BaseJson.userNotExist();
//        }
//
//        if (dbUser.getPhone().equals(account)) {
//            return BaseJson.ofFail("新手机号不能是当前手机号");
//        }
//
//        User phoneUser = this.userService.selectOne(new EntityWrapper<User>().eq("phone", account));
//        if (phoneUser != null) {
//            return BaseJson.ofFail("该手机号已注册");
//        }
//
//        User update = new User();
//        update.setId(dbUser.getId());
//        update.setPhone(account);
//        update.setVersion(dbUser.getVersion());
//        update.setUpdateTime(new Date());
//        Integer count = userMapper.updateById(update);
//
//        //修改手机号,更新用户邀请码
//        InvitationCode invitationCode = invitationCodeMapper.selectOneByUserId(userId);
//        if (invitationCode != null) {
//            InvitationCode updateCode = new InvitationCode();
//            updateCode.setId(invitationCode.getId());
//            updateCode.setCode(account);
//            invitationCodeMapper.updateById(invitationCode);
//        }
//
//        return count > 0 ? BaseJson.ofSuccess("修改成功") : BaseJson.ofFail("修改失败,请稍后再试");
//    }
//
//    @Override
//    public BaseJson modifyPwd(AccountVo accountVo) {
//        Long userId = accountVo.getUserId();
//        String newPwd = accountVo.getPwd();
//        String oldPwd = accountVo.getOldPwd();
//        if (StrKit.isEmpty(newPwd)) {
//            return BaseJson.ofFail("请输入密码");
//        }
//
//        User dbUser = userMapper.selectById(userId);
//        if (dbUser == null) {
//            return BaseJson.userNotExist();
//        }
////        String phone = dbUser.getPhone();
////        Object o = redisService.get(SystemConstants.SMS_FIND_FIND_SET_PASS_KEY.concat(phone));
////        if (null == o) return BaseJson.ofFail("验证不通过,请重新获取验证码");
////
////        redisService.remove(SystemConstants.SMS_FIND_FIND_SET_PASS_KEY.concat(phone));
//
//        String passWord = ToolUtil.pwdEncrypt(oldPwd,dbUser.getSalt());
//        if (!dbUser.getPassword().equals(passWord)){
//            return BaseJson.ofFail("输入密码错误");
//        }
//        Map<String, String> pwdMap = ToolUtil.pwdEncrypt(newPwd);
//        User update = new User();
//        update.setId(dbUser.getId());
//        update.setPassword(pwdMap.get("pwd"));
//        update.setSalt(pwdMap.get("salt"));
//        update.setVersion(dbUser.getVersion());
//        update.setUpdateTime(new Date());
//        Integer count = this.userMapper.updateById(update);
//
//        return count > 0 ? BaseJson.ofSuccess("修改成功") : BaseJson.ofFail("修改失败,请稍后再试");
//    }
//
//    @Override
//    public void changeUserIm() {
//        try {
//            List<User> users = userMapper.selectList(null);
//            int i = 0;
//            for (User user : users) {
//                if (i == 10) {
//                    log.info("【用户重置】,注册用户超过10人，休眠10秒");
//                    Thread.sleep(10000L);
//                    i = 0;
//                }
//                boolean result = regTXChat(user);
//                if (result) {
//                    updateUserImInfo(user);
//                    chatUtils.getUserSign((systemConfig.getLiveYunUserSignPrefix() + user.getId()));
//                }
//                log.info("【用户重置】,用户ID={},注册结果={}", user.getId(), result);
//                i++;
//                Thread.sleep(2000L);
//            }
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 创建用户钱包
//     *
//     * @param userId
//     */
//    private void createUserWallet(Long userId) {
//        UserWallet insert = new UserWallet();
//        insert.setUserId(userId);
//        insert.setMoney(BigDecimal.ZERO);
//        insert.setMoney(BigDecimal.ZERO);
//        insert.setGbMoeny(BigDecimal.ZERO);
//        insert.setCreateTime(new Date());
//        insert.setUpdateTime(new Date());
//        this.userWalletMapper.insert(insert);
//    }
//
//
//    /**
//     * @description: 更新用户二维码及IM信息;
//     * @author: lx
//     */
//    private void updateUserImInfo(User user) {
//        Long userId = user.getId();
//        User dbUser = this.userMapper.selectById(userId);
//        User update = new User();
//        update.setId(userId);
//        update.setImId(systemConfig.getLiveYunUserSignPrefix() + userId);
//        update.setUpdateTime(new Date());
//        update.setVersion(dbUser.getVersion());
//        Integer count = this.userMapper.updateById(update);
//
//        //更新成功,入缓存
//        if (count > 0) {
//            dbUser.setImId(update.getImId());
//            dbUser.setUpdateTime(update.getUpdateTime());
//            redisService.toCache(SystemConstants.USER_REDIS_KEY + userId, dbUser);
//        }
//    }
//
//    /**
//     * 注册腾讯云
//     *
//     * @param user
//     * @return
//     */
//    public boolean regTXChat(User user) {
//        return chatUtils.regUser(user);
//    }
//}
