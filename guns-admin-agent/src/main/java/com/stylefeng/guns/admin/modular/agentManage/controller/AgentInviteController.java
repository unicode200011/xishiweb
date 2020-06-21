package com.stylefeng.guns.admin.modular.agentManage.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.admin.common.annotion.BussinessLog;
import com.stylefeng.guns.admin.core.log.LogObjectHolder;
import com.stylefeng.guns.admin.core.shiro.ShiroKit;
import com.stylefeng.guns.admin.modular.agentManage.service.IAgentInviteService;
import com.stylefeng.guns.admin.modular.agentManage.service.IAgentService;
import com.stylefeng.guns.admin.modular.userManage.service.IUserService;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.persistence.model.Agent;
import com.stylefeng.guns.persistence.model.AgentInvite;
import com.stylefeng.guns.persistence.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 家族申请记录控制器
 *
 * @author stylefeng
 * @Date 2019-12-30 13:47:33
 */
@Controller
@RequestMapping("/agentInvite")
public class AgentInviteController extends BaseController {

    private String PREFIX = "/agentManage/agentInvite/";

    @Autowired
    private IAgentInviteService agentInviteService;

    @Autowired
    private IAgentService agentService;
    @Autowired
    private IUserService userService;
    /**
     * 跳转到家族申请记录首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "agentInvite.html";
    }

    /**
     * 跳转到添加家族申请记录
     */
    @RequestMapping("/agentInvite_add")
    public String agentInviteAdd() {
        return PREFIX + "agentInvite_add.html";
    }

    /**
     * 跳转到修改家族申请记录
     */
    @RequestMapping("/agentInvite_update/{agentInviteId}")
    public String agentInviteUpdate(@PathVariable Integer agentInviteId, Model model) {
        AgentInvite agentInvite = agentInviteService.selectById(agentInviteId);
        User user = userService.selectById(agentInvite.getUserId());
        agentInvite.setName(user==null?"":user.getName());
        agentInvite.setXishiNum(user==null?"":user.getXishiNum());
        model.addAttribute("item",agentInvite);
        LogObjectHolder.me().set(agentInvite);
        return PREFIX + "agentInvite_edit.html";
    }

    /**
     * 获取家族申请记录列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(@RequestParam(required = false, defaultValue = "1") Integer pageNumber,
                       @RequestParam(required = false, defaultValue = "20") Integer pageSize,
                       String condition) {
        EntityWrapper<AgentInvite> entityWrapper = new EntityWrapper();
        Integer deptId = ShiroKit.getUser().getDeptId();
        Agent agent = agentService.selectById(deptId);
        entityWrapper.eq("agent_id", agent.getId());
        entityWrapper.eq("type", 0);
        entityWrapper.orderBy("id",false);
        Page<AgentInvite> page = agentInviteService.selectPage(new Page<AgentInvite>(pageNumber, pageSize), entityWrapper);
        List<AgentInvite> records = page.getRecords();
        for (AgentInvite record : records) {
            User user = userService.selectById(record.getUserId());
            record.setName(user==null?"":user.getName());
            record.setXishiNum(user==null?"":user.getXishiNum());
        }
        return page;
    }

    /**
     * 新增家族申请记录
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    @BussinessLog(value = "新增家族申请记录")
    public Object add(AgentInvite agentInvite) {
        agentInviteService.insert(agentInvite);
        return super.SUCCESS_TIP;
    }

    /**
     * 删除家族申请记录
     */
    @RequestMapping(value = "/delete")
    @BussinessLog(value = "删除家族申请记录")
    @ResponseBody
    public Object delete(@RequestParam Long agentInviteId) {
        agentInviteService.deleteById(agentInviteId);
        return SUCCESS_TIP;
    }

    /**
     * 修改家族申请记录
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    @BussinessLog(value = "修改家族申请记录")
    public Object update(Long id,Integer state) {
        AgentInvite agentInvite = agentInviteService.selectById(id);
        agentInvite.setJoinState(state);
        if(state == 1){ //通过
            User user = userService.selectById(agentInvite.getUserId());
            if(user != null){
                user.setBelongAgent(agentInvite.getAgentId());
                user.setApplyAgent(1);
                user.setNewestApplyAgentId(agentInvite.getAgentId());
                userService.updateById(user);
            }
        }
        agentInviteService.updateById(agentInvite);
        return super.SUCCESS_TIP;
    }

    /**
     * 家族申请记录详情
     */
    @RequestMapping(value = "/detail/{agentInviteId}")
    @ResponseBody
    public Object detail(@PathVariable("agentInviteId") Integer agentInviteId) {
        return agentInviteService.selectById(agentInviteId);
    }
}
