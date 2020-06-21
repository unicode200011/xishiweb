package com.stylefeng.guns.admin.modular.userManage.controller;

import com.stylefeng.guns.admin.common.annotion.BussinessLog;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.admin.core.shiro.ShiroKit;
import com.stylefeng.guns.admin.core.util.ExportExcelSeedBack;
import com.stylefeng.guns.admin.modular.agentManage.service.IAgentInviteService;
import com.stylefeng.guns.admin.modular.agentManage.service.IAgentService;
import com.stylefeng.guns.admin.modular.liveManage.service.ILiveService;
import com.stylefeng.guns.admin.modular.moneyManage.service.IUserWalletService;
import com.stylefeng.guns.core.exception.GunsException;
import com.stylefeng.guns.core.support.StrKit;
import com.stylefeng.guns.core.util.ToolUtil;
import com.stylefeng.guns.persistence.model.*;
import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import com.stylefeng.guns.core.base.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.stylefeng.guns.admin.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import com.stylefeng.guns.admin.modular.userManage.service.IUserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 用户列表控制器
 *
 * @author stylefeng
 * @Date 2019-10-10 15:20:17
 */
@Controller
@RequestMapping("/user")
public class UserController extends BaseController {

    private String PREFIX = "/userManage/user/";

    @Autowired
    private IUserService userService;
    @Autowired
    private IAgentService agentService;
    @Autowired
    private IUserWalletService userWalletService;
    @Autowired
    private IAgentInviteService agentInviteService;
    @Autowired
    private ILiveService liveService;
    /**
     * 跳转到用户列表首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "shower.html";
    }

    /**
     * 跳转到添加用户列表
     */
    @RequestMapping("/user_add")
    public String userAdd() {
        return PREFIX + "user_add.html";
    }

    /**
     * 跳转到修改用户列表
     */
    @RequestMapping("/user_update/{userId}")
    public String userUpdate(@PathVariable Integer userId, Model model) {
        User user = userService.selectById(userId);
        model.addAttribute("item",user);
        LogObjectHolder.me().set(user);
        UserWallet userWallet = userWalletService.selectOne(new EntityWrapper<UserWallet>().eq("user_id", user.getId()));
        user.setGiftAmount(userWallet.getGiftAmount());
        user.setGbMoeny(userWallet.getGbMoeny());
        Live live = liveService.selectOne(new EntityWrapper<Live>().eq("user_id", user));
        if(live != null){
            user.setTotalTime(live.getTotalLiveTime());
        }
        AgentInvite agentInvite = agentInviteService.selectOne(new EntityWrapper<AgentInvite>().eq("agent_id", ShiroKit.getUser().getDeptId()).eq("user_id", userId));
        if(agentInvite != null){
            user.setJoinState(agentInvite.getJoinState());
        }
        model.addAttribute("inviteId",agentInvite==null?-1:agentInvite.getId());
        return PREFIX + "user_edit.html";
    }

    /**
     * 获取用户列表列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(@RequestParam(required = false, defaultValue = "1") Integer pageNumber,
                       @RequestParam(required = false, defaultValue = "20") Integer pageSize,
                       String state,String name,String phone,String xishiNum) {
        EntityWrapper<User> entityWrapper = new EntityWrapper();
        if(StrKit.isNotEmpty(state))entityWrapper.eq("state",state);
        if(StrKit.isNotEmpty(name))entityWrapper.like("name",name);
        if(StrKit.isNotEmpty(phone))entityWrapper.like("phone",phone);
        if(StrKit.isNotEmpty(xishiNum))entityWrapper.like("xishi_num",xishiNum);
        entityWrapper.orderBy("id",false);
        Page<User> page = userService.selectPage(new Page<User>(pageNumber, pageSize), entityWrapper);
        System.out.println(page.getRecords());
        return page;
    }
    /**
     * 获取用户列表列表
     */
    @RequestMapping(value = "/showerList")
    @ResponseBody
    public Object showerList(@RequestParam(required = false, defaultValue = "1") Integer pageNumber,
                             @RequestParam(required = false, defaultValue = "20") Integer pageSize,
                             String state,String name,String phone,String xishiNum,String joinState) {
        EntityWrapper<User> entityWrapper = new EntityWrapper();
        if(StrKit.isNotEmpty(state))entityWrapper.eq("u.state",state);
        if(StrKit.isNotEmpty(name))entityWrapper.like("u.name",name);
        if(StrKit.isNotEmpty(phone))entityWrapper.like("u.phone",phone);
        if(StrKit.isNotEmpty(xishiNum))entityWrapper.like("u.xishi_num",xishiNum);
        if(StrKit.isNotEmpty(joinState))entityWrapper.eq("ai.join_state",joinState);
        Integer deptId = ShiroKit.getUser().getDeptId();
        Agent agent = agentService.selectById(deptId);
        //entityWrapper.eq("ai.agent_id", agent.getId());
        entityWrapper.eq("u.belong_agent", agent.getId());
//        entityWrapper.ne("u.id",agent.getUserId());
        entityWrapper.orderBy("ai.id",false);
        entityWrapper.groupBy("u.id");
        Page<User> page = userService.selectAgentShowerPage(new Page<User>(pageNumber, pageSize), entityWrapper);
        return page;
    }
    /**
     * 新增用户列表
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    @BussinessLog(value = "新增用户列表")
    public Object add(User user) {
        userService.insert(user);
        return super.SUCCESS_TIP;
    }

    /**
     * 删除用户列表
     */
    @RequestMapping(value = "/delete")
    @BussinessLog(value = "删除用户列表")
    @ResponseBody
    public Object delete(@RequestParam Long userId) {
        userService.deleteById(userId);
        return SUCCESS_TIP;
    }

    /**
     * 修改用户列表
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    @BussinessLog(value = "修改用户列表")
    public Object update(User user) {
        userService.updateById(user);
        return super.SUCCESS_TIP;
    }

    @RequestMapping(value = "/changeState")
    @BussinessLog(value = "改变用户状态")
    @ResponseBody
    public Object changeState(@RequestParam Long userId,Integer state) {
        User user = userService.selectById(userId);
        user.setState(state);
        user.setUpdateTime(new Date());
        userService.updateById(user);
        return SUCCESS_TIP;
    }

    @RequestMapping(value = "/submitRate")
    @BussinessLog(value = "改变用户抽成设置")
    @ResponseBody
    public Object submitRate(@RequestParam Long userId,Integer rate) {
        User user = userService.selectById(userId);
        user.setAgentRate(BigDecimal.valueOf(rate));
        user.setUpdateTime(new Date());
        userService.updateById(user);
        return SUCCESS_TIP;
    }
    @RequestMapping(value = "/auditUserApply")
    @BussinessLog(value = "审核用户加入家族")
    @ResponseBody
    public Object auditUserApply(@RequestParam Long inviteId,Integer state) {
        AgentInvite agentInvite = agentInviteService.selectById(inviteId);
        Integer type = agentInvite.getType();
        if(type == 1){
            throw new GunsException(501,"该数据不是用户申请数据");
        }
        agentInvite.setJoinState(state);
        agentInviteService.updateById(agentInvite);

        if(state == 1){
            User user = userService.selectById(agentInvite.getUserId());
            user.setBelongAgent(agentInvite.getAgentId());
            userService.updateById(user);
        }
        return SUCCESS_TIP;
    }
    /**
     * 用户列表详情
     */
    @RequestMapping(value = "/detail/{userId}")
    @ResponseBody
    public Object detail(@PathVariable("userId") Integer userId) {

        return userService.selectById(userId);
    }


    @RequestMapping(value = "/export")
    public void export(HttpServletRequest request, HttpServletResponse response, String state,String name,String phone,String xishiNum) {
        String title = "用户列表" + ToolUtil.getNowEpochSecond() + ".xls";
        String[] headers = new String[]{
                "序号",
                "西施号",
                "用户头像",
                "用户昵称",
                "绑定手机号",
                "会员等级",
                "累计打赏（西施币）",
                "当前余额（西施币）",
                "最近充值（元）",
                "累计充值（元）",
                "累计获取（西施币）",
                "在线状态",
                "当前状态",
        };
        EntityWrapper<User> entityWrapper = new EntityWrapper();
        if(StrKit.isNotEmpty(state))entityWrapper.eq("state",state);
        if(StrKit.isNotEmpty(name))entityWrapper.like("name",name);
        if(StrKit.isNotEmpty(phone))entityWrapper.like("phone",phone);
        if(StrKit.isNotEmpty(xishiNum))entityWrapper.like("xishi_num",xishiNum);
        entityWrapper.orderBy("id",false);
        List<User> records = userService.selectList(entityWrapper);

        List<Object[]> dataList = new ArrayList<>(records.size());
        Object[] objs;
        int len = headers.length;
        for (User one : records) {
            objs = new Object[len];
            objs[0] = 0;
            objs[1] = one.getXishiNum();
            objs[2] = one.getAvatar();
            objs[3] = one.getName();
            objs[4] = one.getPhone();
            objs[5] = one.getUserLevel();
            objs[6] = one.getGiveAmount();
            objs[7] = one.getGbMoeny();
            objs[8] = one.getNearCharge();

            objs[9] = one.getTotalCharge();
            objs[10] = one.getGiftAmount();
            Integer onlineState = one.getOnlineState();
            objs[11] = onlineState == 0?"离线":onlineState == 1?"在线":onlineState == 2?"忙碌":"";
            Integer stateVal = one.getState();
            objs[12] = stateVal == 0?"正常":stateVal == 1?"禁用":"";
            dataList.add(objs);
        }
        //使用流将数据导出
        OutputStream out = null;
        try {
            //防止中文乱码
            String headStr = "attachment; filename=\"" + new String(title.getBytes("gb2312"), "ISO8859-1") + "\"";
            response.setContentType("octets/stream");
            response.setContentType("APPLICATION/OCTET-STREAM");
            response.setHeader("Content-Disposition", headStr);
            out = response.getOutputStream();
            ExportExcelSeedBack ex = new ExportExcelSeedBack(title, headers, dataList);//没有标题
            ex.export(out);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(out);
        }
    }



}
