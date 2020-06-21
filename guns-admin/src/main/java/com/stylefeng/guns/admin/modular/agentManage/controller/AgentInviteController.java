package com.stylefeng.guns.admin.modular.agentManage.controller;

import com.stylefeng.guns.admin.common.annotion.BussinessLog;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import com.stylefeng.guns.core.base.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.stylefeng.guns.admin.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import com.stylefeng.guns.persistence.model.AgentInvite;
import com.stylefeng.guns.admin.modular.agentManage.service.IAgentInviteService;

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
        entityWrapper.orderBy("id",false);
        Page<AgentInvite> page = agentInviteService.selectPage(new Page<AgentInvite>(pageNumber, pageSize), entityWrapper);
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
    public Object update(AgentInvite agentInvite) {
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
