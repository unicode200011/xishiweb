package com.stylefeng.guns.admin.modular.system.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.google.code.kaptcha.Constants;
import com.stylefeng.guns.admin.common.annotion.BussinessLog;
import com.stylefeng.guns.admin.common.exception.InvalidKaptchaException;
import com.stylefeng.guns.admin.config.properties.GlobleProperties;
import com.stylefeng.guns.core.base.tips.ErrorTip;
import com.stylefeng.guns.core.base.tips.Tip;
import com.stylefeng.guns.core.support.HttpKit;
import com.stylefeng.guns.core.support.ObjectKit;
import com.stylefeng.guns.core.util.HTTPUtils;
import com.stylefeng.guns.core.util.MapUtil;
import com.stylefeng.guns.core.util.redis.RedisService;
import com.stylefeng.guns.persistence.dao.AdminMapper;
import com.stylefeng.guns.persistence.dao.OperationLogMapper;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.admin.core.log.LogManager;
import com.stylefeng.guns.admin.core.log.factory.LogTaskFactory;
import com.stylefeng.guns.core.node.MenuNode;
import com.stylefeng.guns.admin.core.shiro.ShiroKit;
import com.stylefeng.guns.admin.core.shiro.ShiroUser;
import com.stylefeng.guns.admin.core.util.ApiMenuFilter;
import com.stylefeng.guns.admin.core.util.KaptchaUtil;
import com.stylefeng.guns.core.util.ToolUtil;
import com.stylefeng.guns.admin.modular.system.dao.MenuDao;
import com.stylefeng.guns.persistence.model.Admin;
import com.stylefeng.guns.persistence.model.OperationLog;
import com.stylefent.guns.entity.message.RpcRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.CredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.stylefeng.guns.core.support.HttpKit.getIp;

/**
 * 登录控制器
 *
 * @author fengshuonan
 * @Date 2017年1月10日 下午8:25:24
 */
@Controller
@Slf4j
public class LoginController extends BaseController {
    @Resource
    MenuDao menuDao;

    @Resource
    AdminMapper adminMapper;

    @Resource
    private OperationLogMapper operationLogMapper;
    @Autowired
    GlobleProperties globleProperties;
    @Autowired
    RedisService redisService;
    /**
     * 跳转到主页
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(Model model) {
        //获取菜单列表
        List<Integer> roleList = ShiroKit.getUser().getRoleList();
        if (roleList == null || roleList.size() == 0) {
            ShiroKit.getSubject().logout();
            model.addAttribute("tips", "该用户没有角色，无法登陆");
            return "/login.html";
        }
        for (Integer integer : roleList) {
            if(integer != 2){
                model.addAttribute("tips", "用户不存在，无法登陆");
                return "/login.html";
            }
        }
        List<MenuNode> menus = menuDao.getMenusByRoleIds(roleList);
        List<MenuNode> titles = MenuNode.buildTitle(menus);
        titles = ApiMenuFilter.build(titles);

        model.addAttribute("titles", titles);
        //获取用户头像
        Integer id = ShiroKit.getUser().getId();
        String avatar = "";
        try {
            Admin admin = adminMapper.selectById(id);
            avatar = admin.getAvatar();
        } catch (Exception e) {
            return "/login.html";
        }
        model.addAttribute("avatar", avatar);

        return "/index.html";
    }

    /**
     * 跳转到登录页面
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(Model model) {
        if (ShiroKit.isAuthenticated() || ShiroKit.getUser() != null) {
            return REDIRECT + "/";
        } else {
            model.addAttribute("loginError","200");
            return "/login.html";
        }
    }

//    @Autowired
//    NettyClient nettyClient;

    /**
     * 点击登录执行的动作
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @BussinessLog(value = "管理员登录")
    public String loginVali(Model model) {

        String username = super.getPara("username").trim();
        String password = super.getPara("password").trim();
        String remember = super.getPara("remember");
//        RpcRequest rpcRequest = new RpcRequest();
//        rpcRequest.setId("admin login");
//        rpcRequest.setData("admin "+username+" 登录");
//        nettyClient.sendMsg(rpcRequest);
        //验证验证码是否正确
        if (KaptchaUtil.getKaptchaOnOff()) {
            String kaptcha = super.getPara("kaptcha").trim();
            String code = (String) super.getSession().getAttribute(Constants.KAPTCHA_SESSION_KEY);
            if (ToolUtil.isEmpty(kaptcha) || !kaptcha.equalsIgnoreCase(code)) {
                throw new InvalidKaptchaException();
            }
        }

        Subject currentUser = ShiroKit.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username, password.toCharArray());

        if ("on".equals(remember)) {
            token.setRememberMe(true);
        } else {
            token.setRememberMe(false);
        }

        currentUser.login(token);

        ShiroUser shiroUser = ShiroKit.getUser();
        super.getSession().setAttribute("shiroUser", shiroUser);
        super.getSession().setAttribute("username", shiroUser.getAccount());

        LogManager.me().executeLog(LogTaskFactory.loginLog(shiroUser.getId(), getIp()));

        ShiroKit.getSession().setAttribute("sessionFlag", true);


        return REDIRECT + "/";
    }

    /**
     * 退出登录
     */
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logOut() {
        OperationLog operationLog = new OperationLog();
        operationLog.setAdminid(ShiroKit.getUser().getId());
        operationLog.setLogname("管理员退出登录");
        operationLog.setLogtype("业务日志");
        operationLog.setCreateTime(new Date());
        operationLogMapper.insert(operationLog);
        LogManager.me().executeLog(LogTaskFactory.exitLog(ShiroKit.getUser().getId(), getIp()));
        ShiroKit.getSubject().logout();
        return REDIRECT + "/login";
    }

    //跳转忘记密码页
    @RequestMapping(value = "/forget")
    public String forget() {
        return  "/forgetPwd.html";
    }


    //获取验证码
    @RequestMapping(value = "/getCode")
    @ResponseBody
    public Object getCode(String phone){
        Integer phone1 = adminMapper.selectCount(new EntityWrapper<Admin>().eq("phone", phone));
        if(phone1 < 1){
            return new ErrorTip(501,"手机号未注册");
        }
        String s = this.doPost(phone);
        Map map = (Map) JSONObject.parse(s);
        return SUCCESS_TIP;
    }

    //验证验证码
    @RequestMapping(value = "/checkCode")
    @ResponseBody
    public Object checkCode(String code,String phone){
        boolean exists = redisService.exists(phone);
        if(!exists){
            return new ErrorTip(405,"验证码错误");
        }
        Object o = redisService.get(phone);
        if(!ObjectKit.equals(o,code)){
            return new ErrorTip(405,"验证码错误");
        }
        return SUCCESS_TIP;
    }


    public String doPost(String phone){
        Map map = new HashMap();
        map.put("phone",phone);
        String url = globleProperties.getGetCodeUrl();
        String result = HttpKit.sendPost(url, JSONObject.toJSONString(map));
        System.err.println("result==【"+result+"】");
        return result;
    }

}
