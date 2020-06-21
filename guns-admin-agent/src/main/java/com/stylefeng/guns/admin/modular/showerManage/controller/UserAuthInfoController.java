package com.stylefeng.guns.admin.modular.showerManage.controller;

import com.stylefeng.guns.admin.common.annotion.BussinessLog;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.admin.modular.agentManage.service.IAgentService;
import com.stylefeng.guns.admin.modular.userManage.service.IUserService;
import com.stylefeng.guns.core.support.StrKit;
import com.stylefeng.guns.persistence.model.Agent;
import com.stylefeng.guns.persistence.model.User;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import com.stylefeng.guns.core.base.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.stylefeng.guns.admin.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import com.stylefeng.guns.persistence.model.UserAuthInfo;
import com.stylefeng.guns.admin.modular.showerManage.service.IUserAuthInfoService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 主播申请控制器
 *
 * @author stylefeng
 * @Date 2019-11-27 14:20:52
 */
@Controller
@RequestMapping("/userAuthInfo")
public class UserAuthInfoController extends BaseController {

    private String PREFIX = "/showerManage/userAuthInfo/";

    @Autowired
    private IUserAuthInfoService userAuthInfoService;
    @Autowired
    private IUserService userService;
    @Autowired
    private IAgentService agentService;
    /**
     * 跳转到主播申请首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "userAuthInfo.html";
    }

    /**
     * 跳转到添加主播申请
     */
    @RequestMapping("/userAuthInfo_add")
    public String userAuthInfoAdd() {
        return PREFIX + "userAuthInfo_add.html";
    }

    /**
     * 跳转到修改主播申请
     */
    @RequestMapping("/userAuthInfo_update/{userAuthInfoId}")
    public String userAuthInfoUpdate(@PathVariable Integer userAuthInfoId, Model model) {
        UserAuthInfo userAuthInfo = userAuthInfoService.selectById(userAuthInfoId);
        model.addAttribute("item",userAuthInfo);
        LogObjectHolder.me().set(userAuthInfo);
        return PREFIX + "userAuthInfo_edit.html";
    }

    /**
     * 获取主播申请列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(@RequestParam(required = false, defaultValue = "1") Integer pageNumber,
                       @RequestParam(required = false, defaultValue = "20") Integer pageSize,
                       String phone,String name,String state,String xishiNum) {
        EntityWrapper<UserAuthInfo> entityWrapper = new EntityWrapper();
        if(StrKit.isNotEmpty(phone)){
            List<User> users = userService.selectList(new EntityWrapper<User>().like("phone", phone));
            List<Long> collect = users.stream().map(User::getId).collect(Collectors.toList());
            if(CollectionUtils.isNotEmpty(collect)){
                entityWrapper.in("user_id",collect);
            }else {
                entityWrapper.eq("user_id",0);
            }
        }
        if(StrKit.isNotEmpty(name)){
            List<User> users = userService.selectList(new EntityWrapper<User>().like("name", name));
            List<Long> collect = users.stream().map(User::getId).collect(Collectors.toList());
            if(CollectionUtils.isNotEmpty(collect)){
                entityWrapper.in("user_id",collect);
            }else {
                entityWrapper.eq("user_id",0);
            }
        }
        if(StrKit.isNotEmpty(xishiNum)){
            List<User> users = userService.selectList(new EntityWrapper<User>().like("xishi_num", xishiNum));
            List<Long> collect = users.stream().map(User::getId).collect(Collectors.toList());
            if(CollectionUtils.isNotEmpty(collect)){
                entityWrapper.in("user_id",collect);
            }else {
                entityWrapper.eq("user_id",0);
            }
        }
        if(StrKit.isNotEmpty(state)){
                entityWrapper.eq("state",state);
        }
        entityWrapper.orderBy("id",false);
        Page<UserAuthInfo> page = userAuthInfoService.selectPage(new Page<UserAuthInfo>(pageNumber, pageSize), entityWrapper);
        List<UserAuthInfo> records = page.getRecords();
        for (UserAuthInfo record : records) {
            User user = userService.selectById(record.getUserId());
            record.setXishiNum(user.getXishiNum());
            record.setPhone(user.getPhone());
            record.setUserName(user.getName());
            Agent agent = agentService.selectById(user.getBelongAgent());
            record.setBelongAgentName(agent==null?"":agent.getAgentName());
        }
        return page;
    }

    /**
     * 新增主播申请
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    @BussinessLog(value = "新增主播申请")
    public Object add(UserAuthInfo userAuthInfo) {
        userAuthInfoService.insert(userAuthInfo);
        return super.SUCCESS_TIP;
    }

    /**
     * 删除主播申请
     */
    @RequestMapping(value = "/delete")
    @BussinessLog(value = "删除主播申请")
    @ResponseBody
    public Object delete(@RequestParam Long userAuthInfoId) {
        userAuthInfoService.deleteById(userAuthInfoId);
        return SUCCESS_TIP;
    }

    /**
     * 修改主播申请
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    @BussinessLog(value = "修改主播申请")
    public Object update(UserAuthInfo userAuthInfo) {
        userAuthInfoService.updateById(userAuthInfo);
        return super.SUCCESS_TIP;
    }

    /**
     * 主播申请详情
     */
    @RequestMapping(value = "/detail/{userAuthInfoId}")
    @ResponseBody
    public Object detail(@PathVariable("userAuthInfoId") Integer userAuthInfoId) {
        return userAuthInfoService.selectById(userAuthInfoId);
    }
}
